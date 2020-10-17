package Chat.Server;

public interface Autherization {
    String getNick (String login, String password);
    boolean registr (String login, String password, String nick);
}
