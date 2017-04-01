import java.io.*;
import java.sql.*;
import java.util.*;

/*
    USER
    ----------------------------------------------------------------------------
    Handles user accounts and authentication.
    ----------------------------------------------------------------------------
*/
public class User
{
    // Properties.
    public Connection CONNUser;
    private String dbPath = "Data/UserDB";
    private ArrayList<AuthedUser> authedUsers;


    // Constructor
    public User()
    {
        SetConn();
    };

    // Process Messages
    public ArrayList<String> ProcessMessage(String login, String hostname, String cmd)
    {
        ArrayList<String> output = new ArrayList<String>();
        String[] arr = cmd.split(" ");
        boolean matched = false;

        if (output.size() < 1)
        {
            if (!matched && Functions.Match(cmd, "ident"))
            {
                matched = true;
                if (arr.length != 2)
                {
                    output.add("To login, send me a private message: user ident [password]" + hostname);
                }
                else
                {
                    boolean authed = AuthorizeUser(login, hostname, arr[1]);

                    if (authed)
                    {
                        output.add("You have been successfully identified. Thanks.");
                    }
                    else
                    {
                        output.add("I could not find your user account.");
                    }
                }
            }

            if (!matched && Functions.Match(cmd, "register"))
            {
                matched = true;
                if (arr.length != 2)
                {
                    output.add("To register, send me a private message: register [password]");
                }
                else
                {
                    String pass = arr[1];
                    RegisterUser(pass);
                    output.add("You have been registered. Don't be dumb and forget your password.");
                }
            }
        }

        return output;
    };


    // Database Methods
    private void CreateDatabase()
    {
        try
        {
            Statement cmd = CONNUser.createStatement();
            String sql = "";

            sql = "";
            sql += "CREATE TABLE BotUser ( ";
            sql += "    Password VARCHAR(300) NOT NULL, ";
            sql += "    UserIP VARCHAR(300), ";
            sql += "    UserLevel BIGINT DEFAULT 2, ";
            sql += "    Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };


    // Helper Methods
    public boolean AuthorizeUser(String login, String hostname, String password)
    {
        boolean found = false;

        try
        {
            Statement cmd = CONNUser.createStatement();
            String sql = Sql_User.AuthorizeUser(password);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                int id = rs.getInt("UserID");
                String ip = login + "." + hostname;
                int lvl = rs.getInt("UserLevel");

                AuthedUser u = new AuthedUser(id, ip, lvl);

                // Clean out any existing entries for the user
                for (AuthedUser row : AuthedUsers())
                {
                    if (row.UserID() == id)
                    {
                        AuthedUsers().remove(row);
                    }
                }

                AuthedUsers().add(u);
                found = true;
            }

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return found;
    };

    public void RegisterUser(String password)
    {
        try
        {
            Statement cmd = CONNUser.createStatement();
            String sql = Sql_User.Register(password, 1);

            cmd.execute(sql);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };

    public boolean IsAuthed(String userIPIn)
    {
        boolean isAuthed = false;

        for (AuthedUser u : AuthedUsers())
        {
            if (u.UserIP() == userIPIn)
            {
                isAuthed = true;
                break;
            }
        }

        return isAuthed;
    };

    // Clear out stale logins
    public void ClearAuth()
    {
        for (AuthedUser u : AuthedUsers())
        {
            if (u.IsStale())
            {
                AuthedUsers().remove(u);
            }
            else
            {
                u.IsStale(true);
            }
        }
    };

    // -------------------------------------------------------------------------
    // Property GET/SET
    public void SetConn()
    {
        boolean newDB = false;

        if (CONNUser == null)
        {
            try
            {
                if (!(new File(dbPath).exists()))
                    newDB = true;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            try
            {
                Class.forName("org.sqlite.JDBC");
                CONNUser = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if (newDB)
        {
            System.out.println("Creating new user database...");
            CreateDatabase();
        }
    }

    public ArrayList<AuthedUser> AuthedUsers()
    {
        if (authedUsers == null) authedUsers = new ArrayList<AuthedUser>();
        return authedUsers;
    }
};
