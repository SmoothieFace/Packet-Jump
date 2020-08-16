/**
 * This class is used to multithread clients, and give them their own dedicated connection with  the  server. It is managed by ChatServer.
 * @author Team Tough Times
 * @version 4-27-20
 */
package userinterface;

import java.io.*;
import java.net.*;
import java.util.*;

class ClientThread extends Thread 
   {
      private Socket cs; 
      private PrintWriter pw;
      private BufferedReader br;
      private String clientMsg;
      private String clientName;
      private ChatServer serv;
      
      public ClientThread(ChatServer serv, Socket cs) 
      {
         this.serv = serv;
         this.cs = cs;
        
      }
      public void joinedChat(){
      
         
         synchronized (serv.clients) 
                  {
                     String msg = clientName + " has joined the chatroom...";
                     
                     for (PrintWriter print : serv.clients) 
                     {
                        print.println(msg);
                        print.flush();
                     }
                  }
      
      }
      
      public void broadcastMessage(String message){
      
      
             synchronized (serv.clients) 
                  {
                     String msg = clientName + ": " + message;
                     for (PrintWriter print : serv.clients) 
                     {
                        print.println(msg);
                        print.flush();
                     }
                  }
      
      }
      public void broadcastHighscore(int score){
      
      
             synchronized (serv.clients) 
                  {
                     String msg = ("\n\n"+clientName+" has achieved a new global highscore of "+serv.highscore+"!!!!!\n\n");
                     for (PrintWriter print : serv.clients) 
                     {
                        print.println(msg);
                        print.flush();
                     }
                  }
      
      }
      public void run() 
      {
         try 
         {   
            //Reads client message 
            br = new BufferedReader(
                   new InputStreamReader( cs.getInputStream() ) );
                   
            clientName = br.readLine(); 
            
            //Creates pw for each player                     
            pw = new PrintWriter( 
                  new OutputStreamWriter( cs.getOutputStream() ) );  
            
            serv.clients.add(pw);
            
            joinedChat();
            
            
            
              
            while (true) 
            {
               String clientMsg = br.readLine();
               
               if(clientMsg.replace(" ","").isEmpty()){

               }
               
               else if (clientMsg.equals("bye")) 
               {
                  br.close();
                  pw.close();
                  cs.close();
                  serv.clients.remove(this.pw);
                  broadcastMessage("People in chat: "+serv.clients.size());
                  break;
                  
               }   
               else{                  
                  if(clientMsg.contains("!*@(*#)@,)(@*)")){//special code for a highscore
                     int score = Integer.parseInt(clientMsg.substring(14));
                     if(score > serv.highscore) {
                        serv.highscore = score;
                        broadcastHighscore(serv.highscore);
                     }
                  }
                  else{
                  broadcastMessage(clientMsg);
                  }
               }
            }
            
            //Close all player communication
            pw.close();
            cs.close(); 
            serv.clients.remove(this.pw);
         } 
         catch (IOException e) 
         {
            serv.clients.remove(this.pw);
            System.out.println(" A player left the server. "+serv.clients.size()+" players remain.");
         }//End Catch
      }
   }//End of ClientThread class
