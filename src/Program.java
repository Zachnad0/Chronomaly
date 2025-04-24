import java.util.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class Program extends Application
{
	private static GUIMgr guiMgr;
	private static Timer gameLoopTimer;

	public static void main(String[] args)
	{
		System.out.println("Program start.");

		// Init game logic classes
		GameMgr gameMgr = GameMgr.getInstance();
		Player player = Player.getInstance();

		// Startup JavaFX GUI
		launch();
		System.out.println("Fin.");
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("ChronomalyGame");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();

		Group root = new Group();
		Canvas canvas = new Canvas(640, 480);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		guiMgr = new GUIMgr(gc);

		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		scene.setOnKeyPressed(Program::keypressEventHandler);
		scene.setOnMouseReleased(Program::mouseEventHandler);
		primaryStage.setScene(scene);

		primaryStage.show();

		gameTick = 0;
		TimerTask loopTask = new TimerTask()
		{
			@Override
			public void run()
			{
				gameLoop(primaryStage, gc);
			}
		};
		gameLoopTimer = new Timer();
		gameLoopTimer.schedule(loopTask, 250, 250);
	}

	private int gameTick = 0;

	private void gameLoop(Stage gameStage, GraphicsContext gc)
	{
		// System.out.println("Loop tick t=" + gameTick);

		// Check gamestate
		GameMgr.checkForEndgameStates();

		switch (GameMgr.gameState())
		{
		case IN_PUZZLE:
		case WANDERING:
			// Nothing special yet
			break;
		case DEATH:
			// Print death screen, restart game
			gameLoopTimer.cancel();
			gameStage.close();
			return;
		case WIN:
			// Print win screen, end game
			gameLoopTimer.cancel();
			GameMgr.resetInstance();
			gameStage.close();
			return;
		}

		// Draw GUI
		guiMgr.drawHUD();
		guiMgr.drawRoomImage();
		guiMgr.drawInventory();
		guiMgr.drawRoomDescription();
		guiMgr.drawClock();

		gameTick++;
	}

	private static void keypressEventHandler(KeyEvent keyEvent)
	{
		System.out.println(String.format("Keypress: %s", keyEvent.getCode().toString()));
		switch (keyEvent.getCode())
		{
		case UP:
			Player.getInstance().tryMoveVert(-1);
			break;
		case DOWN:
			Player.getInstance().tryMoveVert(1);
			break;
		case LEFT:
			Player.getInstance().tryMoveHoriz(-1);
			break;
		case RIGHT:
			Player.getInstance().tryMoveHoriz(1);
			break;
		default:
			break;
		}
	}

	private static void mouseEventHandler(MouseEvent mouseEvent)
	{
		int mX = (int)mouseEvent.getSceneX(), mY = (int)mouseEvent.getSceneY();
		MouseButton mB = mouseEvent.getButton();
		System.out.println(String.format("MouseClick: %s @ (%d, %d)", mB.toString(), mX, mY));

		// Only detect mouse primary
		if (mB.equals(MouseButton.PRIMARY))
			GameMgr.doClickAt(mX, mY);
	}
}
