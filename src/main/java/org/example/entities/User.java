package org.example.entities;

public class User {
    private String Message;
    private String Name;

    
    public User() {
        this("", "");
    }  


    public User(String name, String message) {
        setName(name);
        setMessage(message);
      
       
  }  

   public void setMessage( String Msg )    
   {  Message = Msg;
   }  

   public String getMessage()  
   {  return Message; 
   }  

   public void setName( String name )  
   {  Name = name;
   }  

   public String getName()  // 
   {  return Name; 
   }  

   
}



