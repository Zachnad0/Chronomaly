import java.util.*;

public final class Player
{
    final int START_X = 3, START_Y = 4, MAX_X = 6, MAX_Y = 6;
    private static Player instance = null;

    private final List<String> inventory;
    private int posX = START_X, posY = START_Y;

    private Player()
    {
        inventory = new ArrayList<>();
    }

    public static Player getInstance()
    {
        if (instance == null)
            instance = new Player();
        return instance;
    }

    /** Returns true if movement successful */
    public boolean tryMoveHoriz(int x)
    {
        System.out.println("tryMoveVert(" + x + ")");
        int propX = posX + x;
        if (propX >= 0 && propX <= MAX_X)
        {
            posX = propX;
            return true;
        }
        return false;
    }

    /** Returns true if movement successful */
    public boolean tryMoveVert(int y)
    {
        System.out.println("tryMoveVert(" + y + ")");
        int propY = posY + y;
        if (propY >= 0 && propY <= MAX_X)
        {
            posY = propY;
            return true;
        }
        return false;
    }
}
