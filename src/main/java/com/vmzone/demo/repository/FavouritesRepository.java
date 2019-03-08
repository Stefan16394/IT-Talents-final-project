package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Favourite;


@Repository
public interface FavouritesRepository extends JpaRepository<Favourite, Long> {
	Favourite findById(long id);
	
	@Query(value = "select * from favourites where favourites_id = :favId and user_id = :id", nativeQuery = true)
	Favourite findFavouriteFoUser(@Param("id") long id, @Param("favId") long favId);
}
