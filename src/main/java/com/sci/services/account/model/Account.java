package com.sci.services.account.model;


public class Account {

	private String id;
	private String number;
	private String customerId;
	private Double amount;

	public Account() {

	}
	
	public Account(String number, String customerId, Double amount) {
		this.number = number;
		this.customerId = customerId;
		this.amount = amount;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", number=" + number + ", customerId=" + customerId + ", amount=" + amount + "]";
	}

}
