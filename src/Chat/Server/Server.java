package Chat.Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private List<ClientManager> clients;
    private Autherization auth;
public Server (){
    clients = new CopyOnWriteArrayList<>();
    auth = new SimpleAuth();
    ServerSocket server = null;
    Socket socket = null;
    final int Port = 8189;

    try {
        server = new ServerSocket(Port);
         System.out.println("Server is on");


        while (true) {
            socket = server.accept();
            System.out.println("Client is connected");
            new ClientManager(this, socket);
        }

    } catch (IOException e){
        e.printStackTrace();
    } finally {
        try {
            socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
public void castMess (ClientManager sender, ClientManager reciever, String message){
    if (reciever !=null){
        String [] words = message.split("\\s");
        message = message.substring(words[0].toCharArray().length + words[1].toCharArray().length+1);
        String mess = String.format("[ %s - private to %s ]: %s", sender.getNickname(), reciever.getNickname(), message);
        reciever.sentMessage(mess);
        sender.sentMessage(mess);
    } else {
        String mess = String.format("[ %s ]: %s", sender.getNickname(), message);
        for (ClientManager c : clients) {
            c.sentMessage(mess);
        }
    }
}
public void subscribe (ClientManager client) {
    clients.add(client);
}
public void unsubscribe (ClientManager client) {
        clients.remove(client);
    }

    public Autherization getAuth() {
        return auth;
    }
    public ClientManager getClient (String nick){
        for (ClientManager c : clients) {
            if (c.getNickname().equals(nick)) {
                return c;
            }
        } return null;
    }
}
