/** Represents a room object for JSON deserialization of the map */
public final record RoomData(String name, String description, int destructTime, Boolean[] doors, String[] doorKeys, Integer[] wallBreakTimes, InteractableData[] interactables)
{}
