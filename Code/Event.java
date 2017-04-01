import java.io.*;
import java.sql.*;
import java.util.*;

/*
    EVENT
    ----------------------------------------------------------------------------
    Allows users to set events and query them
    ----------------------------------------------------------------------------
*/
public class Event
{
    // Properties.
    public Connection CONNEvt;
    private String dbPath = "Data/EventDB";

    // Constructor
    public Event()
    {
        SetConn();
    };


    // Process Messages
    public ArrayList<String> ProcessMessage(String sender, String cmd)
    {
        ArrayList<String> output = new ArrayList<String>();
        String[] arr = cmd.split(" ");
        boolean matched = false;

        if (output.size() < 1)
        {
            if (!matched && Functions.Match(cmd, "eventadd"))
            {
                matched = true;
                if (arr.length < 3)
                {
                    output.add("For information on events, msg me with the text: help events");
                }
                else
                {
                    String date = arr[1];
                    String eventName = Functions.ArrayToString(arr, 2, " ");
                    if (AddEvent(sender, eventName, date))
                    {
                        output.add("I have successfully created your event.");
                    }
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
            Statement cmd = CONNEvt.createStatement();
            String sql = "";

            sql = "";
            sql += "CREATE TABLE UserEvent ( ";
            sql += "    Owner VARCHAR(150) NOT NULL, ";
            sql += "    EventName VARCHAR(300) NOT NULL, ";
            sql += "    EventDate VARCHAR(30), ";
            sql += "    Notified BOOL NOT NULL DEFAULT 0, ";
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

    private boolean AddEvent(String sender, String eventName, String date)
    {
        boolean success = false;

        try
        {
            Statement cmd = CONNEvt.createStatement();
            String sql = Sql_Event.Event_Insert(sender, eventName, date);

            cmd.execute(sql);
            success = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return success;
    };




    // -------------------------------------------------------------------------
    // Property GET/SET
    public void SetConn()
    {
        boolean newDB = false;

        if (CONNEvt == null)
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
                CONNEvt = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if (newDB)
        {
            System.out.println("Creating new event database...");
            CreateDatabase();
        }
    }
};
