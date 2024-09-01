package com.example.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Seller.java
@Entity
@Table(name = "seller")
public class Seller {

@Id
@Column(name = "Seller_Id")
private String SellerId;
@Column(name = "Company_Name")
private String CompanyName;
@Column(name = "Contact_No")
private String ContactNo;
@Column(name = "Email_Id")
private String EmailId;
private String Address;
@Column(name = "Msme_verified")
private boolean MsmeVerified;
@Column(name = "Msme_Registration_No")
private String MSMERegistrationNo;
private String GSTIN;
public Seller() {
	super();
	// TODO Auto-generated constructor stub
}
public String getSellerId() {
	return SellerId;
}
public void setSellerId(String SellerId) {
	this.SellerId = SellerId;
}
public String getCompanyName() {
	return CompanyName;
}
public void setCompanyName(String CompanyName) {
	this.CompanyName = CompanyName;
}
public String getContactNo() {
	return ContactNo;
}
public void setContactNo(String ContactNo) {
	this.ContactNo = ContactNo;
}
public String getEmailId() {
	return EmailId;
}
public void setEmailId(String EmailId) {
	this.EmailId = EmailId;
}
public String getAddress() {
	return Address;
}
public void setAddress(String Address) {
	this.Address = Address;
}
public boolean isMsmeVerified() {
	return MsmeVerified;
}
public void setMsmeVerified(boolean MsmeVerified) {
	this.MsmeVerified = MsmeVerified;
}
public String getMSMERegistrationNo() {
	return MSMERegistrationNo;
}
public void setMSMERegistrationNo(String MSMERegistrationNo) {
	this.MSMERegistrationNo = MSMERegistrationNo;
}
public String getGSTIN() {
	return GSTIN;
}
public void setGSTIN(String GSTIN) {
	this.GSTIN = GSTIN;
}


}