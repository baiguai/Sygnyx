import java.io.*;
import java.sql.*;
import java.util.*;

/*
    DATABASE
    ----------------------------------------------------------------------------
    Database related functions, works in conjunction with the Sql strings.
    ----------------------------------------------------------------------------
*/
public class Database
{
    // Add chat.
    public static String Chat_Add(Connection conn, String nick, String message)
    {
        String output = "";

        nick = Functions.SqlCleanup(nick);
        message = Functions.SqlCleanup(message);

        if (nick.equals(""))
        {
            return "Something has gone wrong. There is no nick.";
        }
        if (message.equals(""))
        {
            return "Hmm. There is no message to add.";
        }

        try
        {
            String sql = "";
            Statement cmd = conn.createStatement();

            sql = Sql.Chat_Add(nick, message);

            cmd.execute(sql);

            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Delete chat.
    public static String Chat_Delete(Connection conn, String message)
    {
        int chatID = 0;
        String output = "";
        message = Functions.SqlCleanup(message);

        if (message.equals(""))
        {
            return "Hmm. There is no chat to add.";
        }

        try
        {
            chatID = Integer.parseInt(message);

            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Chat_Delete(chatID);
            cmd.execute(sql);

            cmd.close();

            output = "success";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Find chat - get chatIDs
    public static ArrayList<String> Chat_Find(Connection conn, String filter)
    {
        int chatID = 0;
        String chat = "";
        ArrayList<String> output = new ArrayList<String>();
        String tmp = "";
        filter = Functions.SqlCleanup(filter);

        if (filter.equals(""))
        {
            output.add("I'll need something to filter this search on.");
            return output;
        }

        try
        {
            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Chat_Find(filter);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                chatID = rs.getInt("ChatID");
                chat = rs.getString("Message");

                if (chat.length() > 60)
                {
                    chat = chat.substring(0, 60) + "...";
                }

                tmp = "[" + chatID + "] " + chat;
                output.add(tmp);
            }

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Get chat.
    public static String Chat_Get(Connection conn)
    {
        int chatID = 0;
        String output = "";

        try
        {
            ManageUnaccessed(conn, "Chat", "");

            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Chat_Get();
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                chatID = rs.getInt("ChatID");
                output = rs.getString("Contributor") + ": ";
                output += rs.getString("Message");
            }

            rs.close();
            cmd.close();

            AccessedCount_Increase(conn, "Chat", chatID);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Add Keyword.
    public static String Keyword_Add(Connection conn, String alias, String keyword)
    {
        String output = "";
        alias = Functions.SqlCleanup(alias);
        keyword = Functions.SqlCleanup(keyword);

        if (alias.equals(""))
        {
            return "It doesn't look like you specified an alias.";
        }

        if (keyword.equals(""))
        {
            return "Hey! If you add a blank keyword, that's gonna cause problems.";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Keyword_Add(alias, keyword);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            cmd.execute(sql);

            cmd.close();

            output = "Keyword added.";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Delete Keyword.
    public static String Keyword_Delete(Connection conn, String alias)
    {
        String output = "";
        alias = Functions.SqlCleanup(alias);

        if (alias.equals(""))
        {
            return "Um. You will need to specify the response alias associated with the keyword.";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Keyword_Delete(alias);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            cmd.execute(sql);

            cmd.close();

            output = "Keyword deleted.";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Keyword Get List.
    public static String Keyword_GetList(Connection conn, String alias)
    {
        String output = "";
        alias = Functions.SqlCleanup(alias);

        if (alias.equals(""))
        {
            return "You will need to tell me the response alias for the keywords.";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Keyword_GetList(alias);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                if (!output.equals("")) output += ", ";
                output += rs.getString("Keyword");
            }

            output = "Keywords:  " + output;

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Delete Response.
    public static String Response_Delete(Connection conn, String alias)
    {
        String output = "";
        alias = Functions.SqlCleanup(alias);

        if (alias.equals(""))
        {
            return "I'll need to know the alias for the responses to delete.";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Response_Delete(alias);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            cmd.execute(sql);

            cmd.close();

            output = "Response deleted.";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Add link.
    public static String Link_Add(Connection conn, String nick, String link)
    {
        String output = "";
        nick = Functions.SqlCleanup(nick);
        link = Functions.SqlCleanup(link);

        if (nick.equals(""))
        {
            return "Something went wrong. There is no nick.";
        }
        if (link.equals(""))
        {
            return "Hmm. There is no link to add.";
        }

        try
        {
            String sql = "";
            Statement cmd = conn.createStatement();

            sql = Sql.Link_Add(nick, link);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            cmd.execute(sql);

            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Delete Link.
    public static String Link_Delete(Connection conn, String message)
    {
        int linkID = 0;
        String output = "";
        message = Functions.SqlCleanup(message);

        if (message.equals(""))
        {
            return "Um. I'll need a message to delete.";
        }

        try
        {
            linkID = Integer.parseInt(message);

            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Link_Delete(linkID);
            cmd.execute(sql);

            cmd.close();

            output = "success";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Find link - get linkIDs
    public static ArrayList<String> Link_Find(Connection conn, String filter)
    {
        int linkID = 0;
        String link = "";
        ArrayList<String> output = new ArrayList<String>();
        String tmp = "";
        filter = Functions.SqlCleanup(filter);

        if (filter.equals(""))
        {
            output.add("I'll need something to filter this search on.");
            return output;
        }

        try
        {
            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Link_Find(filter);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                linkID = rs.getInt("LinkID");
                link = rs.getString("Url");

                if (link.length() > 60)
                {
                    link = link.substring(0, 60) + "...";
                }

                tmp = "[" + linkID + "] " + link;
                output.add(tmp);
            }

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Get link.
    public static String Link_Get(Connection conn)
    {
        int linkID = 0;
        String output = "";

        try
        {
            ManageUnaccessed(conn, "Link", "");

            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Link_Get();
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                linkID = rs.getInt("LinkID");
                output = rs.getString("Contributor") + ": ";
                output += rs.getString("Url");
            }

            rs.close();
            cmd.close();

            AccessedCount_Increase(conn, "Link", linkID);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Add Quote.
    public static String Quote_Add(Connection conn, String nick, String topic, String quote)
    {
        String output = "";
        topic = Functions.SqlCleanup(topic);
        quote = Functions.SqlCleanup(quote);

        if (quote.equals(""))
        {
            return "You'll probably want to specify a quote for me to add.";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Quote_Add(nick, topic, quote);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            cmd.execute(sql);

            cmd.close();

            output = "Quote added.";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Delete quote.
    public static String Quote_Delete(Connection conn, String message)
    {
        int quoteID = 0;
        String output = "";
        message = Functions.SqlCleanup(message);

        if (message.equals(""))
        {
            return "Yeah. I'll need you to specify a quote to delete.";
        }

        try
        {
            quoteID = Integer.parseInt(message);

            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Quote_Delete(quoteID);
            cmd.execute(sql);

            cmd.close();

            output = "success";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Find quote - get quoteIDs
    public static ArrayList<String> Quote_Find(Connection conn, String filter)
    {
        int quoteID = 0;
        String quote = "";
        ArrayList<String> output = new ArrayList<String>();
        String tmp = "";
        filter = Functions.SqlCleanup(filter);

        if (filter.equals(""))
        {
            output.add("I'll need a filter to use to search for quotes to delete.");
            return output;
        }

        try
        {
            String sql = "";
            Statement cmd = conn.createStatement();
            
            sql = Sql.Quote_Find(filter);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                quoteID = rs.getInt("QuoteID");
                quote = rs.getString("Quote");

                if (quote.length() > 60)
                {
                    quote = quote.substring(0, 60) + "...";
                }

                tmp = "[" + quoteID + "] " + quote;
                output.add(tmp);
            }

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Get Quote.
    public static String Quote_Get(Connection conn, boolean attribution, String topic)
    {
        String output = "";
        topic = Functions.SqlCleanup(topic);

        try
        {
            ManageUnaccessed(conn, "Quote", topic);

            String sql = "";
            int quoteID = 0;
            Statement cmd = conn.createStatement();

            sql = Sql.Quote_Get(topic);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql + "\n\n");
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                quoteID = rs.getInt("QuoteID");
                if (attribution) output = rs.getString("Contributor") + ": ";
                output += rs.getString("Quote");
            }

            rs.close();
            cmd.close();

            AccessedCount_Increase(conn, "Quote", quoteID);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Add Response.
    public static String Response_Add(Connection conn, String alias, String response)
    {
        String output = "";
        alias = Functions.SqlCleanup(alias);
        response = Functions.SqlCleanup(response);

        if (alias.equals(""))
        {
            return "I'll need a response alias to associate this response to.";
        }
        if (response.equals(""))
        {
            return "Soo... You want me to respond by saying nothing?";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Response_Add(alias, response);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            cmd.execute(sql);

            cmd.close();

            output = "Response added.";
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Response Get.
    public static String Response_Get(Connection conn, String message)
    {
        String output = "";
        message = Functions.SqlCleanup(message);

        if (message.equals(""))
        {
            return "I'll need a response to get.";
        }

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.Response_Get(message);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                output = rs.getString("Response");
                break;
            }

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };

    // Response Alias Get List.
    public static String ResponseAlias_Get(Connection conn)
    {
        String output = "";

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.ResponseAlias_Get();
            if (Main.PRNTSQL) System.out.println("Sql: " + sql);
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                if (!output.equals("")) output += ", ";
                output += rs.getString("ResponseAlias");
            }

            output = "Response Aliases:  " + output;

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return output;
    };



    // Update AccessedCount.
    public static void AccessedCount_Increase(Connection conn, String table, int rowID)
    {
        AccessedCount_Increase(conn, table, "", rowID);
    };
    public static void AccessedCount_Increase(Connection conn, String table, String topic, int rowID)
    {
        int nonAcc = 0;

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";


            sql = Sql.AccessedCount_Increase(table, rowID);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql + "\n\n");
            cmd.execute(sql);

            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };
    public static void ManageUnaccessed(Connection conn, String table, String topic)
    {
        if (UnAccessedCount_Get(conn, table, topic) < 1)
        {
            System.out.println("Resetting....");
            ResetUnaccessed(conn, table, topic);
        }
    };
    public static void ResetUnaccessed(Connection conn, String table, String topic)
    {
        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.AccessedCount_Reset(table, topic);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql + "\n\n");
            cmd.execute(sql);

            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };
    public static int UnAccessedCount_Get(Connection conn, String table, String topic)
    {
        int nonAcc = 0;

        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            sql = Sql.UnAccessedCount_Get(table, topic);
            if (Main.PRNTSQL) System.out.println("Sql: " + sql + "\n\n");
            ResultSet rs = cmd.executeQuery(sql);

            while (rs.next())
            {
                nonAcc = rs.getInt("NonAccessed");
            }

            rs.close();
            cmd.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return nonAcc;
    };
};
