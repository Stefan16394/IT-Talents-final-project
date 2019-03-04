package com.vmzone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Favourite;
import com.vmzone.demo.models.User;


@Repository
public interface FavouritesRepository extends JpaRepository<Favourite, Long> {
	Favourite findById(long id);
}