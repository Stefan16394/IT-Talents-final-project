package com.vmzone.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vmzone.demo.models.Characteristic;

@Repository
public interface CharacteristicsRepository extends JpaRepository<Characteristic, Long>{
	
	@Query(value = "select * from characteristics where product_id = :prodId and value = :value", nativeQuery = true)
	Characteristic findNameOfCharacteristicForProduct(@Param("prodId") long prodId, @Param("value") String value);
	
	@Query(value = "select * from characteristics where product_id = :prodId and characteristics_id = :charactId", nativeQuery = true)
	Characteristic findCharacteristicForProduct(@Param("prodId") long prodId, @Param("charactId") long charactId);
	
	@Query(value = "select * from characteristics where name = 'colour'", nativeQuery = true)
	List<Characteristic> findCharacteristicWithColour();
	
	@Query(value = "select * from characteristics where name = 'colour' and value = :value", nativeQuery = true)
	List<Characteristic> findCharacteristicWithColourAndValue(@Param("value") String value);
	
	@Query(value = "select * from characteristics where name = 'size' and value = :value", nativeQuery = true)
	List<Characteristic> findCharacteristicWithSizeAndValue(@Param("value") String value);

}
