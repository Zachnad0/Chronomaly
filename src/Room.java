import java.util.Arrays;

public final class Room
{
    private String name, description;
    private Integer destructTime;
    private Boolean[] doors;
    private String[] requiredKeys;
    private Integer[] wallBreakTimes;
    private Interactable[] interactables;

    public Room(RoomData data)
    {
        name = data.name();
        description = data.description();
        destructTime = data.destructTime();
        doors = data.doors();
        requiredKeys = data.doorKeys();
        wallBreakTimes = data.wallBreakTimes();
        if (data.interactables() != null)
        {
            interactables = Arrays.stream(data.interactables()).map(Interactable::new).toList().toArray(new Interactable[0]);
        }
        else
            interactables = new Interactable[0];
    }
}
