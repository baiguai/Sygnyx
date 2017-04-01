import java.io.*;
import java.sql.*;

/*
    DATABASE HELPER METHODS
    ----------------------------------------------------------------------------
    Rarely used, or purely DB creation / maintenance methods.
    Eventually the bot needs to me more modular - scalable.
    ----------------------------------------------------------------------------
*/
public class DatabaseHelper
{
    // Create the primary database.
    public static void CreateDatabase(Connection conn)
    {
        try
        {
            Statement cmd = conn.createStatement();
            String sql = "";

            // NEWS FEEDS TABLE
            // The various news feeds to cull topics from.
            sql = "";
            sql += "CREATE TABLE ";
            sql += "    Feeds (";
            sql += "        Url VARCHAR(300) NOT NULL, ";
            sql += "        IntervalMinutes BIGINT NOT NULL DEFAULT 60, ";
            sql += "        Topics VARCHAR(300), ";
            sql += "        AccessedCount BIGINT NOT NULL DEFAULT 0, ";
            sql += "        Contributor VARCHAR(100) NOT NULL, ";
            sql += "        Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);

            // LINKS TABLE
            // Collects links sent to the channels.
            sql = "";
            sql += "CREATE TABLE ";
            sql += "    Link ( ";
            sql += "        Url VARCHAR(300) NOT NULL, ";
            sql += "        AccessedCount BIGINT NOT NULL DEFAULT 0, ";
            sql += "        Contributor VARCHAR(100) NOT NULL, ";
            sql += "        Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);

            // CHAT TABLE
            // Comments that the bot collects on its own, and spouts randomly.
            sql = "";
            sql += "CREATE TABLE ";
            sql += "    Chat ( ";
            sql += "        Message BLOB NOT NULL, ";
            sql += "        AccessedCount BIGINT NOT NULL DEFAULT 0, ";
            sql += "        Contributor VARCHAR(100) NOT NULL, ";
            sql += "        Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);

            // QUOTE TABLE
            // Part of the quote 'module'.
            sql = "";
            sql += "CREATE TABLE ";
            sql += "    Quote (";
            sql += "        Topic VARCHAR(300), ";
            sql += "        Quote BLOB NOT NULL, ";
            sql += "        AccessedDate VARCHAR(25), ";
            sql += "        AccessedCount BIGINT NOT NULL DEFAULT 0, ";
            sql += "        Contributor VARCHAR(100) NOT NULL, ";
            sql += "        Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);

            // RESPONSE TABLE
            // Looks for certain words in the chat and responds to them.
            sql = "";
            sql += "CREATE TABLE ";
            sql += "    Keyword ( ";
            sql += "        ResponseAlias VARCHAR(100) NOT NULL, ";
            sql += "        Keyword VARCHAR(200) NOT NULL, ";
            sql += "        Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);

            sql = "";
            sql += "CREATE TABLE ";
            sql += "    Response ( ";
            sql += "        ResponseAlias VARCHAR(100) NOT NULL, ";
            sql += "        Response BLOB NOT NULL, ";
            sql += "        AccessedCount BIGINT NOT NULL DEFAULT 0, ";
            sql += "        Deleted BOOL NOT NULL DEFAULT 0 ";
            sql += ");";
            System.out.println("SQL: " + sql);
            cmd.execute(sql);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    };
};
