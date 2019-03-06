package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmzone.demo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	@Query(value = "SELECT s.product_id,p.information,p.price,s.quantity FROM shopping_cart s\r\n"
			+ "JOIN products p ON p.product_id = s.product_id\r\n" + "WHERE user_id  = :id", nativeQuery = true)
	List<Object> getShoppingCart(@Param("id") long id);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO shopping_cart VALUES(:productId,:quantity,:userId)", nativeQuery = true)
	void addProductToCart(@Param("productId") Long productId, @Param("quantity") int quantity,
			@Param("userId") Long userId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE shopping_cart SET quantity = :quantity WHERE user_id = :userId AND product_id = :productId LIMIT 1", nativeQuery = true)
	void updateProductInCart(@Param("productId") Long productId, @Param("quantity") int quantity,
			@Param("userId") Long userId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM shopping_cart WHERE user_id = :userId AND product_id = :productId LIMIT 1", nativeQuery = true)
	void deleteProductInCart(@Param("productId") Long productId, @Param("userId") Long userId);

}
