import java.util.*;
import java.net.*;
import java.io.*;

public class ChatServer{
    public static Socket socket;
    public static DataInputStream dis;
    static int i = 1;
    public static DataOutputStream dos;
    public static Vector<ClientHandler> ar = new Vector<>();
    public static String name;
    public static void main(String[] args)throws IOException{


        Thread serverClientSearch = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                //creating the server for all sockets to connect to
                ServerSocket server = new ServerSocket(33676);
                //looping constantly so that the server is always looking for new clients
                for (; ;) {
                    //the server blocks(just sits there) until a new user connects
                    socket = server.accept();
                    //sending out a message in the console informing that a new user successfully connected
                    System.out.println("New client connected!\nConnected from : " + socket.getRemoteSocketAddress());
                    //establishing in and out streams for data
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                    //waits for the client to send in their username
                    name = dis.readUTF();
                    //creating a new instance of our ClientHandler class, which is self explanatory
                    ClientHandler handler = new ClientHandler(socket, name, dis, dos);
                    //creating a new thread so that the server can handle any number of users as once
                    Thread t = new Thread(handler);
                    //adding the new user to our vector array
                    ar.add(handler);
                    //informing the server that the client is added to the client list through the console
                    System.out.println("Adding this client to our list :)");
                    //starting the thread to begin handling of the client
                    t.start();
                    i++;
                }
            }catch(Exception e){}
            }
        });
        serverClientSearch.start();
    }
}
class ClientHandler implements Runnable{
    private Socket socket;
    private String name;
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean isLoggedIn = true;

    public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos){
        //creating variables so that the parameters may be used elsewhere
        this.socket = socket;
        this.name = name;
        this.dos = dos;
        this.dis = dis;

        //sending out the message that a new client has joined to all current users in the char room
        for (ClientHandler mc : ChatServer.ar){
            try {
                mc.dos.writeUTF("> "+name+ " has joined");
            }catch(Exception e){}
        }
    }
    //starting the thread's code
    @Override
    public void run() {
        String received;
        boolean isCommand = false;
        try {
            //this is the welcome message that the server sends to all new clients that join
            dos.writeUTF("Welcome to the chatroom!\nType \"/logout\" to close the chat");
        }catch(Exception e){}
        while(true){
            try{
                isCommand = false;
                //checking if the current threads client is logged in or not
                if(!isLoggedIn){
                    break;
                }
                //reading what the client has sent and storing it into a variable
                received = dis.readUTF();

                StringTokenizer st = new StringTokenizer(received,"#");
                String msg = st.nextToken();
                String clientHandle = st.nextToken();

                String[] brokenMsg = msg.split(" ");

                for (int i = 0;i<ChatServer.ar.size();i++){
                    //System.out.println(clientHandle+" "+ChatServer.ar.get(i).name);
                    if(msg.equals("logout")){
                        ChatServer.ar.get(i).dos.writeUTF("> "+clientHandle+" has logged out");
                        System.out.println("> "+clientHandle+" has logged out");
                        isCommand = true;
                    }
                    else if(brokenMsg[0].toLowerCase().equals("/msg")){
                        try{
                            String recipientHandle = brokenMsg[1];
                            isCommand = true;
                            for(int j = 0;j<ChatServer.ar.size();j++){

                                if(recipientHandle.toLowerCase().equals(ChatServer.ar.get(j).name.toLowerCase())){
                                    String message = "";
                                    for(int k = 2;k<brokenMsg.length;k++)
                                        message+=brokenMsg[k]+" ";
                                    ChatServer.ar.get(j).dos.writeUTF(clientHandle+" > "+"You : "+message);
                                    i=ChatServer.ar.size();
                                }

                            }
                        }catch(Exception e){}
                    }
                    else if(msg.toLowerCase().equals("/list")){
                        isCommand = true;
                        if(clientHandle.equals(ChatServer.ar.get(i).name)) {
                            dos.writeUTF("" + ChatServer.ar.size() + " clients are currently online");
                            for(int j = 0; j<ChatServer.ar.size();j++){
                                dos.writeUTF("- "+ChatServer.ar.get(j).name);
                            }
                        }
                    }
                    else {
                        try {
                            if (!ChatServer.ar.get(i).name.equals(clientHandle)&&!isCommand) {
                                ChatServer.ar.get(i).dos.writeUTF(clientHandle + " : " + msg);
                                System.out.println(clientHandle + " : " + msg);
                            }
                        }catch(Exception e){e.printStackTrace();}
                    }
                }
                for(int i = 0;i<ChatServer.ar.size();i++) {
                    if (clientHandle.equals(ChatServer.ar.get(i).name)&&msg.equals("logout")) {
                        ChatServer.ar.get(i).isLoggedIn = false;
                        ChatServer.ar.get(i).socket.close();
                        ChatServer.ar.remove(ChatServer.ar.get(i));
                    }
                }
            }catch(Exception e){e.printStackTrace();}
        }
    }
}