package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.AddressDao;
import com.alpha.qspiderrestapi.entity.Address;
import com.alpha.qspiderrestapi.repository.AddressRepository;

@Repository
public class AddressDaoImpl implements AddressDao {

	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Address saveAddress(Address address) {
		return addressRepository.save(address);
	}

	@Override
	public Optional<Address> fetchAddressById(long addressId) {
		return addressRepository.findById(addressId);
	}

	@Override
	public List<Address> fetchAllAddresses() {
		return addressRepository.findAll();
	}

	@Override
	public void deleteAddress(long addressId) {
		addressRepository.deleteById(addressId);

	}

	@Override
	public long isAddressPresent(long addressId) {
		return addressRepository.findByAddressId(addressId);
	}

}
