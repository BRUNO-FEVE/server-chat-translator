package org.example.modules;


import java.io.FileNotFoundException;
import java.lang.SecurityException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.example.entities.User;

public class Create_txt_file
{  private Formatter output;  
   public void openFile()  
   {  try
      {  output = new Formatter( "Mensagens.txt" );
      }  
      catch( SecurityException securityException )
      {  System.err.println( "You do not have write access to this file." );
         System.exit( 1 );
      }  
      catch( FileNotFoundException filesNotFoundException )
      {  System.err.println( "Error creating file." );
         System.exit( 1 );
      } 
   } 

   public void addRecords(User userInput)   
   {  
      User record = new User();
      Scanner sc = new Scanner(System.in);
      
       try // output values to file
         {  
            record.setName( userInput.getName() ); 
            record.setMessage( userInput.getMessage() ); 
            

            output.format("" + record.getName() + " \n" + record.getMessage() );
            
         }
            
         catch ( FormatterClosedException formatterClosedException )
         {  System.err.println( "Error writing to file." );
            return;
         } 
         catch ( NoSuchElementException elementException )
         {  System.err.println( "Invalid input. Please try again." );
            sc.nextLine(); 
         }  
         
   } 

   public void closeFile() 
   {  if ( output != null )
         output.close();
   }  
}  


