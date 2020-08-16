package userinterface;
public class ChatThread extends Thread{
   private ChatClient pc;
   public ChatThread(ChatClient pC){pc=pC;}
   
   public void run(){
      pc.start();
   }
   public ChatClient getChat(){
      return pc;
   }
   public boolean isNotReady(){
      return pc.isNotReady();
   }
 } 