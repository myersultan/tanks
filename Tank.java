package day4.tanks;

import java.util.Random;

public class Tank {

    private int speed = 10;

    private int x;
    private int y;
    private int direction;

    private int nearCornerX = 0;
    private int nearcornerY = 0;

    private ActionField af;
    private BattleField bf;

    public Tank(ActionField af, BattleField bf){
        this(af, bf, 128, 512, 1);
    }

    public Tank(ActionField af, BattleField bf, int x, int y, int direction){
        this.af = af;
        this.bf = bf;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void turn(int direction) throws Exception{
        this.direction = direction;
        af.processTurn(this);
    }

    public void move() throws Exception{
        af.processMove(this);
    }

    public void fire() throws Exception{
        Bullet bullet = new Bullet((x + 25), (y + 25), direction);
        af.processFire(bullet);
    }

    public void moveRandom() throws Exception{
        Random r = new Random();
        int i;
        while (true){
            i = r.nextInt(5);
            if(i>0){
                direction = i;
                af.processTurn(this);
                af.processMove(this);
            }
        }
    }

    public void moveToQuadrant(int v, int h) throws Exception{
        String coordinates = af.getQuadrantXY(v, h);
        int separator = coordinates.indexOf("_");
        int y = Integer.parseInt(coordinates.substring(0, separator));
        int x = Integer.parseInt(coordinates.substring(separator + 1));

        if (this.x < x) {
            while (this.x != x) {
                turn(4);
                fire();
                af.processMove(this);
            }
        } else {
            while (this.x != x) {
                turn(3);
                fire();
                af.processMove(this);
            }
        }

        if (this.y < y) {
            while (this.y != y) {
                turn(2);
                fire();
                af.processMove(this);
            }
        } else {
            while (this.y != y) {
                turn(1);
                fire();
                af.processMove(this);
            }
        }
    }

    public void clean() throws Exception{


         int posX = x/64;
         int posY = y/64;

        System.out.println("Tank position [ " + posX + ", " + posY + " ]");
        nearestCorner(posX, posY);
        System.out.println("Nearest corner position [ " + nearCornerX + ", " + nearcornerY + " ]");

        int checkX = posX;
        int checkY = posY;
        if (direction == 1) {
            checkY--;
        } else if (direction == 2) {
            checkY++;
        } else if (direction == 3) {
            checkX--;
        } else if (direction == 4) {
            checkX++;
        }
        System.out.println("Checks " + checkX + " " + checkY);

        fire();
        fire();
        while (posX != nearCornerX || posY != nearcornerY){
            if(posX < nearCornerX){
                direction = 4;
                move();
            } else if (posX > nearCornerX){
                direction = 3;
                move();
            }else {
                if(posY < nearcornerY){
                    direction = 2;
                    move();
                }else if (posY > nearcornerY){
                    direction = 1;
                    move();
                }
            }
        }

        if (bf.battleField[checkY][checkX].equals("B")) {
            fire();
        }
        int tankMovedir = 0;
        int tankTurn = 0;
        if((posX == 0 && posY != 0) || (posX !=0 && posY != 0)){
           tankTurn = 1;
        }else {
           tankTurn = 2;
        }
        if((posY == 0 && posX != 0) || (posY !=0 && posX != 0)){
            tankMovedir = 3;
        }else {
            tankMovedir = 4;
        }

        while (true){
          turn(tankTurn);
            if (posY == 0){
                for (int i = 0; i < bf.battleField.length; i++) {
                   if(bf.battleField[i][posX].equals("B")){
                      // Bullet bul = new Bullet(x+25, y+25, direction);
                       fire();
                   }
                }
            }else {
                for (int i = bf.battleField.length - 1; i >= 0; i--) {
                    if(bf.battleField[i][posX].equals("B")){
                        fire();
                    }
                }
            }

            move();
        }

    }

    private void nearestCorner(int posX, int posY) throws Exception{
        if(posY > bf.battleField.length / 2){
            nearcornerY = bf.battleField.length - 1;
        }else {
            nearcornerY = 0;
        }

        if(posX > bf.battleField[0].length / 2){
            nearCornerX = bf.battleField[0].length - 1;
        }else {
            nearCornerX = 0;
        }


    }

    public void updateX(int x){
        this.x += x;
    }

    public void updateY(int y){
        this.y += y;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }
}
