package com.github.xuan118s.springboot_angularjs;
import java.util.Date;

public class Order {


	    public int id;
	    public String name;
	    public String messages;
	    public Date create_date; 
	    
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
		public String getMessage() {
			return messages;
		}
		public void setMessage(String messages) {
			this.messages = messages;
		}
		
		public Date getcreate_date() {
			return create_date;
		}
		public void setcreate_date(Date create_date) {
			this.create_date = create_date;
		}
		
		@Override
		public String toString() {
			return "Customer [id=" + id + ", name=" + name + ", message=" + messages + ", createdata=" + create_date +"]";
		}
	    
	

}
