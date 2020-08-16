/**
* The ChatClient which the user will get which he can use to access multiplayer
*@author Team Tough Times
*@version 04/17/2020
*/
package userinterface;

import util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import gameobject.*;

public class ChatClient extends JFrame implements ActionListener,WindowListener
{ 
   //Atributes
   private Socket s; 
   private JTextArea jtaChatroom;
   private JPanel    jpChat;
   private JPanel    jpInput;
   private JTextArea jtaInput;
   private JButton   jbSend;
   private JPanel    menu;
   private JMenuBar  menubar;
   private JMenu     file;
   private JMenuItem exit;
   private PrintWriter pw;
   private BufferedReader br;
   private boolean singlePlayer=false; // Changes services based on the status of this boolean 
   private int personalHigh = 0;
   private String ipDestination;
   private String displayName;
   private OutputStream out;
   private InputStream in;
   
   public void start(){
   Scanner in = new Scanner(System.in);
      System.out.println("Welcome to Packet Jump! Avoid Network congestion, DDOS attacks, firewalls... and more!");
      System.out.println("Controls: \nSpacebar: Start, Restart Game, and Jump\nDown Arrow: Ducking\n");
              //For Multiplayer Mode
      System.out.print("Please enter a destination IP Address to access multiplayer (leave blank if playing singleplayer): ");
      ipDestination = in.nextLine();
      if(ipDestination.equals("")||ipDestination==null) {
         displayName = "";
         System.out.println("You have been placed in single player mode");
         singlePlayer = true;
      }
      else{
         System.out.print("Please enter a display name to enter the chat: ");
   
         //Display the name of the client that just joined 
         displayName = in.nextLine();
         if(displayName == null||(displayName.replace(" ", "")).equals(""))
         {
            displayName = "Anonymous";
         }
         System.out.println("Your name has been set to "+displayName);
      }
      try 
      {
         //Create GUI for this client 
         setTitle("Packet Jump Global Chat");
         setSize(300,300);
         setLocationRelativeTo(null);
         
         menu = new JPanel();
         menubar = new JMenuBar();
         file = new JMenu("File");
         exit = new JMenuItem("Exit");
         menubar.add(file);
         file.add(exit);
         menu.add(menubar);
         add(menu, BorderLayout.SOUTH);
         
         jpChat = new JPanel();
         jtaChatroom = new JTextArea("", 30, 40);
         jtaChatroom.setEnabled(false);
         jtaChatroom.setLineWrap(true);
         jtaChatroom.setWrapStyleWord(true);
         JScrollPane jspChatroom = new JScrollPane(jtaChatroom);
         jpChat.add(jspChatroom);
         
         add(jpChat, BorderLayout.CENTER);
         
         jpInput = new JPanel();
         jtaInput = new JTextArea("", 2, 20);
         jbSend = new JButton("Send");
         jbSend.addActionListener(this);
         addWindowListener(this);
         jbSend.setMnemonic(KeyEvent.VK_ENTER);
         
         jpInput.add(jtaInput);
         jpInput.add(jbSend);
         
         add(jpInput, BorderLayout.SOUTH);
          
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         pack();
         
         //If singleplayer, don't bother to connect to the chat
         if(singlePlayer == false) {
            execute();
         }
         
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }
      
   
   }
   
   public boolean isNotReady(){
         if(displayName == null || ipDestination == null){
            return true;
         }
         return false;
   }
   public boolean isMultiplayer(){
      return !singlePlayer;
   }
   //Executes when the maincharacter dies
   public void youDied(int score){
      String deathMessage = "\nYou died with a score of "+score+". Press Space to play again.";
      jtaChatroom.append(deathMessage);
      System.out.println(deathMessage);
   }
   
   public void execute()
   { 
      try
      {        
            
            //Create socket 
            s = new Socket(ipDestination, 16789 );
            
            out = s.getOutputStream();
            in = s.getInputStream();
            
            
            //Reading from the server
            br = new BufferedReader (
                  new InputStreamReader( in ) );
           //Writing to server
            pw = new PrintWriter(
                 new OutputStreamWriter( out ));
            
            pw.println(displayName);
            pw.flush();
            
            
            //By this point, verify the state of the game before making chat visible
            if(singlePlayer == false){
               setVisible(true);
            }
            
            try{
               while(true)
               {
                  //Update chatroom text area
                  String message = br.readLine();
                  append( message + "\n" );
                  
               }
           } catch(IOException ioe){ 
               System.out.println("Thanks for playing Packet Jump!"); 
             }
        }    
      
      catch(ConnectException ce) // If they cannot connect, launch a message and put the game into single player mode
      {
         String message;
         if(ipDestination.equals("localhost")){
            message = "You forgot to turn on your server...you have been placed in single player mode.";
         }
         else{
            message = "Attempt to connect to a World Wide Web IP failed, you have been placed in single player mode, that server router probably need's port forwarding. ";
         }
         System.out.println(message);
         jtaChatroom.append(message+"\n");
         singlePlayer = true;
      }
      catch(UnknownHostException uhe){
         String message = "Incorrect IP address, you have been placed in single player mode.";
         System.out.println(message);
         jtaChatroom.append(message+"\n");
         singlePlayer = true;
      }
      catch(SocketException se){
         String message = "Incorrect IP address, you have been placed in single player mode.";
         System.out.println(message);
         jtaChatroom.append(message+"\n");
         singlePlayer = true;
      
      }
      catch(IllegalArgumentException iae){
         String message = "Incorrect IP address, you have been placed in single player mode.";
         System.out.println(message);
         singlePlayer = true;
      }
      catch(IOException ioe){
         singlePlayer = true;
         ioe.printStackTrace();
      }
   }   
          
   public void actionPerformed(ActionEvent ae) 
   {
     try{
        String message = jtaInput.getText();
        if(singlePlayer == false){
            if (ae.getSource() == jbSend) 
            { 
               pw.println( message );
               pw.flush();
            }
        }
     } catch(NullPointerException npe){
     
     }
   }
   
   public void sendScore(int score){
   try{
      if(singlePlayer == false){
         pw.println("!*@(*#)@,)(@*)"+score); //Secret key for sending a highscore
         pw.flush();
      }
      else{
         if(score > personalHigh){
            String message = "\nYou have achieved a new personal highscore of "+score+"!!!!!";
            jtaChatroom.append(message);
            System.out.println(message);
            }
      }
   } catch(NullPointerException npe){}
   }
   public void windowActivated(WindowEvent e){}
   public void windowClosed(WindowEvent e){}
   public void windowClosing(WindowEvent e){
      if(singlePlayer == false){
         System.out.println("\nThank you for playing Packet Jump!");
         pw.println("bye");
         pw.flush();
         try{
            br.close();
            pw.close();
            s.close();  
         } catch(IOException ioe){
             ioe.printStackTrace();
         } 
      }
      else{
         System.out.println("\nThank you for playing Packet Jump!");
      }
   }
   
   
   public void windowDeactivated(WindowEvent e){}
   public void windowDeiconified(WindowEvent e){}
   public void windowIconified(WindowEvent e){}
   public void append(String message){
      jtaChatroom.append(message);
   }
   public void windowOpened(WindowEvent e){}
}//end ChatClient class