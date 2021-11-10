package com.aritra.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepo extends JpaRepository<content,String>{

	public List<content> findByTshirt(String tshirt);
	public List<content> findByGender(String gender);
	public List<content> findByDiscount(String discount);
	public List<content> findByDiscountAndGender(String discount,String gender);
}

