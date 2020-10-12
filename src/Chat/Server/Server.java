package Chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
public void castMess (ClientManager sender, String message){
    String mess = String.format("[ %s ]: %s", sender.getNickname(), message);
    for (ClientManager c: clients){
        c.sentMessage(mess);
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
}
