package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Address;

public interface AddressDao {

	Address saveAddress(Address address);

	Optional<Address> fetchAddressById(long addressId);

	List<Address> fetchAllAddresses();

	void deleteAddress(long addressId);
	
	long isAddressPresent(long addressId);

	boolean isCityPresent(String cityName);
}
