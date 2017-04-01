import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    SQL
    ----------------------------------------------------------------------------
    All sql statements are defined here. - If this gets unweildy, split these
    out into logical categories etc.
    ----------------------------------------------------------------------------
*/
public class Sql
{
    public static String AccessedCount_Increase(String table, int rowID)
    {
        String sql = "";

        sql += "UPDATE " + table + " ";
        sql += "    SET AccessedCount = (AccessedCount + 1) ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND ROWID = " + rowID + " ";
        sql += ";";

        return sql;
    };

    public static String AccessedCount_Reset(String table, String topic)
    {
        String sql = "";

        sql += "UPDATE " + table + " ";
        sql += "    SET AccessedCount = 0 ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";

        if (table.equalsIgnoreCase("quote"))
        {
            if (!topic.equals(""))
            {
                sql += "    AND Topic = '" + topic + "' ";
            }
            else
            {
                sql += "    AND IFNULL(Topic, '') = '' ";
            }
        }

        sql += ";";

        return sql;
    };

    public static String Chat_Add(String nick, String message)
    {
        String sql = "";

        sql += "INSERT INTO ";
        sql += "    Chat ( ";
        sql += "        Message, ";
        sql += "        Contributor, ";
        sql += "        AccessedCount ";
        sql += ") ";
        sql += "SELECT '" + message + "', '" + nick + "', 1 ";
        sql += ";";

        return sql;
    };

    public static String Chat_Delete(int chatID)
    {
        String sql = "";

        sql += "UPDATE ";
        sql += "    Chat ";
        sql += "SET ";
        sql += "    Deleted = 1 ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND ROWID = " + chatID + " ";
        sql += ";";

        return sql;
    };

    public static String Chat_Find(String filter)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID ChatID, ";
        sql += "    Message ";
        sql += "FROM ";
        sql += "    Chat ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "    AND Message LIKE '%" + filter + "%' ";
        sql += ";";

