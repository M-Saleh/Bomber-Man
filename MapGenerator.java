package hello;

import java.util.Random;

public class MapGenerator {
    
    public static final int WALL = -8;
    public static final int EMPTY  = -1;
    public static final int BOX = -4;
    public static final int MONSTER = -2;
    public static final int PLAYER = -3;
    public static final int BOMB = -5 ;
    public static final int FIRE = -6;
    static  int [][] boxDigits = new int[10][2];
    static  int [][] boxPower = new int[10][2];
    private Random rand = new Random();
        
    public int [][] generateMap(int xsize , int ysize,int monsters, int boxRatioProb,int missingDigit){

        int [][] map = new int [xsize+2][ysize+2];
        for(int i=0;i<(xsize+2);i++)
            for(int j=0;j<(ysize+2);j++)
                map[i][j] = EMPTY;
        //side walls
        for(int i=0; i < (xsize+2); i++){
           
            map[i][0] = WALL;
            map[xsize+1 - i][ysize+1] = WALL;            
        }
        for(int i=0; i < (ysize+2); i++){
         map[0][i] = WALL;
         map[xsize+1][ysize+1 - i] = WALL;
        }
        // in  middle map wall
        for(int i=0;i< (xsize+1);i++){
            for(int j=0 ; j< (ysize+1);j++){
                if(i%2 == 0 && j%2 == 0){
                    map[i][j] = WALL;
                }
            }
        }
        
        // Monsters
         if(monsters > (xsize*ysize)/2)monsters = (xsize*ysize)/2;
        while(monsters > 0){
            int xmrand = EquationGenerator.nextInt(rand,xsize+1);
            int ymrand = EquationGenerator.nextInt(rand,ysize+1);
             if (!(xmrand == 1 &&(ymrand==1 || ymrand==2)) || (xmrand == 2 && ymrand==1)){
                 if(map[xmrand][ymrand] != WALL && map[xmrand][ymrand] != MONSTER){
                     map[xmrand][ymrand] = MONSTER;
                     monsters--;
                 }
             }
        }
        
        //put missingDigit box
        for(int i=0;i<10;i++){
             int mdbrc = EquationGenerator.nextInt(rand,ysize+1);
        int mdbrr = EquationGenerator.nextInt(rand,xsize+1);
        while(map[mdbrr][mdbrc] != EMPTY || mdbrr < 3 || mdbrc < 3){
            mdbrc =EquationGenerator.nextInt(rand,ysize+1);
            mdbrr = EquationGenerator.nextInt(rand,(xsize+1));
        }
        map[mdbrr][mdbrc] = BOX;
        boxDigits[i][0] = mdbrr;
        boxDigits[i][1] = mdbrc;
        }
       

        //put missingDigit box
        for(int i=0;i<10;i++){
        int mdbrc = EquationGenerator.nextInt(rand,ysize+1);
        int mdbrr = EquationGenerator.nextInt(rand,xsize+1);
        while(map[mdbrr][mdbrc] != EMPTY || mdbrr < 3 || mdbrc < 3){
            mdbrc =EquationGenerator.nextInt(rand,ysize+1);
            mdbrr = EquationGenerator.nextInt(rand,(xsize+1));
        }
        map[mdbrr][mdbrc] = BOX;
        boxPower[i][0] = mdbrr;
        boxPower[i][1] = mdbrc;
        }
                        
        //boxes
         
        for(int i = 1;i<(xsize+1) ; i++){
            for(int j=1;j<(ysize+1);j++){
                if(map[i][j] != WALL && map[i][j] != MONSTER && map[i][j] != BOX){   
                    if(EquationGenerator.nextInt(rand,boxRatioProb+1) > 0){
                        {
                            map[i][j] = BOX;
                            
                        }
                        
                    }
                }
            }
        }
        
        map[1][1] = EMPTY;
        map[1][2] = EMPTY;
        map[2][1] = EMPTY;
     //   Game.printMap(map);
     //   Game.printMap(boxDigits);
        return map;
    }
    
    public static void main(String [] args){
       MapGenerator mg = new MapGenerator();
       int [][] map = mg.generateMap(5, 5,30,1,3);
       for(int i=0;i<map.length;i++){
           for(int j=0; j<map[i].length ; j++){
               if(map[i][j] > -1)
                   System.out.print(" ");
               System.out.print(map[i][j]);
                   System.out.print(" ");
           }
           System.out.println("");
       }
     }
}
