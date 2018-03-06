package test;

// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
 
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 


// Server class
public class Server 
{
 
    
    
    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    static String getname(DataInputStream din,DataOutputStream dout)
    {
        String name=null;
        try {
            dout.writeUTF("What is your name? \n");
            name = din.readUTF();
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }
    // counter for clients
    static int i = 0;
 
    public static void main(String[] args) throws IOException 
    {
         
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);
         InetAddress inetAddress = InetAddress.getLocalHost();
        Socket s;
        
        
        ssendMesssage ssend = new ssendMesssage();
        Thread ts = new Thread(ssend);
        ts.start();
        
        // running infinite loop for getting
        // client request
        while (true) 
        {
            // Accept the incoming request
            s = ss.accept();
            System.out.println(inetAddress);
            System.out.println("New client request received : " + s.getPort());
             
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            String name = getname(dis,dos); 
            System.out.println("Creating a new handler for this client...");
            
            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s,name, dis, dos);
 
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
             
            System.out.println("Adding this client to active client list");
 
            // add this client to active clients list
            ar.add(mtch);
 
            // start the thread.
            t.start();
 
            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;
 
        }
    
        
        
    
    }
}
 
class ssendMesssage implements Runnable
{
    Scanner scn = new Scanner(System.in);
    
//    Thread sendMessage = new Thread(new Runnable() 
//            {
                @Override
                public void run() {
                    while (true) {

                        // read the message to deliver.
                        String msg =scn.nextLine();
                        String msg1="Server pressed "+msg;

                        try {
                            // write on the output stream
                            for (ClientHandler mc : Server.ar) 
                    {
                        // if the recipient is found, write on its
                        // output stream
                        /*mc.name.equals(recipient) && */
                        if (mc.isloggedin==true) 
                        {
                            mc.dos.writeUTF(/*this.name+" : "+*/msg1);
                            //break;
                        }
                    }
    //                        if (msg.equals("logout")) {
    //                            System.out.println("lol");
    //                            
    //                            s.close();
    //                            break;
    //                        }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
//            });
}

// ClientHandler class
class ClientHandler implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientHandler(Socket s, String name,
                            DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        String received;
        while (true) 
        {
            try
            {
                // receive the string
                received = dis.readUTF();
                
                System.out.println(name+" pressed "+received);
                String received1= received+"#"+this.name; 
                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
                 
                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received1, "#");
                String MsgToSend = this.name + " pressed " +st.nextToken();
                String recipient = st.nextToken();
 
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                for (ClientHandler mc : Server.ar) 
                {
                    // if the recipient is found, write on its
                    // output stream
                    /*mc.name.equals(recipient) && */
                    if (mc.isloggedin==true) 
                    {
                        mc.dos.writeUTF(/*this.name+" : "+*/MsgToSend);
                        //break;
                    }
                }
//                Enumeration en = Server.ar.elements();
//                while(en.hasMoreElements())
//                {
//                    en.nextElement()
//                }
            } catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
        try {
            // closing resources
            this.dis.close();this.dos.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            
             
        
    }
}