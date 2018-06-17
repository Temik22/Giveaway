package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Piece piece;

   public void setPiece(Piece piece){
        this.piece = piece;
   }

   public Piece getPiece(){
        return piece;
   }

   public boolean havePiece(){
       return piece != null;
   }

   public Tile(boolean light, int x, int y){
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        relocate(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
        setFill(light ? Color.valueOf("#bababa") : Color.valueOf("#636363"));
   }
}
