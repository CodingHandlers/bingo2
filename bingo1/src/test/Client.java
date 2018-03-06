package test;

import java.io.*;
import java.net.*;
import java.util.Scanner;
 
public class Client 
{
    final static int ServerPort = 1234;
 
    public static void main(String args[]) throws UnknownHostException, IOException 
    {
        Scanner scn = new Scanner(System.in);
         
        // getting localhost ip
        InetAddress ip = InetAddress.getLocalHost();
//         ClientHandler o = new ClientHandler();
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
         
        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
 
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                while (true) {
 
                    // read the message to deliver.
                    String msg = scn.nextLine();
                     
                    try {
                        // write on the output stream
                        dos.writeUTF(msg);
                        if (msg.equals("logout")) {
                            System.out.println("lol");
                            
                            s.close();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         
        // readMessage thread
        Thread readMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                boolean run  = true;
                while (run) {
                    try {
                        // read the message sent to this client
                        
                            String msg = dis.readUTF();
                        System.out.println(msg);
                        
                    } catch (IOException e) {
                        
                        run = false;
                    }
                }
            }
        });
 
        sendMessage.start();
        readMessage.start();
 
    }
}