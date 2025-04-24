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
	public static void main(String[] args)
	{
		System.out.println("Program start.");

		// Init game logic classes
		GameMgr gameMgr = GameMgr.getInstance();
		Player player = Player.getInstance();

		// Startup JavaFX GUI
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("ChronomalyGame");
		Group root = new Group();
		Canvas canvas = new Canvas(640, 480);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Render map view
		drawMapGrid(gc);
		// Render doors...
		// TODO add further rendering layers

		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		scene.setOnKeyPressed(Program::keypressEventHandler);
		scene.setOnMouseReleased(Program::mouseEventHandler);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	private void drawMapGrid(GraphicsContext gc)
	{
		ImageView imgView = new ImageView(new Image("./testimg.jpg", 400, 400, false, true));
		imgView.setRotate(90);
		gc.drawImage(imgView.snapshot(new SnapshotParameters(), null), 10, 10);
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
