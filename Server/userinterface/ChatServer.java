/**
*The server that's hosting all the traffic and accepts connections to be redirected to their own dedicated thread
*@author Team Tough Times
*@version 04/17/2020
*/
package userinterface;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer
{
   Vector<PrintWriter> clients = new Vector<PrintWriter>();
   
   int highscore = 0;
   final int portNum = 16789;
   public static void main(String[] args) 
   {
      new ChatServer();
   }
   
   public ChatServer() 
   {  
      try 
      { 
         //Creates ChatServerSocket 
         ServerSocket ss = new ServerSocket(portNum);
         System.out.println("Packet Jump server is open on port "+portNum);  
         //Creates thread to process player
         while (true) 
         {
            
            Socket cs = ss.accept();
            System.out.println("New player: " + cs);
            //Create a thread to process the player
            new ClientThread(this,cs).start();
         }
      } 
      catch (IOException e) 
      {
         e.printStackTrace();
      }
   }//End ChatServer constructor
      
      
      
   }//End ChatServer class