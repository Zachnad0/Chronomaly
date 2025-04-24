import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
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

    private GameMgr()
    {
        rooms = loadRooms();
        gameStartUnixTime = System.currentTimeMillis() / 1000;
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

    public static Room getRoom(int x, int y)
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

    public static void doClickAt(final int sceneX, final int sceneY)
    {
        // TODO
    }

    public static boolean canMakeMove(final int fromX, final int fromY, final int dirX, final int dirY)
    {
        // Validity checks
        if (dirX == dirY || (dirX != 0 && dirY != 0))
            throw new IllegalArgumentException();
        int nextX = fromX + dirX, nextY = fromY + dirY;
        if (nextX < 0 || nextX > MAX_X || nextY < 0 || nextY > MAX_Y)
            return false;

        Room currRoom = getRoom(fromX, fromY);
        Room nextRoom = getRoom(nextX, nextY);

        int currTime = GetGameTime();
        return currRoom.canAccessInDir(dirX, dirY, currTime) && nextRoom.canAccessInDir(-dirX, -dirY, currTime);
    }

    public static int GetGameTime()
    {
        long currUnixTime = System.currentTimeMillis() / 1000;
        return (int)(currUnixTime - getInstance().gameStartUnixTime);
    }
}
