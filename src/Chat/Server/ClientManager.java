package Chat.Server;

import Chat.Client.Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager {
    DataInputStream in;
    DataOutputStream out;
    Server server;
    Socket socket;

    private String nick;
    private String login;

    private SimpleAuth authUser;

    public ClientManager(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                // authentication step
                while (true) {
                    String mess = in.readUTF();
                    if (mess.startsWith("/auth")){
                        String[] tocken = mess.split("\\s");
                            if (tocken.length <3) {
                                continue;
                            }
                            String newNick = server.getAuth().getNick(tocken[1], tocken[2]);
                            if (newNick != null){
                                nick = newNick;
                                server.subscribe(this);
                                server.castMess(this, null, "*** join chat ***\n");
                                sentMessage("/auth_ok "+ newNick);
                                break;
                           } else {
                                sentMessage("invalid login/ password");
                            }
                    }
                }
                // work step

                    while (true) {
                        String mess = in.readUTF();
                        if (mess.startsWith("/exit")) {
                            break;
                        }
                        ClientManager receiver = null;
                        if (mess.startsWith("/w")){
                            String[] privat = mess.split("\\s");
                            receiver = server.getClient (privat[1]);
                            if (receiver == null){
                                mess = "Invalid nickname or request";
                                ClientManager.this.sentMessage(mess);
                                continue;
                            }
                        }
                        server.castMess(this, receiver, mess);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    server.castMess(this, null, "*** left chat ***\n");
                    System.out.println("Client is disconnected");
                    try {
                        socket.close();
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sentMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getNickname() {
        return nick;
    }

}



