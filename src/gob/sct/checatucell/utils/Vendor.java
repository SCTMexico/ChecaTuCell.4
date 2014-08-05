package gob.sct.checatucell.utils;

public class Vendor {
	
	String name;
	String description;
	int idVendor;
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	
	public void setVendorId(int idVendor){
		this.idVendor = idVendor;
	}
	public int getVendorId(){
		return idVendor;
	}
}
