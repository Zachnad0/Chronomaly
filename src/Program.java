import java.util.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
		DrawMapGrid(gc);
		// Render doors...

		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		scene.setOnKeyPressed(Program::KeypressEventHandler);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	private void DrawMapGrid(GraphicsContext gc)
	{
		ImageView imgView = new ImageView(new Image("./testimg.jpg", 400, 400, false, true));
		imgView.setRotate(90);
		gc.drawImage(imgView.snapshot(new SnapshotParameters(), null), 10, 10);
	}

	private static void KeypressEventHandler(KeyEvent keyEvent)
	{
		// TODO all keybinds
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
}
