import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.io.Console;
import org.jibble.pircbot.*;

/*
    MAIN CLASS
    ----------------------------------------------------------------------------
    Sets up the DB and default IRC settings then connects to IRC.
    ----------------------------------------------------------------------------
*/
public class Main
{
    // Properties.
    public static Connection CONN;
    private static boolean DEVMD = false;
    public static boolean PRNTSQL = false;
    public static boolean PRNTDBG = false;
    private static String dbPath = "Data/SygDB";

    // Constructor.
    public static void main(String[] args)
    {
        String tmp = "";

        dbPath = Functions.GetSetting("DBPATH", "Data/SygDB");

        tmp = Functions.GetSetting("DEVMD", "false");
        if (tmp.equalsIgnoreCase("true")) DEVMD = true;
        else DEVMD = false;

        if (!Main.DEVMD)
        {
            Main.PRNTSQL = false;
            Main.PRNTDBG = false;
        }
        else
        {
            tmp = Functions.GetSetting("PRNTSQL", "false");
            if (tmp.equalsIgnoreCase("true")) PRNTSQL = true;
            else PRNTSQL = false;

            tmp = Functions.GetSetting("PRNTDBG", "false");
            if (tmp.equalsIgnoreCase("true")) PRNTDBG = true;
            else PRNTDBG = false;
        }

        // Initialize the database.
        SetConn();

        // Initialize the Irc Class.
        Irc irc = new Irc();
    };


    // -------------------------------------------------------------------------
    // Property GET/SET
    public static void SetConn()
    {
        boolean newDB = false;

        if (CONN == null)
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
                CONN = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if (newDB)
        {
            System.out.println("Creating new bot database...");
            DatabaseHelper.CreateDatabase(CONN);
        }
    }
};
