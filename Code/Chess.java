import java.util.*;
import java.lang.Math;

/*
    CHESS
    ----------------------------------------------------------------------------
    the chess playing modue.
    ----------------------------------------------------------------------------
*/
public class Chess
{
    private static String x = "a,b,c,d,e,f,g,h";
    private static String y = "8,7,6,5,4,3,2,1";
    private static String white = "";
    private static String black = "";
    private static boolean blackTurn = false;

    public static ArrayList<String> Help(String cmd)
    {
        ArrayList<String> output = new ArrayList<String>();

        if (Functions.Match(cmd, "help chess"))
        {
            output.add("Chess Help:");
        }

        return output;
    };


    // Constructor
    public Chess()
    {
        Reset();
    };


    // Process Messages
    public static ArrayList<String> ProcessMessage(String hostname, String cmd)
    {
        boolean handled = false;
        ArrayList<String> output = new ArrayList<String>();
        String[] arr = cmd.split(" ");

        if (output.size() < 1)
        {
            if (!handled && Functions.Match(cmd, "chess n"))
            {
                handled = true;
                Reset();
                output = DrawBoard();
            }
            if (!handled && Functions.Match(cmd, "chess brd"))
            {
                handled = true;
                output = DrawBoard();
            }

            if (!handled && Functions.Match(cmd, "chess mv"))
            {
                handled = true;
                if (arr.length == 3)
                {
                    output = Move(arr[2]);
                }
                else
                {
                    output.add("Error!");
                }
            }

            if (!handled && Functions.Match(cmd, "chess help|chess ?"))
            {
                handled = true;
                output.add("To view the chess help system, send me a private message: chess help");
            }
        }

        return output;
    };
    public static ArrayList<String> ProcessPrivateMessage(String hostname, String cmd)
    {
        ArrayList<String> output = new ArrayList<String>();
        String[] arr = cmd.split(" ");

        if (output.size() < 1)
        {
            if (Functions.Match(cmd, "chess help|chess ?"))
            {
                Reset();
                output = Help(cmd);
            }
        }

        return output;
    };


    // General Methods
    public static void Reset()
    {
        Black("ra8,nb8,bc8,qd8,ke8,bf8,ng8,rh8,pa7,pb7,pc7,pd7,pe7,pf7,pg7,ph7");
        White("Ra1,Nb1,Bc1,Qd1,Ke1,Bf1,Ng1,Rh1,Pa2,Pb2,Pc2,Pd2,Pe2,Pf2,Pg2,Ph2");
    };

    public static ArrayList<String> DrawBoard()
    {
        String[] xArr = X().split(",");
        String[] yArr = Y().split(",");
        String[] w = White().split(",");
        String[] b = Black().split(",");
        ArrayList<String> output = new ArrayList<String>();
        String tmp = "";
        boolean occupied = false;
        String turn = "";
        
        for (String row : yArr)
        {
            tmp = "";

            for (String col : xArr)
            {
                occupied = false;

                for (String wPc : w)
                {
                    if (wPc.substring(1).equals(col + row))
                    {
                        tmp += wPc.substring(0,1);
                        occupied = true;
                        break;
                    }
                }
                for (String bPc : b)
                {
                    if (bPc.substring(1).equals(col + row))
                    {
                        tmp += bPc.substring(0,1);
                        occupied = true;
                        break;
                    }
                }

                if (!occupied) tmp += ".";
            }

            output.add(tmp);
        }

        if (BlackTurn())
        {
            turn = "Black's turn";
        }
        else
        {
            turn = "White's turn";
        }

        output.add(turn);

        return output;
    };

