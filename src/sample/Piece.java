package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


public class Piece extends StackPane {

    private double mouseX, mouseY, curX, curY;
    private int team; // red = t;  blue = f;
    boolean isKing;
    private Ellipse ellipse;
    public static final int RED = 1, BLUE = -1;

    public Piece(int x, int y, int team, boolean isKing) {
        this.isKing = isKing;
        this.team = team;
        move(x, y);

        ellipse = new Ellipse();
        ellipse.setTranslateX((Main.TILE_SIZE - Main.TILE_SIZE * 0.26 * 2) / 2);
        ellipse.setTranslateY((Main.TILE_SIZE - Main.TILE_SIZE * 0.26 * 2) / 2);
        ellipse.setRadiusX(Main.TILE_SIZE / 4);
        ellipse.setRadiusY(Main.TILE_SIZE / 4);
        ellipse.setStroke(Color.BLACK);


        if (!isKing) {
            ellipse.setFill(team == RED ? Color.valueOf("#d22d2d") : Color.valueOf("#009fdb"));
        } else {
            ellipse.setFill(team == RED ? Color.valueOf("#ff0000") : Color.valueOf("#0000ff"));
        }
        getChildren().addAll(ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseReleased(e ->{});

        setOnMouseDragged(e ->{
            relocate(e.getSceneX() - mouseX + curX, e.getSceneY() - mouseY + curY);
        });
    }

    public void move(int x, int y) {
        curX = x * Main.TILE_SIZE;
        curY = y * Main.TILE_SIZE;
        relocate(curX, curY);
    }

    public void abortMove() {
        relocate(curX, curY);
    }

    public void makeKing(){
        isKing = true;
        ellipse.setFill(team == RED ? Color.valueOf("#ff0000") : Color.valueOf("#0000ff"));
    }

    public double getCurX() {
        return curX;
    }

    public double getCurY() {
        return curY;
    }

    public int getTeam() {
        return team;
    }

}
