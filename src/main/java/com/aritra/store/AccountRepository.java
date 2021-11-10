package com.aritra.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<account,String>{

	public account findByEmailAndPassword(String email , String password);
	public account findByEmail(String email);
	public boolean existsByEmail(String email);
}
