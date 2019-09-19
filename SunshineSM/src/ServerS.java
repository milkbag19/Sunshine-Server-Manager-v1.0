
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.*;

public class ServerS{

    public static void main(String[] args){
        try  {
            var listener = new ServerSocket(25565);
            System.out.println("Service Started...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Bruh(listener.accept()));
            }
        }catch(Exception e){e.printStackTrace();}
    }

    private static class Bruh implements Runnable{
        private Socket socket;
        private ServerSocket server = null;
        private Scanner in = null;
        private PrintWriter out = null;
        private DataInputStream inputLocal = null;

        private Bruh(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                System.out.println("Connected : "+ socket);
                out = new PrintWriter(socket.getOutputStream(), true);


                in = new Scanner(socket.getInputStream());

                inputLocal = new DataInputStream(System.in);


                String name = in.nextLine();
                out.println("Your name is : "+name);
                out.flush();
                String line = "";
                String outputLine = "";

                while(!line.equals("Over")){
                    try{

                        line = in.nextLine();
                        System.out.println(name +" : "+ line);
                        out.println("Sent to server : "+line);

                    }catch(Exception e){e.printStackTrace();}

                }
                try{
                    socket.close();
                    in.close();

                }catch(Exception e){e.printStackTrace();}



            }catch(Exception e){e.printStackTrace();}
        }
    }

}