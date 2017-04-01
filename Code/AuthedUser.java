/*
    AUTHED USER
    ----------------------------------------------------------------------------
    Stores all the authorized users, tying thier IP to their User ID.
    ----------------------------------------------------------------------------
*/
public class AuthedUser
{
    private int userID = 0;
    private String userIP = "";
    private int userLevel = 0;
    private boolean isStale = false;

    // Constructor
    public AuthedUser(int userIDIn, String userIPIn, int userLevelIn)
    {
        UserID(userIDIn);
        UserIP(userIPIn);
        UserLevel(userLevelIn);
        IsStale(false);
    };

    // -------------------------------------------------------------------------
    // Property GET/SET
    public void UserID(int value) { userID = value; }
    public int UserID() { return userID; }

    public void UserIP(String value) { userIP = value; }
    public String UserIP() { return userIP; }

    public void UserLevel(int value) { userLevel = value; }
    public int UserLevel() { return userLevel; }

    public void IsStale(boolean value) { isStale = value; }
    public boolean IsStale() { return isStale; }
};
