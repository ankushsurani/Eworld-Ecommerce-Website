package com.eworld.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Size(min = 3, max = 50)
	private String name;
	
	@Size(min = 10, max = 10)
	private String mobilenum;
	
	@NotBlank
	private String pincode;
	
	@Size(min = 5, max = 500)
	private String fullAddress;
	
	@NotBlank
	private String state;
	
	@NotBlank
	private String city;
	
	@NotBlank
	private String addressType;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	public Address() {
		super();
	}

	public Address(int id, String name, String mobilenum, String pincode, String fullAddress, String state, String city,
			String addressType, User user) {
		super();
		this.id = id;
		this.name = name;
		this.mobilenum = mobilenum;
		this.pincode = pincode;
		this.fullAddress = fullAddress;
		this.state = state;
		this.city = city;
		this.addressType = addressType;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobilenum() {
		return mobilenum;
	}

	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}