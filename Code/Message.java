import org.jibble.pircbot.*;

/*
    --[MESSAGE HANDLER]---------------------------------------------------------
    Handles all messages coming into the channel.
    ----------------------------------------------------------------------------
*/
public class Message extends PircBot
{
    public String ProcessMessage(String nick, String sender, String msg)
    {
        String output = "";
        boolean handled = false;
        String[] arr = msg.split(" ");
        String cmd = "";
        String topic = "";

        if (arr.length > 0)
        {
            cmd = arr[0];
        }

        if (arr.length > 1)
        {
            topic = arr[1];
        }

        // Add insult.
        if (!handled && Functions.Match(cmd, "addinsult"))
        {
            String tmp = Functions.ArrayToString(arr, 1, " ");

            output = Database.Quote_Add(Main.CONN, sender, "insult", tmp);
            handled = true;
        }

        // Add Keyword.
        if (!handled && Functions.Match(cmd, "addkeyword"))
        {
            String alias = topic;
            String tmp = Functions.ArrayToString(arr, 2, " ");

            // Substitute the bot's nick
            tmp = tmp.replaceAll("(?i)" + nick, "--NICK--");

            output = Database.Keyword_Add(Main.CONN, alias, tmp);
            handled = true;
        }

        // Add Quote.
        // addquote||topic quote text ...
        if (!handled && Functions.Match(cmd, "addquote"))
        {
            String tmp = Functions.ArrayToString(arr, 1, " ");

            output = Database.Quote_Add(Main.CONN, sender, "", tmp);
            handled = true;
        }

        // Add Response.
        if (!handled && Functions.Match(cmd, "addresponse"))
        {
            String alias = topic;
            String tmp = Functions.ArrayToString(arr, 2, " ");

            output = Database.Response_Add(Main.CONN, alias, tmp);
            handled = true;
        }

        // Add Toret.
        if (!handled && Functions.Match(cmd, "addtoret"))
        {
            String tmp = Functions.ArrayToString(arr, 1, " ");

            output = Database.Quote_Add(Main.CONN, sender, "toret", tmp);
            handled = true;
        }


        // Delete Chat.
        if (!handled && Functions.Match(cmd, "deletechat"))
        {
            String chatID = topic;

            output = Database.Chat_Delete(Main.CONN, chatID);

            if (!output.equals(""))
            {
                output = "Chat entry deleted.";
                handled = true;
            }
        }

        // Delete Link.
        if (!handled && Functions.Match(cmd, "deletelink"))
        {
            String linkID = topic;

            output = Database.Link_Delete(Main.CONN, linkID);

            if (!output.equals(""))
            {
                output = "Link entry deleted.";
                handled = true;
            }
        }

        // Delete Quote.
        if (!handled && Functions.Match(cmd, "deletequote"))
        {
            String quoteID = topic;

            output = Database.Quote_Delete(Main.CONN, quoteID);

            if (!output.equals(""))
            {
                output = "Quote deleted.";
                handled = true;
            }
        }

        // Delete Response.
        if (!handled && Functions.Match(cmd, "deleteresponse"))
        {
            String alias = topic;

            output = Database.Response_Delete(Main.CONN, alias);
            output = Database.Keyword_Delete(Main.CONN, alias);

            if (!output.equals(""))
            {
                output = "Response deleted, and its keywords removed.";
                handled = true;
            }
        }

        // Response Alias List.
        if (!handled && Functions.Match(cmd, "responsealias"))
        {
            output = Database.ResponseAlias_Get(Main.CONN);
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Add links.
        if (!handled && Functions.Match(msg, "http"))
        {
            output = Database.Link_Add(Main.CONN, sender, msg);
            handled = true;
        }

        if (!handled && Functions.Match(msg, nick + " help"))
        {
            output = "";
            handled = true;
        }

        // Link.
        if (!handled && Functions.Match(cmd, "link"))
        {
            output = Database.Link_Get(Main.CONN);
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Keyword List.
        if (!handled && Functions.Match(cmd, "keywords"))
        {
            output = Database.Keyword_GetList(Main.CONN, topic);
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Insult.
        if (!handled && Functions.Match(cmd, "insult"))
        {
            output = Database.Quote_Get(Main.CONN, false, "insult");
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Chat.
        if (!handled && Functions.Match(cmd, "chat"))
        {
            output = Database.Chat_Get(Main.CONN);
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Toret.
        if (!handled && Functions.Match(cmd, "toret"))
        {
            output = Database.Quote_Get(Main.CONN, false, "toret");
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Quote.
        if (!handled && Functions.Match(cmd, "quote"))
        {
            output = Database.Quote_Get(Main.CONN, false, "");
            if (!output.equals(""))
            {
                handled = true;
            }
        }

        // Time.
        if (!handled && Functions.Match(msg, nick))
        {
            if (Functions.Match(msg, "time")) {
                String time = new java.util.Date().toString();
                output = sender + ": The time is now " + time + "  ";
                handled = true;
            }
        }



        // Add chat.
        if (!handled && arr.length > 7)
        {
            // Criteria for storing 'interesting' comments
            boolean doKeep = true;

            // Remove ASCII art
            if (
                msg.indexOf("--") >= 0 ||
                msg.indexOf("__") >= 0 ||
                msg.indexOf("||") >= 0 ||
                msg.indexOf("| |") >= 0 ||
                msg.indexOf("|  |") >= 0 ||
                msg.indexOf("**") >= 0 ||
                msg.indexOf("##") >= 0 ||
                msg.indexOf("/ /") >= 0 ||
                msg.indexOf("\\ \\") >= 0 ||
                msg.indexOf("##") >= 0 ||
                msg.indexOf("   ") >= 0 ||
                msg.toUpperCase().indexOf("MM") >= 0
            )
            {
                doKeep = false;
            }

            // Remove mundane statuses
            if (
                msg.toUpperCase().indexOf("DISCONNECT") >= 0 ||
                msg.toUpperCase().indexOf("SERVER") >= 0 ||
                msg.toUpperCase().indexOf("BOT") >= 0
            )
            {
                doKeep = false;
            }

            if (doKeep)
            {
                output = Database.Chat_Add(Main.CONN, sender, msg);
                handled = true;
            }
        }

        // TRY TO GET A RESPONSE
        if (!handled)
        {
            String tmp = msg;
            
            tmp = tmp.replaceAll("(?i)" + nick, "--NICK--");
            tmp = tmp.replaceAll("(?i)" + sender, "--SENDER--");

            output = Database.Response_Get(Main.CONN, tmp);
            if (!output.equals(""))
            {
                output = output.replaceAll("--NICK--", nick);
                output = output.replaceAll("--SENDER--", sender);
                handled = true;
            }
        }

        return output;
    };
};
