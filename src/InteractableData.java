import java.util.Objects;

public final record InteractableData(String name, Integer left, Integer top)
{
    public InteractableData
    {
        // If the object is specifed in the JSON, then it must specify contain all fields
        assert name != null : "InteractableData name is null!";
        assert left != null : "InteractableData left is null!";
        assert top != null : "InteractableData top is null!";
    }
}
