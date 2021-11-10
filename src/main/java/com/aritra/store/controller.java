package com.aritra.store;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class controller {

	@Autowired
	AccountRepository repo;
	
	@Autowired
	ContentRepo conr;
	
	@Autowired
	OrederRepo orr;
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		account ob = new account();
		model.addAttribute("account", ob);
		return "register";
	}
	
	@PostMapping("/register")
	public String signedup(@ModelAttribute("account") account ob,Model model) {
		String msg;
		if(!repo.existsByEmail(ob.getEmail())) {
			repo.save(ob); 
			model.addAttribute("msg", "Success");
			msg = "Success";
		}else {
			model.addAttribute("msg", "Failed");
			msg = "Failed";
		}	
		return "redirect:/login/"+msg;
	}
	
	@GetMapping("/login/{msg}")
	public String login(Model model,@PathVariable("msg") String msg) {
		account ob = new account();
		model.addAttribute("account", ob);
		model.addAttribute("msg", msg);
		return "login";
	}
	
	@PostMapping("/dashboard")
	public String dashboard(@ModelAttribute("account") account ob, Model model) {
		content con = new content();
		model.addAttribute("content",con);
		if(ob.getEmail().equals("admin@email.com") && ob.getPassword().equals("admin1234")) {
			List<content> item = new ArrayList<>();
			item = conr.findAll();
			model.addAttribute("items", item);
			return "admin";
		}
		else if(repo.findByEmailAndPassword(ob.getEmail(),ob.getPassword()) != null){
			account obj = repo.findByEmailAndPassword(ob.getEmail(),ob.getPassword());
//			List<List<content>> items = List.of(conr.findAll());
			List<content> item = new ArrayList<>();
			item = conr.findAll();
			model.addAttribute("customer", obj);
			model.addAttribute("items", item);
//			System.out.println("ITEMS => "+item.get(0).getMobile());)
			return "dashboard";
		}else {
			model.addAttribute("msg", "exists");
			String msg = "exists";
			return "redirect:/login/"+msg;
		}
		
	}
	
	@PostMapping("/dashboard/{email}")
	public String finditem(@ModelAttribute("content") content con, Model model, @PathVariable("email") String email) {
		if(email.equals("admin@email.com")) {
			List<content> item = new ArrayList<>();
			item = conr.findByTshirt(con.getTshirt());
			model.addAttribute("items", item);
			content ob = new content();
			model.addAttribute("content", ob);
			return "admin";
		}
		List<content> ob = new ArrayList<>();
		ob = conr.findByTshirt(con.getTshirt());
		account obj = new account();
		obj = repo.findByEmail(email);
		model.addAttribute("customer", obj);
		model.addAttribute("items", ob);
		return "dashboard";
	}
	
	@GetMapping("/dashboard/{email}/{gender}")
	public String finditem(Model model, @PathVariable("email") String email,@PathVariable("gender") String gender) {
		if(email.equals("admin@email.com")) {
			List<content> item = new ArrayList<>();
			item = conr.findByGender(gender);
			model.addAttribute("items", item);
			content ob = new content();
			model.addAttribute("content", ob);
			return "admin";
		}
		List<content> ob = new ArrayList<>();
		ob = conr.findByGender(gender);
		account obj = new account();
		obj = repo.findByEmail(email);
		model.addAttribute("customer", obj);
		model.addAttribute("items", ob);
		content con = new content();
		model.addAttribute("content", con);
		return "dashboard";
	}
	
	@GetMapping("/dashboard/{email}/discount/{discount}")
	public String finditemd(Model model, @PathVariable("email") String email,@PathVariable("discount") String discount) {
		if(email.equals("admin@email.com")) {
			List<content> item = new ArrayList<>();
			item = conr.findByDiscount(discount);
			model.addAttribute("items", item);
			content ob = new content();
			model.addAttribute("content", ob);
			return "admin";
		}
		List<content> ob = new ArrayList<>();
		ob = conr.findByDiscount(discount);
		account obj = new account();
		obj = repo.findByEmail(email);
		model.addAttribute("customer", obj);
		model.addAttribute("items", ob);
		content con = new content();
		model.addAttribute("content", con);
		return "dashboard";
	}
	
	@GetMapping("/dashboard/{email}/discount/{discount}/{gender}")
	public String finditemdg(Model model, @PathVariable("email") String email,@PathVariable("discount") String discount,@PathVariable("gender") String gender) {
		if(email.equals("admin@email.com")) {
			List<content> item = new ArrayList<>();
			item = conr.findByDiscount(discount);
			model.addAttribute("items", item);
			content ob = new content();
			model.addAttribute("content", ob);
			return "admin";
		}
		List<content> ob = new ArrayList<>();
		ob = conr.findByDiscountAndGender(discount,gender);
		account obj = new account();
		obj = repo.findByEmail(email);
		model.addAttribute("customer", obj);
		model.addAttribute("items", ob);
		content con = new content();
		model.addAttribute("content", con);
		return "dashboard";
	}
	
	@GetMapping("/buy/{email}/{id}")
	public String buy(@PathVariable("email") String email , @PathVariable("id") String id, Model model) {
		account ob = repo.findByEmail(email);
		model.addAttribute("customer",ob);
		content obj = conr.getById(id);
		model.addAttribute("item", obj);
		orders ord = new orders();
		model.addAttribute("orders", ord);
		return "buy";
	}
	
	@PostMapping("/dashboard/buy/placed/{itemid}/{email}")
	public String placed(@ModelAttribute("orders") orders order,Model model,@PathVariable("itemid") String itemid,@PathVariable("email") String email) {
		content ob = conr.getById(itemid);
		account ac = repo.findByEmail(email);
		model.addAttribute("customer", ac);
		orders obj = new orders();
		obj.setId(order.getId());
		obj.setName(order.getName());
		obj.setDilevaryAddress(order.getDilevaryAddress());
		obj.setProductName(ob.getTshirt());
		obj.setProductPrice(ob.getPrice());
		obj.setEmail(email);
		obj.setProductId(ob.getId());
		obj.setPhone(order.getPhone());
		orr.save(obj);
		model.addAttribute("order",obj);
		model.addAttribute("msg","Order Placed Successfully ... Thank You 😊");
		return "placed";
	}
	
	@GetMapping("/dashboard/{email}")
	public String dashb(@PathVariable("email") String email,Model model) {
		List<content> ob = new ArrayList<>();
		ob = conr.findAll();
		account obj = repo.findByEmail(email);
		model.addAttribute("customer", obj);
		model.addAttribute("items", ob);
		content con = new content();
		model.addAttribute("content", con);
		return "dashboard";
	}
	
	@GetMapping("/dashboard/orders/{email}")
	public String orderlist(@PathVariable("email") String email,Model model) {
		account ac = repo.getById(email);
		model.addAttribute("customer", ac);
		List<orders> ord = orr.findByEmail(email);
		model.addAttribute("ordered", ord);
		model.addAttribute("msg", "");
		return "orderlist";
	}
	
	@GetMapping("/dashboard/orders/proceed/{id}/{email}")
	public String cancelorder(@PathVariable("id") long id,@PathVariable("email") String email,Model model) {
		if(orr.findById(id) != null)
			orr.deleteById(id);
		List<orders> ord = new ArrayList<>();
		ord = orr.findByEmail(email);
		model.addAttribute("ordered", ord);
		account ac = repo.getById(email);
		model.addAttribute("customer", ac);
		model.addAttribute("msg", "cancel");
		return "orderlist";
	}
	
	@PostMapping("/admin")
	public String admin_page(@ModelAttribute("content") content ob) {	
		conr.save(ob);
		return "admin";
	}
	
	@GetMapping("/admin/order/admin@email.com")
	public String adminorder(Model model) {
		List<orders> ord = new ArrayList<>();
		ord = orr.findAll();
		model.addAttribute("ordered", ord);
		//System.out.println("ordered");)
		model.addAttribute("msg", "");
		return "ordered";
	}
	
	@GetMapping("/admin/orders/proceed/{id}")
	public String removeorder(@PathVariable("id") long id,Model model) {
		if(orr.findById(id) != null)
			orr.deleteById(id);
		List<orders> ord = new ArrayList<>();
		ord = orr.findAll();
		model.addAttribute("ordered", ord);
		model.addAttribute("msg","removed");
		return "ordered";
	}
	
	@GetMapping("/admin/dashboard")
	public String adash(Model model) {
		List<content> item = new ArrayList<>();
		item = conr.findAll();
		model.addAttribute("items", item);
		content ob = new content();
		model.addAttribute("content", ob);
		return "admin";
	} 
	
	@GetMapping("/admin/add/admin@email.com")
	public String additem(Model model) {
		content ob = new content();
		model.addAttribute("content", ob);
		return "additem";
	}
}
