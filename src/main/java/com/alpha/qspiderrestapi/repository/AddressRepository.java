package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query(value = "Select a.addressId From Address a Where a.addressId = :addressId")
	Long findByAddressId(@Param("addressId") long addressId);

}