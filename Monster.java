package hello;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Monster {

    int x;
    int y;
    int tileX;
    int tileY;
    int prevTileX;
    int PrevTileY;
    int timer ;
    boolean alive;
    int direction;
    int speed;
    boolean[] available;
    int playerDirection;
    int sight;
    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int STUCK = 4;
    Sprite monsterSprite;
    public int bounceRatio = 1;
    private Random rand = new Random();

    public Monster(int tileX, int tileY, int sight) throws IOException {
        this.tileX = tileX;
        this.tileY = tileY;
        this.prevTileX = tileX;
        this.PrevTileY = tileY;
        this.sight = sight;
        this.alive = true;
        this.direction = STUCK;
        this.speed = 1 ;
        this.timer=0;
        
       // LevelGenerator.printMap(Game.map);
//        System.out.println(tileX+" "+tileY);
        Image monImage = Image.createImage("/MonstersAll.PNG");
        monsterSprite = new Sprite(monImage, monImage.getHeight(), monImage.getHeight());
        int[] seq = {0, 1, 2, 3};
        monsterSprite.setFrameSequence(seq);
        monsterSprite.setPosition(tileY*32, tileX*32);
    }

    private int tileInDirection(int direction, int distance) {
//        System.out.println("tileX: "+tileX+" tileY: "+tileY + " Direction" + direction); 
        if ((tileY - distance) >= 0 && direction == LEFT) {
            return Game.map[tileX][tileY - distance];
        }
        if ((tileX - distance) >= 0 && direction == UP) {
            return Game.map[tileX - distance][tileY];
        }
        if ((tileY + distance) < Game.map[tileX].length && direction == RIGHT) {
            return Game.map[tileX ][tileY+ distance];
        }
        if ((tileX + distance) < Game.map.length && direction == DOWN) {
            return Game.map[tileX+ distance][tileY ];
        }
        return EquationGenerator.ERROR;

    }

    public boolean isStuck() {
        boolean isStuck = false;
        for (int i = 0; i < this.available.length; i++) {
            isStuck = isStuck || this.available[i];
        }
        return !isStuck;
    }

    private boolean[] getAvailableDirections() {
        boolean[] ways = new boolean[4];
        this.playerDirection = -1;
        if (tileInDirection(LEFT, 1) == MapGenerator.EMPTY || tileInDirection(LEFT, 1) == MapGenerator.FIRE) {
            ways[LEFT] = true;
//            System.out.println("LEFT"+tileInDirection(LEFT, 1));
        }
        if (tileInDirection(UP, 1) == MapGenerator.EMPTY || tileInDirection(UP, 1) == MapGenerator.FIRE) {
            ways[UP] = true;
//            System.out.println("UP"+tileInDirection(UP, 1));
        }
        if (tileInDirection(RIGHT, 1) == MapGenerator.EMPTY || tileInDirection(RIGHT, 1) == MapGenerator.FIRE) {
            ways[RIGHT] = true;
//            System.out.println("Right"+tileInDirection(RIGHT, 1));
        }
        if (tileInDirection(DOWN, 1) == MapGenerator.EMPTY || tileInDirection(DOWN, 1) == MapGenerator.FIRE) {
            ways[DOWN] = true;
//            System.out.println("DOWN"+tileInDirection(DOWN, 1));
        }
        for (int i = 1; i < sight; i++) {

            if (tileInDirection(LEFT, i) == EquationGenerator.PLAYER) {
                this.playerDirection = LEFT;
            }
            if (tileInDirection(LEFT, i) != EquationGenerator.EMPTY) {
                break;
            }
        }

        for (int i = 1; i < sight; i++) {

            if (tileInDirection(UP, i) == EquationGenerator.PLAYER) {
                this.playerDirection = UP;
            }
            if (tileInDirection(UP, i) != EquationGenerator.EMPTY) {
                break;
            }
        }
        for (int i = 1; i < sight; i++) {


            if (tileInDirection(RIGHT, i) == EquationGenerator.PLAYER) {
                this.playerDirection = RIGHT;
            }
            if (tileInDirection(RIGHT, i) != EquationGenerator.EMPTY) {
                break;
            }
        }
        for (int i = 1; i < sight; i++) {

            if (tileInDirection(DOWN, i) == EquationGenerator.PLAYER) {
                this.playerDirection = DOWN;
            }
            if (tileInDirection(DOWN, i) != EquationGenerator.EMPTY) {
                break;
            }
        }
        return ways;
    }

    public boolean inTilePlace() {
        return (monsterSprite.getX() == tileY * 32 && monsterSprite.getY() == tileX * 32);
    }

    private int getDirection() {
        if (!inTilePlace()) {
            
            return direction;
        }
        this.available = getAvailableDirections();
//        Game.printMap(Game.map);
//        System.out.println("X:"+tileX+"Y:"+tileY);
//        for (int i = 0; i < 4; i++) {
//            System.out.print(this.available[i] + " ");
//            System.out.print(tileInDirection(i, 1)+" ");
//        }
//        System.out.println();
        if (isStuck()) {
            this.direction = STUCK;
            return STUCK;
        }
        if (this.playerDirection != -1 && this.available[this.playerDirection] && this.direction != STUCK) {
            this.direction = this.playerDirection;
        } else if (this.direction == STUCK ||!this.available[this.direction]) {
                if (this.direction != STUCK && EquationGenerator.nextInt(rand,bounceRatio) > 0) {
                    if (this.direction >= 2) {
                        this.direction -= 2;
                    } else {
                        this.direction += 2;
                    }
                } 
                else {
                    direction = getRandomDirection();
                }
            }
        else{
            if(EquationGenerator.nextInt(rand,bounceRatio+2) > 0)
                direction = getRandomDirection(); 
        }
        return direction;
    }

    private int getRandomDirection() {
        int turnDirection = EquationGenerator.nextInt(rand,4);
        while (!this.available[turnDirection]) {
            turnDirection = EquationGenerator.nextInt(rand,4);
        }
        return turnDirection;
    }
    int try_direc;

    public void destroyed (Vector monesVec)
    {
        timer ++ ;
        if (timer == 1)
            this.monsterSprite.setFrame(2);        
        
        if (timer == 30 )
            this.monsterSprite.setFrame(3);
        
        if (timer == 50 )
        {
//            this.monsterSprite.setFrame(4);
            Game.layerManager.remove(monsterSprite);
//            Game.backGround_layer.setCell(this.tileX, this.tileY, 0);
            this.monsterSprite = null ;
            monesVec.removeElement(this);
        }        
    }
    
    public void check_Move_Dest(Vector monesVec) {
        if (!alive)
        {
            destroyed(monesVec);
            return ;
        }
        
        this.tileX =( monsterSprite.getY()+16) / 32;
        this.tileY = (monsterSprite.getX()+16) / 32;
        if(this.tileX != this.prevTileX || this.tileY != this.PrevTileY){
            Game.map[this.prevTileX][this.PrevTileY] = MapGenerator.EMPTY;
            Game.map[this.tileX][this.tileY] = MapGenerator.MONSTER;
            this.prevTileX = this.tileX;
            this.PrevTileY = this.tileY;
//             System.out.print("MONSTER MAP");
//        Game.printMap(Game.map);
//        System.out.println();
            //MapGenerator.printMap(Game.map);
//			monsterSprite.setPosition(tileY*32, tileX*32);

        }
        try_direc = getDirection();
//     System.out.println("tileX: "+tileX+" tileY: "+tileY + " Direction" + direction); 
//    System.out.println(try_direc);
        switch (try_direc) {
            case STUCK:
                break;
            case UP:
                monsterSprite.setPosition(monsterSprite.getX(), monsterSprite.getY() -speed);
                break;
            case DOWN:
                monsterSprite.setPosition(monsterSprite.getX(), monsterSprite.getY() + speed);
                break;
            case LEFT:
                monsterSprite.setFrame(1);
                monsterSprite.setPosition(monsterSprite.getX() - speed , monsterSprite.getY());
                break;
            case RIGHT:
                monsterSprite.setFrame(0);
                monsterSprite.setPosition(monsterSprite.getX() + speed, monsterSprite.getY());
                break;
        }
    }
}
