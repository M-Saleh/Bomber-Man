package hello;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.MediaException;

public class Bomb {
    
    static boolean mat = false ;
    int tileX;  
    int tileY;
    Sprite bombSprite;
    int range;
    int timer;
    boolean stillOn ;
    int max = 55 ;
    private boolean fired ;
    private Vector firedBombVec ;
    private int firedBombVec_size;
    int bombstatus;
    private static final int  one = 0;
    private static final int two = 1;
    private static final int three = 2;
    private static final int fire = 3;
    
    public Bomb(int tileX , int tileY,int range,int bombstatus) throws IOException 
    {
        this.tileX = tileX;
        this.tileY = tileY;
        this.timer = 0;
        this.range = range;
        firedBombVec = new Vector();
        fired = false ;
        firedBombVec_size=0;
        mat = false ;
        stillOn = true ;
        this.bombstatus = bombstatus;
        Game.map[tileY][tileX] = bombstatus ;
//        System.out.println("status: "+bombstatus);
//        if(bombstatus == -5)
//            Game.printMap(Game.map);
        Image monImage = Image.createImage("/BombAll.PNG");
        bombSprite = new Sprite(monImage, monImage.getHeight(), monImage.getHeight());
        int [] seq = {0,1,2,3};
        bombSprite.setFrameSequence(seq);        
        bombSprite.setPosition(tileX *32, tileY*32 );
    }
    
    public boolean checkStatus (LayerManager layerManager , Character player,TiledLayer backGround_layer , Vector monsVec) throws IOException, MediaException
    {   
        timer ++ ;
        if (fired && timer >= 3)
        {            
            Bomb b ;
             firedBombVec_size = firedBombVec.size();
             int size ;
//             System.out.println("Size = " + firedBombVec_size) ;
             
             for (int i=0;i<firedBombVec_size;i++)
             {
                b = (Bomb) firedBombVec.elementAt(i);
                 if (b.bombSprite.collidesWith(player.playerSprite, true))
                 {
                     mat = true ;
                    return true ;
                 }                 
                 
                 else{ 
                        if ( Game.map[b.tileY][b.tileX] == MapGenerator.FIRE)
                        {                     
                            Game.map[b.tileY][b.tileX] = MapGenerator.EMPTY ;
//                            System.out.println(b.tileX + ","+b.tileY);
                            backGround_layer.setCell(b.tileX, b.tileY,0);                              
                        }

//                        else // check bomb Vs. Monster collision
//                        {
                            Monster mon ;
                            size = monsVec.size();
                            for (int k=0;k<size;k++)
                            {
        //                        System.out.println("Size = " + size) ;
                                mon =  (Monster) monsVec.elementAt(k) ;
                                if (mon.monsterSprite.collidesWith(b.bombSprite, true)
                                    || mon.monsterSprite.collidesWith(this.bombSprite, true))
                                {
//                                    System.out.println("bomb Vs. Monster collision");
                                    Game.map[b.tileY][b.tileX] = MapGenerator.EMPTY ;
                                    mon.alive = false ;
                                    mon.destroyed(monsVec);
                                }                        
                            }                   
//                    }
                 } 
                 layerManager.remove(b.bombSprite);
                 b = null;
             }
                firedBombVec.removeAllElements();
                
             if (this.bombSprite.collidesWith(player.playerSprite, true))
             {
                 mat = true ;
             }
             
             layerManager.remove(bombSprite);
             
             return true ;
        }
        
        if(this.timer >= max && !this.fired) 
        {
//            if (Game.restBombs < 2 * Game.level)
//                Game.restBombs ++ ;
            
            this.fired = true ;
            bombSprite.setFrame(fire);
            this.bombstatus = MapGenerator.FIRE;
            Game.map[tileY][tileX] = MapGenerator.FIRE;
            if (!Game.mute)
                Game.bomb_tone.start();
            if (Game.map[tileX+1][tileY] != MapGenerator.WALL)
            {
                Bomb b = new Bomb(tileX+1, tileY, 1,MapGenerator.FIRE);
                b.bombSprite.setFrame(fire);
                layerManager.insert(b.bombSprite, Game.playerPriority);
                firedBombVec.addElement(b);
            }
             if (Game.map[tileX-1][tileY] != MapGenerator.WALL)
            {
                Bomb b = new Bomb(tileX-1, tileY, 1,MapGenerator.FIRE);
                b.bombSprite.setFrame(fire);
                layerManager.insert(b.bombSprite, Game.playerPriority);
                firedBombVec.addElement(b);
            }
             if (Game.map[tileX][tileY+1] != MapGenerator.WALL)
            {
                Bomb b = new Bomb(tileX, tileY+1, 1 ,MapGenerator.FIRE);
                b.bombSprite.setFrame(fire);
                layerManager.insert(b.bombSprite, Game.playerPriority);
                firedBombVec.addElement(b);
            }
             if (Game.map[tileX][tileY-1] != MapGenerator.WALL)
            {
                Bomb b = new Bomb(tileX, tileY-1, 1,MapGenerator.FIRE);
                b.bombSprite.setFrame(fire);
                layerManager.insert(b.bombSprite, Game.playerPriority);
                firedBombVec.addElement(b);
            }
             
            timer = 0 ;
            return  false;
        }
        
        if ((timer % 4) == 0 && !fired)
        {
            switch (bombSprite.getFrame())
            {
                case Bomb.one:
                    bombSprite.setFrame(Bomb.two);
                    break;
                case Bomb.two:
                    bombSprite.setFrame(Bomb.three);
                    break;
                case Bomb.three:
                    bombSprite.setFrame(Bomb.one);
                    break;
            }
        }    
        return false ;
    }
}
