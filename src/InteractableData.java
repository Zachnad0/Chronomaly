import java.util.Objects;

public final record InteractableData(String name, Integer left, Integer top)
{
    public InteractableData
    {
        // If the object is specifed in the JSON, then it must specify contain all fields
        Objects.requireNonNull(name);
        Objects.requireNonNull(left);
        Objects.requireNonNull(top);
    }
}