    public static ArrayList<String> Move(String cmd)
    {
        ArrayList<String> output = new ArrayList<String>();
        String[] tmp = cmd.split("-");
        
        if (tmp.length == 2)
        {
            boolean validMove = false;
            String start = tmp[0].substring(1);
            String end = tmp[1];
            String piece = tmp[0].substring(0, 1);
            boolean isBlack = piece.equals(piece.toLowerCase());

            Functions.Output("Start: " + start);
            Functions.Output("End: " + end);

            if (BlackTurn() != isBlack)
            {
                if (BlackTurn())
                {
                    output.add("it's Black's turn");
                }
                else
                {
                    output.add("it's White's turn");
                }
                return output;
            }

            validMove = ValidMove(piece, start, end);

            if (validMove)
            {
                // MovePiece(isBlack, piece, start, end);
                if (!BlackTurn())
                {
                    BlackTurn(true);
                }
                else
                {
                    BlackTurn(false);
                }
                output = DrawBoard();
            }
            else
            {
                output.add("Invalid move");
            }
        }

        return output;
    };

    // Ensure the move is valid
    public static boolean ValidMove(String piece, String start, String end)
    {
        boolean isValid = true;
        String[] xArr = X().split(",");
        ArrayList<Integer> startXY = Notation_XY(start);
        ArrayList<Integer> endXY = Notation_XY(end);
        int startX = startXY.get(0);
        int endX = endXY.get(0);
        int startY = startXY.get(1);
        int endY = endXY.get(1);
        boolean isBlack = piece.equals(piece.toLowerCase());

        if (isValid)
        {
            switch (piece.toLowerCase())
            {
                // pawn
                case "p":
                    isValid = ValidMove_Pawn(isBlack, startX, startY, endX, endY);
                    break;
            }
        }

        return isValid;
    };

    // Move the piece
    public static void MovePiece(boolean isBlack, String piece, String start, String end)
    {
        if (isBlack)
        {
            Black(Black().replaceAll(piece + start, piece + end));
        }
        else
        {
            White(White().replaceAll(piece + start, piece + end));
        }
    };
    public static void WalkMove_RightAngle_Pawn(int startX, int startY, int endX, int endY)
    {
        
    };



    // Valid Move Checks
    public static boolean ValidMove_Pawn(boolean isBlack, int startX, int startY, int endX, int endY)
    {
        boolean isValid = true;
        String occ = Occupying_Color(endX, endY);

        Functions.Output("End X: " + endX);

        // Forward move
        if (isBlack && (startY - endY) < 0) isValid = false;
        if (!isBlack && (startY - endY) >= 0) isValid = false;
        // Vertical move
        if (Is_RightAngle(startX, startY, endX, endY))
        {
            // Distance
            if (startY == 7 || startY == 2)
            {
                // Hasn't moved yet
                if (Move_Distance(startX, startY, endX, endY) > 2) isValid = false;
            }
            else
            {
                if (Move_Distance(startX, startY, endX, endY) > 1) isValid = false;
            }

            // Occupied
            if (occ != "") isValid = false;
        }
        // Diagonal move / If success, capture
        if (Is_Diagonal(startX, startY, endX, endY))
        {
            Functions.Output("Diagonal move");

            // Distance
            if (Move_Distance(startX, startY, endX, endY) > 1) isValid = false;

            // Occupied by self
            if (isBlack && occ.equals("bl")) isValid = false;
            if (!isBlack && occ.equals("wh")) isValid = false;

            Functions.Output("Occ: " + occ);

            if (isValid && !occ.equals(""))
            {
                Functions.Output("Taking piece...");
                // Capture
                TakePiece(isBlack, endX, endY);
            }
        }

        // Check check
        // TODO: Write the 'in check' method

        return isValid;
    };



    // Right angle check
    public static boolean Is_RightAngle(int startX, int startY, int endX, int endY)
    {
        boolean isRight = true;

        if (startX != endX && startY != endY) isRight = false;

        return isRight;
    };

    // Diagonal check
    public static boolean Is_Diagonal(int startX, int startY, int endX, int endY)
    {
        boolean isDiag = true;

        if (
            Math.abs(startX - endX) !=
            Math.abs(startY - endY)
        ) isDiag = false;

        return isDiag;
    };

