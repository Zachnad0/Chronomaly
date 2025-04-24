import java.util.*;

public final class Room
{
    private String name, description;
    private Integer destructTime;
    private Boolean[] doors;
    private String[] requiredKeys;
    private Integer[] wallBreakTimes;
    private Set<Interactable> interactables;

    public Room(RoomData data)
    {
        name = data.name();
        description = data.description();
        destructTime = data.destructTime();
        doors = data.doors();
        requiredKeys = data.doorKeys();
        wallBreakTimes = data.wallBreakTimes();

        // Unpack interactables
        interactables = new HashSet<>();
        if (data.interactables() != null)
        {
            for (InteractableData inData : data.interactables())
            {
                interactables.add(new Interactable(inData, this));
            }
        }
    }

    private static int getIndexFromDir(final int dirX, final int dirY)
    {
        // Are ordered N,E,S,W to 0,1,2,3
        switch ((int)Math.signum(dirX))
        {
        case -1: // Left
            return 3;
        case 1: // Right
            return 1;
        case 0:
            switch ((int)Math.signum(dirY))
            {
            case -1: // Up
                return 0;
            case 1: // Down
                return 2;
            case 0:
                throw new IllegalArgumentException("Cannot get index in no dir!");
            }
            break;
        }

        throw new IndexOutOfBoundsException();
    }

    public boolean canAccessInDir(final int dirX, final int dirY, final int currTime)
    {
        if (dirX == dirY || (dirX != 0 && dirY != 0))
            throw new IllegalArgumentException();

        int doorIndex = getIndexFromDir(dirX, dirY);
        if (doors[doorIndex])
        {
            // If it is a door, then check player has key to enter, if a key is needed
            if (requiredKeys[doorIndex] != null)
                return Player.hasItem(requiredKeys[doorIndex]);
            return true;
        }
        // If not a door, then check if wall has broken
        return wallBreakTimes[doorIndex] == null ? false : currTime >= wallBreakTimes[doorIndex];
    }

    public String getRequiredKeyInDir(final int dirX, final int dirY)
    {
        if (dirX == dirY || (dirX != 0 && dirY != 0))
            throw new IllegalArgumentException();

        return requiredKeys[getIndexFromDir(dirX, dirY)];
    }

    public String name()
    {
        return name;
    }

    public String description()
    {
        return description;
    }

    public int destructTime()
    {
        return destructTime;
    }
}
