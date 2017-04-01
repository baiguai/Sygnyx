import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.Object;
import java.lang.Object.*;
import javax.swing.Timer;
import org.jibble.pircbot.*;

/*
    IRC CLASS
    ----------------------------------------------------------------------------
    Handles the basic IRC functions.
    Contains the timer for random actions.
    ----------------------------------------------------------------------------
*/
public class Irc
{
    // Properties.
    private String server = "127.0.0.1";
    private String nick = "Sygnyx";
    private ArrayList<String> channels;
    private String nickPassword = "warbeiter";
    private String adminKey = "probot";
    private int idleMinutes = 60;

    // Constructor.
    public Irc()
    {
        Server(Functions.GetSetting("SERVER", Server()));
        Nick(Functions.GetSetting("NICK", Nick()));
        AdminKey(Functions.GetSetting("ADMINKEY", AdminKey()));

        String chans = Functions.GetSetting("CHANNELS", "");
        String[] arrChan = chans.split(",");
        for (String ch : arrChan)
        {
            Channels().add(ch);
        }

        if (Channels().size() < 1)
        {
            Channels().add("#dsm");
        }

        try
        {
            // Now start our bot up.
            Bot bot = new Bot(Nick());

            bot.AdminKey = AdminKey();
            
            // Enable debugging output.
            bot.setVerbose(true);
            
            // Connect to the IRC server.
            bot.connect(Server());

            // Join the #pircbot channel.
            for (String ch : Channels())
            {
                bot.joinChannel(ch);
            }

            // Timer for all timed events
            idleMinutes = Integer.parseInt(Functions.GetSetting("IDLEMINUTES", "60"));
            int dly = idleMinutes*60000;
            Timer timer = new Timer(dly, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent arg0)
                {
                    if (!bot.ActiveChannel())
                    {
                        for (String ch : Channels())
                        {
                            bot.RandomIdleEvent(ch);
                        }
                    }

                    bot.ActiveChannel(false);
                }
            });
            timer.setRepeats(true);
            timer.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };



    /*    
        ------------------------------------------------------------------------
        Property GET/SET.
    */
    public void Server(String value) { server = value; }
    public String Server() { return server; }

    public void Nick(String value) { nick = value; }
    public String Nick() { return nick; }

    public ArrayList<String> Channels()
    {
        if (channels == null) channels = new ArrayList<String>();
        return channels;
    }

    public void NickPassword(String value) { nickPassword = value; }
    public String NickPassword() { return nickPassword; }

    public void AdminKey(String value) { adminKey = value; }
    public String AdminKey() { return adminKey; }
};