    // Knight move check
    public static boolean Is_KnightMove(int startX, int startY, int endX, int endY)
    {
        boolean isKnMove = true;

        if (
            (
                Math.abs(startX - endX) == 2 &&
                Math.abs(startY - endY) != 3
            ) ||
            (
                Math.abs(startX - endX) == 3 &&
                Math.abs(startY - endY) != 2
            )
        ) isKnMove = false;

        return isKnMove;
    }

    // Distance
    public static int Move_Distance(int startX, int startY, int endX, int endY)
    {
        // Assumes a diagonal or right move has been verified
        int distance = 0;

        if (Is_Diagonal(startX, startY, endX, endY))
        {
            distance = Math.abs(startX - endX);
        }
        else
        {
            distance = Math.abs(startX - endX) + Math.abs(startY - endY);
        }

        return distance;
    };

    // Occupying Color
    public static String Occupying_Color(int x, int y)
    {
        String pos = XY_Notation(x, y);
        String occ = "";
        String[] blArr = Black().split(",");
        String[] whArr = White().split(",");

        for (String p : blArr)
        {
            if (p.substring(1).equalsIgnoreCase(pos)) occ = "bl";
        }
        for (String p : whArr)
        {
            if (p.substring(1).equalsIgnoreCase(pos)) occ = "wh";
        }

        return occ;
    };

    // Take piece
    public static void TakePiece(boolean isBlack, int x, int y)
    {
        Functions.Output("Taking piece...");

        String[] blArr = Black().split(",");
        String[] whArr = White().split(",");
        String locNot = XY_Notation(x, y);
        String pNot = "";

        if (isBlack)
        {
            for (String p : blArr)
            {
                if (p.substring(1).equalsIgnoreCase(locNot))
                {
                    pNot = p;
                    break;
                }
            }

            Black(Black().replaceAll(pNot, ""));
            blArr = Black().split(",");
            Black(ListCleanup(blArr));
        }
        else
        {
            for (String p : whArr)
            {
                if (p.substring(1).equalsIgnoreCase(locNot))
                {
                    pNot = p;
                    break;
                }
            }

            White(White().replaceAll(pNot, ""));
            whArr = White().split(",");
            White(ListCleanup(whArr));
        }
    };

    // X, Y to position notation
    public static String XY_Notation(int x, int y)
    {
        String[] xArr = X().split(",");
        String[] yArr = Y().split(",");
        String pos = "";

        pos = xArr[x] + yArr[8 - y];

        return pos;
    };
    // Position notation to X, Y
    public static ArrayList<Integer> Notation_XY(String notation)
    {
        if (notation.length() > 2) notation = notation.substring(1); // There is a piece in there
        String x = notation.substring(0,1);
        String y = notation.substring(1);
        String[] xArr = X().split(",");
        String[] yArr = Y().split(",");
        ArrayList<Integer> output = new ArrayList<Integer>();

        for (int i = 0; i < xArr.length; i++)
        {
            if (x.equalsIgnoreCase(xArr[i]))
            {
                output.add(i);
                break;
            }
        }
        for (int i = 0; i < yArr.length; i++)
        {
            if (y.equalsIgnoreCase(yArr[i]))
            {
                output.add(Integer.parseInt(yArr[i]));
                break;
            }
        }

        return output;
    };

    public static String ListCleanup(String[] lst)
    {
        String newList = "";

        for (String itm : lst)
        {
            if (!itm.equals(""))
            {
                if (!newList.equals("")) newList += ",";
                newList += itm;
            }
        }

        return newList;
    };



    /*    
        ------------------------------------------------------------------------
        Property GET/SET.
    */
    public static String X() { return x; }
    public static String Y() { return y; }
    public static void White(String value) { white = value; }
    public static String White() { return white; }
    public static void Black(String value) { black = value; }
    public static String Black() { return black; }

    public static void BlackTurn(boolean value) { blackTurn = value; }
    public static boolean BlackTurn() { return blackTurn; }    
};
