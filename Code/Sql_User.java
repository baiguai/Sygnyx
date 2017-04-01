import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    SQL USER
    ----------------------------------------------------------------------------
    All user database sql statements are defined here. - If this gets unweildy,
    split these out into logical categories etc.
    ----------------------------------------------------------------------------
*/
public class Sql_User
{
    public static String AuthorizeUser(String password)
    {
        String sql = "";

        sql += "SELECT ";
        sql += "    ROWID UserID, ";
        sql += "    UserIP, ";
        sql += "    UserLevel ";
        sql += "FROM ";
        sql += "    BotUser ";
        sql += "WHERE 1 = 1 ";
        sql += "    AND Deleted = 0 ";
        sql += "    AND Password = '" + password + "' ";
        sql += ";";

        return sql;
    };

    public static String Register(String password, int userLevel)
    {
        String sql = "";

        sql += "INSERT INTO BotUser ( ";
        sql += "    Password, ";
        sql += "    UserLevel ";
        sql += ") ";
        sql += "SELECT '" + password + "', " + userLevel + ";";

        return sql;
    };
};
