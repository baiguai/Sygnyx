import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    SQL EVENT
    ----------------------------------------------------------------------------
    The Event database sql scripts.
    ----------------------------------------------------------------------------
*/
public class Sql_Event
{
    public static String Event_Insert(String owner, String eventName, String date)
    {
        String sql = "";
        owner = Functions.SqlCleanup(owner);
        eventName = Functions.SqlCleanup(eventName);
        date = Functions.SqlCleanup(date);

        sql += "INSERT INTO Event ( ";
        sql += "    Owner, ";
        sql += "    EventName, ";
        sql += "    EventDate ";
        sql += ") ";
        sql += "SELECT '" + owner + "', '" + eventName + "', '" + date + "' ";
        sql += ";";

        return sql;
    };
};
