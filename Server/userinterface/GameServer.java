/**
*The server that's hosting all the traffic and accepts connections to be redirected to their own dedicated thread
*@author Team Tough Times
*@version 04/17/2020
*/
package userinterface;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer 
{

   Vector<Object> mainCharacters = new Vector<Object>();
   
   final int portNum = 16790;
   public static void main(String[] args) 
   {
      new GameServer();
   }
   
   public GameServer() 
   {  
      try 
      { 
         //Creates ChatAndGameServerSocket 
         ServerSocket ss = new ServerSocket(portNum);
         System.out.println("Packet Jump Game server is open on port "+portNum);  
         //Creates thread to process player
         while (true) 
         {
            Socket cs = ss.accept();
            System.out.println("New player: " + cs);
            //Create a thread to process the player
            new GameThread(this,cs).start(); //with a order to push main characters immediately
         }
      } 
      catch (Exception e) 
      {
         
      }
   }//End ChatServer constructor
   public void addPacket(Object mc){
      mainCharacters.add(mc);
   }
   public void removePacket(Object mc){
      try{
         mainCharacters.remove(mainCharacters.indexOf(mc));
      }
      catch(Exception e){
         //If we can't find the index, then it's already gone.
      }
   }
      
      
      
   }//End ChatServer class