package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Photo;


@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

   Photo findById(long id);
   @Query(value = "SELECT * FROM photos WHERE product_id = :id",nativeQuery=true)
   List<Photo> findPhotosForProductById(@Param("id") long id);
}
