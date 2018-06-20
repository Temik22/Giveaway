package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

public class Main extends Application {

    static final int TILE_SIZE = 100;

    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;

    private static Tile[][] gameData = new Tile[WIDTH][HEIGHT];

    private boolean turn = true; // blue = t; red = f

    private Piece toEat = null;
    private short redNumber = 12, blueNumber = 12;

    private TextArea text = new TextArea();

    private Pane root = new Pane();
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    private Group textGroup = new Group();



    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(paint());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent paint() {
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE + 50);
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                gameData[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = placePiece(x, y, Piece.RED);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = placePiece(x, y, Piece.BLUE);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }

        root.getChildren().addAll(textGroup);
        text.setPrefHeight(50);
        text.setPrefWidth(800);
        text.setLayoutX(0);
        text.setLayoutY(800);
        text.setFocusTraversable(false);
        textGroup.getChildren().add(text);
        text.setFont(Font.font(20));
        text.setText("Welcome, players. Blue moves first");
        return root;
    }

    private Piece placePiece(int x, int y, int team) {
        Piece piece = new Piece(x, y, team, false);

        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());

            MoveResult result;

            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveResult(MoveType.NONE);
            } else {
                result = toMove(piece, newX, newY);
            }

            int x0 = toBoard(piece.getCurX());
            int y0 = toBoard(piece.getCurY());

            switch (result.getType()) {
                case NONE:
                    piece.abortMove();
                    break;
                case NORMAL:
                    piece.move(newX, newY);
                    gameData[x0][y0].setPiece(null);
                    if (piece.getTeam() == Piece.RED && newY == 7)
                        piece.makeKing();
                    else if (piece.getTeam() == Piece.BLUE && newY == 0)
                        piece.makeKing();
                    gameData[newX][newY].setPiece(piece);
                    break;
                case KILL:
                    piece.move(newX, newY);
                    gameData[x0][y0].setPiece(null);

                    if (piece.getTeam() == Piece.RED && newY == 7)
                        piece.makeKing();
                    else if (piece.getTeam() == Piece.BLUE && newY == 0)
                        piece.makeKing();

                    gameData[newX][newY].setPiece(piece);
                    Piece otherPiece = result.getPiece();
                    if (otherPiece.getTeam() == Piece.RED)
                        redNumber--;
                    if (otherPiece.getTeam() == Piece.BLUE)
                        blueNumber--;
                    gameData[toBoard(otherPiece.getCurX())][toBoard(otherPiece.getCurY())].setPiece(null);
                    pieceGroup.getChildren().remove(otherPiece);
                    haveWinner();
                    break;
            }
        });
        return piece;
    }

    private boolean findExtra(int newX, int newY, int team) {
            if (isLegalPos(newX - 1, newY + 1) && isLegalPos(newX - 2, newY + 2)) {
                if (gameData[newX - 1][newY + 1].havePiece() && team != gameData[newX - 1][newY + 1].getPiece().getTeam()
                        && !gameData[newX - 2][newY + 2].havePiece())
                    return true;
            }
            if (isLegalPos(newX + 1, newY + 1) && isLegalPos(newX + 2, newY + 2)) {
                if (gameData[newX + 1][newY + 1].havePiece() && team != gameData[newX + 1][newY + 1].getPiece().getTeam()
                        && !gameData[newX + 2][newY + 2].havePiece())
                    return true;
            }

            if (isLegalPos(newX - 1, newY - 1) && isLegalPos(newX - 2, newY - 2)) {
                if (gameData[newX - 1][newY - 1].havePiece() && team != gameData[newX - 1][newY - 1].getPiece().getTeam()
                        && !gameData[newX - 2][newY - 2].havePiece())
                    return true;
            }
            if (isLegalPos(newX + 1, newY - 1) && isLegalPos(newX + 2, newY - 2)) {
                if (gameData[newX + 1][newY - 1].havePiece() && team != gameData[newX + 1][newY - 1].getPiece().getTeam()
                        && !gameData[newX + 2][newY - 2].havePiece())
                    return true;
            }
        return false;
    }

    private boolean checkExtra() {
        int team;
        if (turn)
            team = Piece.BLUE;
        else team = Piece.RED;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (gameData[i][j].havePiece() && gameData[i][j].getPiece().getTeam() == team)
                    if (findExtra(i, j, gameData[i][j].getPiece().getTeam()))
                        return true;
            }
        }
        return false;
    }

    private MoveResult toMove(Piece piece, int newX, int newY) {
        if (gameData[newX][newY].havePiece() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }
        if (toEat != null && toEat != piece) {
            return new MoveResult(MoveType.NONE);
        }
        if (piece.getTeam() == Piece.RED && turn) {
            return new MoveResult(MoveType.NONE);
        }
        if (piece.getTeam() == Piece.BLUE && !turn) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(piece.getCurX());
        int y0 = toBoard(piece.getCurY());

        if (piece.isKing) {
            if (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) == 1) {
                if (checkExtra())
                    return new MoveResult(MoveType.NONE);
                setTurn(piece.getTeam());
                return new MoveResult(MoveType.NORMAL);
            }
            if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == 2) {
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;
                if (gameData[x1][y1].havePiece() && gameData[x1][y1].getPiece().getTeam() != piece.getTeam()) {
                    if (!findExtra(newX, newY, piece.getTeam()))
                        setTurn(piece.getTeam());
                    return new MoveResult(MoveType.KILL, gameData[x1][y1].getPiece());
                }
            }
        }

        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getTeam()) {
            if (checkExtra())
                return new MoveResult(MoveType.NONE);
            setTurn(piece.getTeam());
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == 2) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (gameData[x1][y1].havePiece() && gameData[x1][y1].getPiece().getTeam() != piece.getTeam()) {
                if (!findExtra(newX, newY, piece.getTeam())) {
                    toEat = null;
                    setTurn(piece.getTeam());
                } else {
                    toEat = piece;
                }
                return new MoveResult(MoveType.KILL, gameData[x1][y1].getPiece());
            }
        }
        return new MoveResult(MoveType.NONE);
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void reset() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (gameData[j][i].havePiece())
                    pieceGroup.getChildren().remove(gameData[j][i].getPiece());
                tileGroup.getChildren().remove(gameData[j][i]);
                gameData[j][i] = null;
            }
        }
        textGroup.getChildren().remove(text);
        root.getChildren().removeAll(tileGroup, pieceGroup, textGroup);
    }

    private void haveWinner() {
        if (blueNumber == 0) {
            text.setText("Blue has won");
            reset();
            redNumber = 12;
            blueNumber = 12;
            turn = true;
            paint();
        }
        if (redNumber == 0) {
            text.setText("Red has won");
            reset();
            redNumber = 12;
            blueNumber = 12;
            turn = true;
            paint();
        }
    }

    private boolean isLegalPos(int newX, int newY) {
        return newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7;
    }

    private void setTurn(int team) {
        if (team == Piece.RED) {
            text.setText("Blue's turn");
            turn = true;
            return;
        }
        text.setText("Red's turn");
        turn = false;
    }

    private int toBoard(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

}
