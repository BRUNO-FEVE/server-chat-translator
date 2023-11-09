package org.example.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.example.entities.User;

public class Read_txt_file {

    private Scanner input;

    public void openFile()
   {  try
      {  input = new Scanner( new File( "Mensagens.txt" ) );
      }  
      catch ( FileNotFoundException fileNotFoundException )
      {  System.err.println( "Error opening file." );
         System.exit( 1 );
      }  
   }  

    public void readFile (User user) {
        try // read records from file using Scanner object
        {
                user.setName(input.nextLine());
                user.setMessage(input.nextLine());
                
            
        } 
        catch (NoSuchElementException elementException) {
            System.err.println("File improperly formed.");
            input.close();
            System.exit(1);
        } 
        catch (IllegalStateException stateException) {
            System.err.println("Error reading from file.");
            System.exit(1);
        } 
    } 
    

    public void closeFile()
   {  if( input != null )
         input.close(); 
   }  
}