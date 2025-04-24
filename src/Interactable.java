import java.util.*;
import java.util.stream.*;

public final class Interactable
{
    private static final List<Interactable> allInstances = new ArrayList<>();
    private final Integer posX, posY;
    private final String name;
    private final Room roomLoc;

    public Interactable(InteractableData interactableData, Room room)
    {
        Objects.requireNonNull(interactableData);
        Objects.requireNonNull(room);

        name = interactableData.name();
        posX = interactableData.left();
        posY = interactableData.top();
        roomLoc = room;

        allInstances.add(this);
    }

    public boolean isInRoom(Room room)
    {
        return room != null && roomLoc.equals(room);
    }

    private static int closerToPoint(Interactable i1, Interactable i2, final int x, final int y)
    {
        return (int)Math.signum(Math.hypot(x - i1.posX, y - i1.posY) - Math.hypot(x - i2.posX, y - i2.posY));
    }

    public static Optional<String> tryGetAtPosInRoom(final int x, final int y, final double clickRad, Room currRoom)
    {
        return allInstances.stream()
                // In room and within radius
                .filter(i -> i.isInRoom(currRoom) && Math.hypot(x - i.posX, y - i.posY) <= clickRad)
                // Closest to point
                .min((i1, i2) -> closerToPoint(i1, i2, x, y))
                // Get name
                .map(i -> i.name);
    }
}
