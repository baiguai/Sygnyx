import java.io.*;
import java.util.*;

/*
    GENERAL HELPER FUNCTIONS
    ----------------------------------------------------------------------------
    Methods designed to make life easier.
    ----------------------------------------------------------------------------
*/
public class Functions
{
    // Case insensitive match - or contains.
    public static boolean Match(String source1, String source2)
    {
        boolean isMatch = false;
        source2 = source2.replace("|", ",");
        String[] arr = source2.split(",");

        for (String itm: arr)
        {
            if (source1.equalsIgnoreCase(itm) ||
                source1.toLowerCase().contains(source2.toLowerCase()))
            {
                isMatch = true;
                break;
            }
        }

        return isMatch;
    };

    public static void Output(String message)
    {
        System.out.println(message);
    };

    // Array to string.
    public static String ArrayToString(String[] array, int startIndex, String delimiter)
    {
        String output = "";

        for (int ix = startIndex; ix < array.length; ix++)
        {
            if (!output.equals("")) output += delimiter;
            output += array[ix];
        }

        return output;
    }

    // Sql Cleanup.
    public static String SqlCleanup(String input)
    {
        input = input.replaceAll("'", "''");

        return input;
    };

    // Get Setting
    public static String GetSetting(String key, String defVal)
    {
        Properties prop = new Properties();
        String value = "";

        try
        {
            Properties props = new Properties();
            String propFile = "./Bot.properties";
            FileInputStream inputStream = new FileInputStream(propFile);

            if (inputStream != null)
            {
                prop.load(inputStream);
                value = prop.getProperty(key);
            }
            else
            {
                key = "";
                //throw new FileNotFoundException("Config file not found: " + propFile);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if (value.equals("")) value = defVal;

        return value;
    };
};
