package com.aritra.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrederRepo extends JpaRepository<orders,Long>{
	public List<orders> findByEmail(String email);
}
