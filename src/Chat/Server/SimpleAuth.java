package Chat.Server;

import sun.font.CreatedFontTracker;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuth implements Autherization{

    private class UserData {
        String login;
        String password;
        String nick;

        public UserData (String login, String password, String nick) {
            this.login = login;
            this.password=password;
            this.nick=nick;
        }

    }
private List<UserData> users;

    public SimpleAuth() {
        users = new ArrayList<>();
        for (int i = 1; i <10 ; i++) {
            users.add(new UserData("qwe"+i, "qwe"+i, "qwe"+i));
        }
    }

    @Override
    public String getNick(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)){
                return user.nick;
            }
        } return null;
    }
}
