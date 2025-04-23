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
    private static final int MAX_X = 6, MAX_Y = 6;
    private static final String ROOMS_JSON_FILENAME = "./bin/LEVELDATA.json";
    private static GameMgr instance = null;

    private final Room[][] rooms;

    private GameMgr()
    {
        rooms = loadRooms();
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

        /*
        Room[][] result = new Room[MAX_X + 1][];
        for (int x = 0; x <= MAX_X; x++){
            result[x] = new Room[MAX_Y + 1];
            for (int y = 0; y <= MAX_Y;y++)
            {
        
            }
        }*/

        // return result;
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
            return GameMgr.getInstance().rooms[x][y];
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException(String.format("Room not at coordinates (%d, %d)", x, y));
        }
    }
}
