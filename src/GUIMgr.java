import java.util.Objects;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class GUIMgr
{
    public static final int INTR_RGN_LEFT = 291, INTR_RGN_TOP = 56, INTR_RGN_BOTTOM = 235, INTR_RGN_RIGHT = 610;
    private static final String ASSET_FOLDER_PATH = "./assets/";
    private static final Font DEFAULT_FONT = Font.font("Consolas", 12), LARGER_FONT = Font.font("Consolas", 18), LARGEST_FONT = Font.font("Consolas", 42);

    private final GraphicsContext gc;

    public GUIMgr(GraphicsContext gc)
    {
        this.gc = gc;
        gc.setFont(DEFAULT_FONT);
    }

    public void drawHUD()
    {
        // Also acts as a way to clear the screen!
        gc.drawImage(new Image("./assets/GUI-Background.bmp", 640, 480, false, false), 0, 0);
    }

    public void drawRoomImage()
    {
        gc.drawImage(new Image(getRoomFileURL(GameMgr.getRoomOfPlayer()), 320, 180, false, false), INTR_RGN_LEFT, INTR_RGN_TOP);
    }

    private static String getRoomFileURL(Room room)
    {
        return ASSET_FOLDER_PATH + room.name().replace(' ', '-').toLowerCase() + ".bmp";
    }

    public void drawInventory()
    {
        gc.setFill(Color.WHITE);
        gc.setFont(LARGER_FONT);
        gc.fillText(">>> INVENTORY <<<", 20, 40);
        gc.setFont(DEFAULT_FONT);
        String inventoryString = "> " + String.join("\n> ", Player.getInventory());
        gc.fillText(inventoryString, 20, 60);
    }

    public void drawRoomDescription()
    {
        gc.setFill(Color.WHITE);
        Room currRoom = GameMgr.getRoomOfPlayer();
        gc.setFont(LARGER_FONT);
        gc.fillText("~ " + currRoom.name() + " ~", 434, 368);
        gc.setFont(DEFAULT_FONT);
        gc.fillText("> " + currRoom.description(), 209, 402);
    }

    public void drawClock()
    {
        int tMinusSecs = 180 - GameMgr.getGameTime();
        int mins = tMinusSecs / 60;
        int secs = tMinusSecs % 60;
        gc.setFill(Color.WHITE);
        gc.setFont(LARGEST_FONT);
        gc.fillText(mins + ":" + secs, 230, 338);
    }
}