        return sql;
    };

    public static String Chat_Get()
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID ChatID, ";
        sql += "    Contributor, ";
        sql += "    Message ";
        sql += "FROM ";
        sql += "    Chat ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND AccessedCount < 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "ORDER BY RANDOM() ";
        sql += "LIMIT 1 ";
        sql += ";";

        return sql;
    };

    public static String Command_Clear()
    {
        String sql = "";

        sql += "DELETE FROM Command ";
        sql += ";";

        return sql;
    };

    public static String Command_Get(String input)
    {
        String sql = "";

        sql += "SELECT * FROM Command ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Command LIKE '" + input + "' ";
        sql += "    AND Deleted = 0 ";
        sql += ";";

        return sql;
    };

    public static String Command_Populate()
    {
        String sql = "";

        sql += "INSERT INTO Command (";
        sql += "    Command, ";
        sql += "    Action ";
        sql += ") ";
        sql += "SELECT 'link', 'link' ";
        sql += "UNION ";
        sql += "SELECT 'quote', 'quote' ";
        sql += "UNION ";
        sql += "SELECT 'toret', 'toret' ";
        sql += "UNION ";
        sql += "SELECT 'addquote', 'addquote' ";
        sql += "UNION ";
        sql += "SELECT 'addtoret', 'addtoret' ";
        sql += "UNION ";
        sql += "SELECT 'deletequote', 'deletequote' ";
        sql += "UNION ";
        sql += "SELECT 'addfeed', 'addfeed' ";
        sql += "UNION ";
        sql += "SELECT 'deletefeed', 'deletefeed' ";
        sql += ";";

        return sql;
    };

    public static String Keyword_Add(String alias, String keyword)
    {
        String sql = "";

        sql += "INSERT INTO Keyword (";
        sql += "    ResponseAlias, ";
        sql += "    Keyword ";
        sql += ") ";
        sql += "SELECT '" + alias + "', '" + keyword + "' ";
        sql += ";";

        return sql;
    };

    public static String Keyword_Delete(String alias)
    {
        String sql = "";

        sql += "DELETE FROM Keyword ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND ResponseAlias = '" + alias + "' ";
        sql += ";";

        return sql;
    };

    public static String Keyword_GetList(String alias)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    Keyword ";
        sql += "FROM ";
        sql += "    Keyword ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "    AND ResponseAlias = '" + alias + "' ";
        sql += "GROUP BY ";
        sql += "    Keyword ";
        sql += "ORDER BY ";
        sql += "    Keyword ";
        sql += ";";

        return sql;        
    };

    public static String Link_Add(String nick, String link)
    {
        String sql = "";

        sql += "INSERT INTO Link ( ";
        sql += "    Url, ";
        sql += "    Contributor ";
        sql += ") ";
        sql += "SELECT '" + link + "', '" + nick + "' ";
        sql += ";";

        return sql;
    };

    public static String Link_Delete(int linkID)
    {
        String sql = "";

        sql += "UPDATE ";
        sql += "    Link ";
        sql += "SET ";
        sql += "    Deleted = 1 ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND ROWID = " + linkID + " ";
        sql += ";";

        return sql;
    };

    public static String Link_Find(String filter)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID LinkID, ";
        sql += "    Url ";
        sql += "FROM ";
        sql += "    Link ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "    AND Url LIKE '%" + filter + "%' ";
        sql += ";";

        return sql;
    };

    public static String Link_Get()
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID LinkID, ";
        sql += "    Url, ";
        sql += "    Contributor ";
        sql += "FROM ";
        sql += "    Link ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND AccessedCount < 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "ORDER BY RANDOM() ";
        sql += "LIMIT 1 ";
        sql += ";";

        return sql;
    };

    public static String Quote_Add(String nick, String topic, String quote)
    {
        String sql = "";

        sql += "INSERT INTO Quote (";
        sql += "    Topic, ";
        sql += "    Quote, ";
        sql += "    Contributor ";
        sql += ") ";
        sql += "SELECT '" + topic + "', '" + quote + "', '" + nick + "' ";
        sql += ";";

        return sql;
    };

    public static String Quote_Delete(int quoteID)
    {
        String sql = "";

        sql += "UPDATE ";
        sql += "    Quote ";
        sql += "SET ";
        sql += "    Deleted = 1 ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND ROWID = " + quoteID + " ";
        sql += ";";

        return sql;
    };

    public static String Quote_Find(String filter)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID QuoteID, ";
        sql += "    Quote ";
        sql += "FROM ";
        sql += "    Quote ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "    AND Quote LIKE '%" + filter + "%' ";
        sql += ";";

        return sql;
    };

    public static String Quote_Get(String topic)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID QuoteID, ";
        sql += "    * ";
        sql += "FROM ";
        sql += "    Quote ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND AccessedCount = 0 ";
        sql += "    AND Deleted = 0 ";
        if (!topic.equals(""))
        {
            sql += "    AND Topic = '" + topic + "' ";
        }
        else
        {
            sql += "    AND IFNULL(Topic, '') = '' ";
        }
        sql += "ORDER BY ";
        sql += "    RANDOM() ";
        sql += "LIMIT 1 ";
        sql += ";";

        return sql;
    };

    public static String Response_Add(String alias, String response)
    {
        String sql = "";

        sql += "INSERT INTO Response (";
        sql += "    ResponseAlias, ";
        sql += "    Response ";
        sql += ") ";
        sql += "SELECT '" + alias + "', '" + response + "' ";
        sql += ";";

        return sql;
    };

    public static String Response_Delete(String alias)
    {
        String sql = "";

        sql += "DELETE FROM Response ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND ResponseAlias = '" + alias + "' ";
        sql += ";";

        return sql;
    };

    public static String Response_Get(String input)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    r.Response ";
        sql += "FROM ";
        sql += "    Response r ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND r.Deleted = 0 ";
        sql += "    AND r.ResponseAlias = ( ";
        sql += "        SELECT k.ResponseAlias ";
        sql += "        FROM Keyword k ";
        sql += "        WHERE 1 = 1 ";
        sql += "            AND k.Deleted = 0 ";
        sql += "            AND '" + input + "' LIKE '%'||k.Keyword||'%' ";
        sql += "        LIMIT 1 ";
        sql += "    ) ";
        sql += "ORDER BY RANDOM() ";
        sql += "LIMIT 1 ";
        sql += ";";

        return sql;
    };

    public static String ResponseAlias_Get()
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ResponseAlias ";
        sql += "FROM ";
        sql += "    Response ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "GROUP BY ";
        sql += "    ResponseAlias ";
        sql += "ORDER BY ";
        sql += "    ResponseAlias ";
        sql += ";";

        return sql;        
    };

    public static String UnAccessedCount_Get(String table, String topic)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    COUNT(*) NonAccessed ";
        sql += "FROM ";
        sql += "    " + table + " ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND AccessedCount = 0 ";
        sql += "    AND Deleted = 0 ";

        if (table.equalsIgnoreCase("quote"))
        {
            if (!topic.equals(""))
            {
                sql += "    AND Topic = '" + topic + "' ";
            }
            else
            {
                sql += "    AND IFNULL(Topic, '') = '' ";
            }
        }

        sql += "ORDER BY ";
        sql += "    RANDOM() ";
        sql += "LIMIT 1 ";
        sql += ";";

        return sql;
    };
};
