package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


class Piece extends StackPane {

    private double mouseX, mouseY, curX, curY;
    private int team; // red = t;  blue = f;
    boolean isKing;
    private Ellipse ellipse;
    static final int RED = 1, BLUE = -1;

    Piece(int x, int y, int team, boolean isKing) {
        this.isKing = isKing;
        this.team = team;
        move(x, y);

        ellipse = new Ellipse();
        ellipse.setCenterX(Main.TILE_SIZE * x - Main.TILE_SIZE / 2);
        ellipse.setCenterY(Main.TILE_SIZE * y - Main.TILE_SIZE / 2);
        ellipse.setRadiusX(Main.TILE_SIZE / 2);
        ellipse.setRadiusY(Main.TILE_SIZE / 2);
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

        setOnMouseReleased(e -> {
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + curX, e.getSceneY() - mouseY + curY);
        });
    }

    void move(int x, int y) {
        curX = x * Main.TILE_SIZE;
        curY = y * Main.TILE_SIZE;
        relocate(curX, curY);
    }

    void abortMove() {
        relocate(curX, curY);
    }

    void makeKing() {
        isKing = true;
        ellipse.setFill(team == RED ? Color.valueOf("#ff0000") : Color.valueOf("#0000ff"));
    }

    double getCurX() {
        return curX;
    }

    double getCurY() {
        return curY;
    }

    int getTeam() {
        return team;
    }

}
