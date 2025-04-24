import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public final class GameMgr
{
    private static final int MAX_X = 6, MAX_Y = 6, CLICK_RADIUS = 6;
    private static final String ROOMS_JSON_FILENAME = "./bin/LEVELDATA.json";
    private static GameMgr instance = null;

    private final Room[][] rooms;
    private final long gameStartUnixTime;
    private GameState gameState;
    private boolean mRoomADone, mRoomBDone, mRoomCDone;

    private GameMgr()
    {
        rooms = loadRooms();
        gameStartUnixTime = System.currentTimeMillis() / 1000;
        gameState = GameState.WANDERING;
        mRoomADone = mRoomBDone = mRoomCDone = false;
    }

    private Room[][] loadRooms()
    {
        Room[][] result = new Room[MAX_X + 1][MAX_Y + 1];
        try
        {
            Gson gson = new Gson();
            // System.out.println(System.getProperty("user.dir"));
            Path fPath = Path.of(ROOMS_JSON_FILENAME);
            if (!Files.exists(fPath))
                throw new IOException();
            String jsonContents = Files.readString(fPath);
            RoomData[][] data = gson.fromJson(jsonContents, RoomData[][].class);
            for (int x = 0; x < data.length; x++)
            {
                for (int y = 0; y < data[x].length; y++)
                {
                    result[x][y] = new Room(data[x][y]);
                }
            }
        }
        catch (InvalidPathException e)
        {
            System.out.println("Rooms JSON path is invalid!");
            throw e;
        }
        catch (IOException e)
        {
            System.out.println("Can't read string of JSON file");
            throw new RuntimeException();
        }
        catch (JsonSyntaxException e)
        {
            System.out.println("JSON syntax is invalid!");
            throw e;
        }

        return result;
    }

    public static GameMgr getInstance()
    {
        if (instance == null)
            instance = new GameMgr();
        return instance;
    }

    public static void resetInstance()
    {
        instance = new GameMgr();
    }

    public static Room getRoomAt(int x, int y)
    {
        try
        {
            return getInstance().rooms[x][y];
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException(String.format("No room at coordinates (%d, %d)", x, y));
        }
    }

    public static Room getRoomOfPlayer()
    {
        return getRoomAt(Player.posX(), Player.posY());
    }

    public static void doClickAt(final int sceneX, final int sceneY)
    {
        if (sceneX < GUIMgr.INTR_RGN_LEFT || sceneX > GUIMgr.INTR_RGN_RIGHT || sceneY < GUIMgr.INTR_RGN_TOP || sceneY > GUIMgr.INTR_RGN_BOTTOM)
            return;

        System.out.println("Click is within interactable region");
        int localX = sceneX - GUIMgr.INTR_RGN_LEFT, localY = sceneY - GUIMgr.INTR_RGN_TOP;
        Interactable.tryGetAtPosInRoom(localX, localY, CLICK_RADIUS, getRoomOfPlayer()).ifPresent(GameMgr::doInteraction);
    }

    public static void doInteraction(String interactable)
    {
        System.out.println(String.format("Interacting with \"%s\"", interactable));
        // TODO implement interactable stuff depending on situation...
    }

    public static boolean canMakeMove(final int fromX, final int fromY, final int dirX, final int dirY)
    {
        // Validity checks
        if (dirX == dirY || (dirX != 0 && dirY != 0))
            throw new IllegalArgumentException();
        int nextX = fromX + dirX, nextY = fromY + dirY;
        if (!getInstance().gameState.equals(GameState.WANDERING) || nextX < 0 || nextX > MAX_X || nextY < 0 || nextY > MAX_Y)
            return false;

        Room currRoom = getRoomAt(fromX, fromY);
        Room nextRoom = getRoomAt(nextX, nextY);

        int currTime = getGameTime();
        return currRoom.canAccessInDir(dirX, dirY, currTime) && nextRoom.destructTime() > getGameTime();
    }

    /** Gives current time through game in seconds. Returns -1 if game has ended. */
    public static int getGameTime()
    {
        if (!List.of(GameState.IN_PUZZLE, GameState.WANDERING).contains(getInstance().gameState))
            return -1;
        long currUnixTime = System.currentTimeMillis() / 1000;
        return (int)(currUnixTime - getInstance().gameStartUnixTime);
    }

    public static GameState gameState()
    {
        return getInstance().gameState;
    }

    public static void checkForEndgameStates()
    {
        if (getInstance().mRoomADone && getInstance().mRoomBDone && getInstance().mRoomCDone && getRoomOfPlayer().name().equals("Chrono Chamber"))
        {
            getInstance().gameState = GameState.WIN;
            return;
        }

        if (getGameTime() >= Math.min(getRoomOfPlayer().destructTime(), 180) || getGameTime() == -1)
        {
            getInstance().gameState = GameState.DEATH;
            return;
        }
        // TODO further states
    }
}
