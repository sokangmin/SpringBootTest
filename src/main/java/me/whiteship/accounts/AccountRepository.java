package me.whiteship.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByUsername(String username);

	@Query("SELECT x FROM Account x ORDER BY x.username")
	List<Account> findAllOrderByName();
}
