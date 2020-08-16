/**
 * This class is used to multithread clients, and give them their own dedicated connection with  the  server. It is managed by ChatServer.
 * @author Team Tough Times
 * @version 4-27-20
 */
package userinterface;

import java.io.*;
import java.net.*;
import java.util.*;

class GameThread extends Thread 
   {
      private Socket cs; 
      private Object theirPacket;
      private GameServer serv;
      private ObjectOutputStream oos;
      private ObjectInputStream ooi;
      private boolean packAdded = false;
      private boolean sent = false;
      
      public GameThread(GameServer serv, Socket cs) 
      {
         this.serv = serv;
         this.cs = cs;
        
      }
      
      public boolean noCharactersToDraw(){
         return serv.mainCharacters.isEmpty();
      }
      
      public void joinedChat(){            
         try{
            oos.writeObject(new Object()); // to avoid both reading at the same time and program hanging
            if(noCharactersToDraw()){
               theirPacket = ooi.readObject();
               serv.addPacket( theirPacket );
               packAdded = true;
            }
            else{
               //push the characters
               for(Object mcs : serv.mainCharacters){
                     oos.writeObject(mcs);
               }
               theirPacket = ooi.readObject();
               serv.addPacket(theirPacket);
               packAdded = true;
               //pull characters to update mainChar list in chatserver
            }
          } catch(Exception e){
               if(packAdded){
                  serv.removePacket(theirPacket);
               }
            }
      }
      
     
      public void run() 
      {
         try 
         {   
            
            oos = new ObjectOutputStream(cs.getOutputStream());
            
            ooi = new ObjectInputStream(cs.getInputStream());

            joinedChat();

            while (true) 
            {
            
               for(Object mcs : serv.mainCharacters){
                  if(mcs != theirPacket && !sent) oos.writeObject(mcs);
                  sent = true;
               }
               
            }
 
         } 
         catch (Exception e) 
         {
            if(packAdded){
               serv.removePacket(theirPacket);
            }
         }//End Catch
      }
   }//End of ClientThread class
