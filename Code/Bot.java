import org.jibble.pircbot.*;
import java.util.*;
import java.util.Random;

/*
    Bot
    ----------------------------------------------------------------------------
    This is the pircbot object.
    It handles all the pircbot methods.
        2017.02.02 : Norm
            - Added more random methods for use w/ the idle timer
            - Added one method to choose a random method to fire
    ----------------------------------------------------------------------------
*/
public class Bot extends PircBot
{
    // Properties
    public String Nick;
    public String AdminKey;
    private boolean activeChannel = false;

    private final int R_CHAT = 1;
    private final int R_INSLT = 2;
    private final int R_LNK = 3;
    private final int R_QUOT = 4;
    private final int R_TOR = 5;

    private Chess clsChess;
    private User clsUser;
    private Event clsEvent;


    // Constructor
    public Bot(String nick)
    {
        Nick = nick;
        this.setName(nick);
    };


    // Messages into the channel.
    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        boolean handled = false;
        ArrayList<String> output = new ArrayList<String>();

        ActiveChannel(true);

        Message msg = new Message();

        String sOut = msg.ProcessMessage(Nick, sender, message);

        if (!sOut.equals(""))
        {
            handled = true;
            sendMessage(channel, sOut);
        }

        // Handle the modules
        if (!handled)
        {
            // Chess
            if (output.size() < 1)
            {
                output = ClsChess().ProcessMessage(message);
            }

            if (output.size() > 0)
            {
                for (String s : output)
                {
                    sendMessage(channel, s);
                }
            }
        }
    };

    // Private messages to the bot.
    public void onPrivateMessage(String sender, String login, String hostname, String message)
    {
        Message msg = new Message();
        boolean handled = false;
        String output = "";
        ArrayList<String> outputArr = new ArrayList<String>();
        String cmd = message;
        String parms = "";
        if (message.indexOf(' ') > 0)
        {
            cmd = message.substring(0, message.indexOf(' '));
            parms = message.substring(message.indexOf(' ') + 1);
        }

        if (message.equalsIgnoreCase("suicide"))
        {
            System.exit(0);
        }
        else if (Functions.Match(message, "help"))
        {
            handled = ShowHelp(message.toLowerCase(), sender);
        }

        // Find chat
        if (!handled && Functions.Match(message, "findchat"))
        {
            handled = true;
            ArrayList<String> arrchat = Database.Chat_Find(Main.CONN, parms);

            if (arrchat.size() < 1)
            {
                sendMessage(sender, "No chat entries matched the criteria:  " + parms);
            }

            if (arrchat.size() > 10)
            {
                sendMessage(sender, "Too many results to return, try a more specific filter.");
            }
            else
            {
                for (String ch : arrchat)
                {
                    sendMessage(sender, ch);
                }
            }
        }

        // Find links
        if (!handled && Functions.Match(message, "findlink"))
        {
            handled = true;
            ArrayList<String> arrlink = Database.Link_Find(Main.CONN, parms);

            if (arrlink.size() < 1)
            {
                sendMessage(sender, "No link entries matched the criteria:  " + parms);
            }

            if (arrlink.size() > 10)
            {
                sendMessage(sender, "Too many results to return, try a more specific filter.");
            }
            else
            {
                for (String lnk : arrlink)
                {
                    sendMessage(sender, lnk);
                }
            }
        }

        // Find quote
        if (!handled && Functions.Match(message, "findquote"))
        {
            handled = true;
            ArrayList<String> arrquote = Database.Quote_Find(Main.CONN, parms);

            if (arrquote.size() < 1)
            {
                sendMessage(sender, "No quote entries matched the criteria:  " + parms);
            }

            if (arrquote.size() > 10)
            {
                sendMessage(sender, "Too many results to return, try a more specific filter.");
            }
            else
            {
                for (String qt : arrquote)
                {
                    sendMessage(sender, qt);
                }
            }
        }

        // Handle the modules
        if (!handled)
        {
            // User
            if (outputArr.size() < 1)
            {
                outputArr = ClsUser().ProcessMessage(login, hostname, message);
            }

            if (outputArr.size() > 0)
            {
                for (String s : outputArr)
                {
                    sendMessage(sender, s);
                }
            }
        }

        // Handle User modules
        if (!handled)
        {
            // Events
            if (outputArr.size() < 1)
            {
                outputArr = ClsEvent().ProcessMessage(sender, message);
            }

            if (outputArr.size() > 0)
            {
                for (String s : outputArr)
                {
                    sendMessage(sender, s);
                }
            }
        }

        // Process message against the message logic
        if (!handled)
        {
            output = msg.ProcessMessage(Nick, sender, message);

            if (!output.equals(""))
            {
                sendMessage(sender, output);
            }
        }
    };


    // Custom message tool
    public void CustomMessage(String channel, String message)
    {
        sendMessage(channel, message);
    };


    // Random methods for use w/ the idle timer
    public void RandomIdleEvent(String channel)
    {
        Random rnd = new Random();
        int chk = rnd.nextInt(4) + 1;

        switch (chk)
        {
            case R_CHAT:
                RandomChat(channel);
                break;
            case R_INSLT:
                RandomInsult(channel);
                break;
            case R_LNK:
                RandomLink(channel);
                break;
            case R_QUOT:
                RandomQuote(channel, "");
                break;
            case R_TOR:
                RandomQuote(channel, "toret");
                break;
        }
    };
    public void RandomChat(String channel)
    {
        Message msg = new Message();
        String output = msg.ProcessMessage(Nick, "", "chat");
        if (!output.equals("")) sendMessage(channel, output);
    };
    public void RandomInsult(String channel)
    {
        Message msg = new Message();
        String output = msg.ProcessMessage(Nick, "", "insult");
        if (!output.equals("")) sendMessage(channel, output);
    };
    public void RandomLink(String channel)
    {
        Message msg = new Message();
        String output = msg.ProcessMessage(Nick, "", "link");
        if (!output.equals("")) sendMessage(channel, output);
    };
    public void RandomQuote(String channel, String topic)
    {
        String output = Database.Quote_Get(Main.CONN, false, topic);
        if (!output.equals("")) sendMessage(channel, output);
    };

    // Help system
    public boolean ShowHelp(String cmd, String sender)
    {
        boolean handled = false;
        ArrayList<String> modHelp = new ArrayList<String>();

        // Handle modules' help
        if (modHelp.size() < 1)
        {
            modHelp = Chess.Help(cmd);
        }

        if (modHelp.size() > 0)
        {
            handled = true;
            for (String h : modHelp)
            {
                sendMessage(sender, h);
            }
        }

        // Standard help
        if (Functions.Match(cmd, "quote"))
        {
            sendMessage(sender,
                "Quote: The bot is capable of speaking various quotes that have been added to it. " +
                "For standard quotes it will also give attribution. Other tools like torets and " +
                "insult use the quote system. "
            );
            sendMessage(sender, "addquote <quote text>  :  Adds a new quote to the bot.");
            sendMessage(sender, "findquote <search text>  :  Finds quotes and lists their IDs.");
            sendMessage(sender, "deletequote <quote id>  :  Removes the specified quote.");
            handled = true;
        }

        if (Functions.Match(cmd, "insult"))
        {
            sendMessage(sender,
                "Insult: The bot can insult people in the channel. Insult is a utilization of the " +
                "quote tool, but it doesn't give attribution and uses its own trigger, and add command. " +
                "Insults can be searched for and deleted using the findquote and deletequote commands " +
                "(See help quote). "
            );
            sendMessage(sender, "addinsult <insult text>  :  Adds a new insult to the bot.");
            sendMessage(sender, "insult  :  Causes the bot to insult the people in the channel.");
            handled = true;
        }

        if (Functions.Match(cmd, "toret"))
        {
            sendMessage(sender,
                "Toret: The bot can have 'toret' fits using toret. " +
                "Torets can be searched for and deleted using the findquote and deletequote commands " +
                "(See help quote). "
            );
            sendMessage(sender, "addtoret <toret text>  :  Adds a new toret to the bot.");
            sendMessage(sender, "toret  :  Causes the bot to have a toret fit.");
            handled = true;
        }

        if (Functions.Match(cmd, "response"))
        {
            sendMessage(sender,
                "Response: The bot can respond to user defined keywords, making it appear to be " +
                "engaging in conversations of sorts. Keywords can be defined, and random Responses " +
                "can be assigned to the keywords. Common aliases (single word) are used to join responses and keywords. "
            );
            sendMessage(sender, "Special codes can be used with responses: ");
            sendMessage(sender, "--NICK--  :  Inserts the bot's nick. ");
            sendMessage(sender, "--SENDER--  :  Inserts the chat user's nick. ");
            sendMessage(sender, "addresponse <alias> <response text>  :  Adds a response to the specified response alias. ");
            handled = true;
        }

        if (Functions.Match(cmd, "keyword"))
        {
            sendMessage(sender,
                "Keyword: Keywords are used as triggers for their associated responses. Keywords and " +
                "responses are tied together using common aliases (single word). "
            );
            sendMessage(sender, "Special codes can be used with keywords: ");
            sendMessage(sender, "--NICK--  :  Inserts the bot's nick. ");
            sendMessage(sender, "--SENDER--  :  Inserts the chat user's nick. ");
            sendMessage(sender, "addkeyword <alias> <keyword phrase>  :  Inserts a new keyword phrase tied to the alias. ");
            handled = true;
        }

        if (Functions.Match(cmd, "alias"))
        {
            sendMessage(sender,
                "Alias: Responses can be removed if needed, using their aliases. All of the keywords and responses " +
                "associated with the alias are removed. "
            );
            sendMessage(sender, "keywords <alias>  :  Lists all the keywords associated with the specified alias. ");
            sendMessage(sender, "responsealias  :  Lists all of the current response aliases. ");
            sendMessage(sender, "deleteresponse <alias>  :  Deletes all responses and keywords associated with the alias. ");
            handled = true;
        }

        if (Functions.Match(cmd, "links"))
        {
            sendMessage(sender,
                "Links: Links in the chat are collected by the bot and can be recalled at random at a later time. "
            );
            sendMessage(sender, "link  :  Recall a random link. ");
            sendMessage(sender, "findlink <search criteria> :  Lists all links that match the search criteria, displaying the link IDs. ");
            sendMessage(sender, "deletelink <link ID>  :  Deletes the specified link. ");
            handled = true;
        }

        if (!handled)
        {
            sendMessage(sender,
                "For help, use one of the following: "
            );
            sendMessage(sender, "help quote");
            sendMessage(sender, "help insult");
            sendMessage(sender, "help toret");
            sendMessage(sender, "help response");
            sendMessage(sender, "help keyword");
            sendMessage(sender, "help alias");
            sendMessage(sender, "help links");
            handled = true;
        }


        return handled;
    };



    /*    
        ------------------------------------------------------------------------
        Property GET/SET.
    */
    public void ActiveChannel(boolean value) { activeChannel = value; }
    public boolean ActiveChannel() { return activeChannel; }

    public Chess ClsChess()
    {
        if (clsChess == null) clsChess = new Chess();

        return clsChess;
    }

    public User ClsUser()
    {
        if (clsUser == null) clsUser = new User();

        return clsUser;
    }

    public Event ClsEvent()
    {
        if (clsEvent == null) clsEvent = new Event();

        return clsEvent;
    }
};
