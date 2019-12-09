package com.rbnr.business; 
import org.json.simple.JSONObject;

public class Validator {
	private JSONObject obj;
	private boolean valid;
	public Validator(){
		this.obj = new JSONObject();
		this.valid = true;
	}	


	public void validateUsername(String username){
		
		if(username == null ){
			this.obj.put("username","Username is required!");
			this.valid = false;
			
		}
		else{ 
			if(!username.matches("[a-zA-Z0-9]{6,25}")){
				this.obj.put("username","Username is not valid!");
				this.valid=false;
			}

		}
		
	}


	public void validatePassword(String password){
		
		if(password == null ){
			this.obj.put("password","Password is required!");
			this.valid = false;
			
		}
		else{ 
			if(password.length()<8){
				this.obj.put("password","Password is too short!");
				this.valid=false;
			}

		}
		
	}

	public void validateFirstname(String firstname){
		
		if(firstname == null ){
			this.obj.put("firstname","Firstname is required!");
			this.valid=false;
		}
		else{ 
			if(!firstname.matches("[a-zA-Z]{2,25}")){
				this.obj.put("firstname","Firstname is not valid!");
				this.valid=false;
			}
		}
		
	}

	public void validateLastname(String lastname){
		
		if(lastname == null ){
			this.obj.put("lastname","Lastname is required!");
			this.valid=false;
		}
		else{ 
			if(!lastname.matches("[a-zA-Z]{2,25}")){
				this.obj.put("lastname","Lastname is not valid!");
				this.valid=false;
			}
		}
		
	}

	public JSONObject getErrors(){
		return this.obj;
	}

	public boolean isValid(){
		return this.valid;
	}


}