import java.util.*;

public final class Player
{
    final int START_X = 3, START_Y = 4, MAX_X = 6, MAX_Y = 6;
    private static Player instance = null;

    private final Set<String> inventory = new HashSet<>();
    private int posX = START_X, posY = START_Y;

    private Player()
    {}

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
        if (GameMgr.canMakeMove(posX, posY, x, 0))
        {
            posX += x;
            System.out.println("    -> true");
            return true;
        }
        System.out.println("    -> false");
        return false;
    }

    /** Returns true if movement successful */
    public boolean tryMoveVert(int y)
    {
        System.out.println("tryMoveVert(" + y + ")");
        if (GameMgr.canMakeMove(posX, posY, 0, y))
        {
            posY += y;
            System.out.println("    -> true");
            return true;
        }
        System.out.println("    -> false");
        return false;
    }

    public static boolean hasItem(String itemName)
    {
        return getInstance().inventory.contains(itemName);
    }

    public static void consumeItem(String itemName)
    {
        if (hasItem(itemName))
            getInstance().inventory.remove(itemName);
    }

    public static int posX()
    {
        return getInstance().posX;
    }

    public static int posY()
    {
        return getInstance().posY;
    }

    public static Set<String> getInventory()
    {
        Set<String> inv = getInstance().inventory;
        if (inv.isEmpty())
            return Set.of("Nothing.", "Still nothing!", "Hmm...");
        return new HashSet<>(inv);
    }
}
