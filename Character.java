package hello;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.MediaException;

public class Character {
    
    Sprite playerSprite;
    int tileX;
    int tileY;
    
    boolean visible;
    static final int goLeft=2;
    static final int runLeft=3;
    static final int goRight=4;
    static final int runRight=5;
    static final int goUp=1;
    static final int goDown=0;
    private int prevTileX;
    private int prevTileY;
    
    private boolean collied  ;

    public Character() throws IOException 
    {
        visible = true ;
        collied = false;
        int [] seq = {0,1,2,3,4,5};
        Image playerImag = Image.createImage("/PlayerAll.PNG");
        int playerLength = playerImag.getHeight();
        Game.step = playerLength / 4 ;

        playerSprite = new Sprite(playerImag, playerLength, playerLength);
        playerSprite.setFrameSequence(seq);
        playerSprite.setPosition(32,32); // set by Pixel

        this.tileX = getTileX();
        this.tileY = getTileY();
        this.prevTileX = this.tileX;
        this.prevTileY = this.tileY;
    }
    
    public int getTileX ()
    {
        return  (playerSprite.getX()/32);
    }
    public int getTileY ()
    {
        return  (playerSprite.getY()/32);
    }
    
    private boolean collision (TiledLayer backGround , Vector bombVec)
    {
        if (playerSprite.collidesWith(backGround, false))
            return true ;
        
        Bomb bom ;
        boolean check ;
        for (int i=0;i<bombVec.size();i++)
        {
            bom = (Bomb) bombVec.elementAt(i);
            check = playerSprite.collidesWith(bom.bombSprite, false) ;
            if (check && !bom.stillOn)
                return true ;
            else if (check && bom.stillOn)
                return false ;
            else if (!check && bom.stillOn)
            {
                bom.stillOn = false ;
                return false ;
            }
        }
        
        return false ;
    }
    
    public void detectMotion (TiledLayer backGround,int keyStates , Vector bombsVec ) throws MediaException
{    
        collied = false ;
        if( (keyStates & Game.LEFT_PRESSED)!= 0)
        {                         
            if(playerSprite.getFrame() == goLeft)
                playerSprite.setFrame(runLeft);
                else
                    playerSprite.setFrame(goLeft);
            
            playerSprite.setPosition(playerSprite.getX()-1, playerSprite.getY());
            if(! collision(backGround , bombsVec) )
            {      
                playerSprite.setPosition(playerSprite.getX()-Game.step +1, playerSprite.getY());
            }     
            else 
            {
                collied = true ;
                playerSprite.setPosition(playerSprite.getX()+1, playerSprite.getY());
            }
        }
        
        else if( (keyStates & Game.RIGHT_PRESSED)!= 0)
        {
            if(playerSprite.getFrame() == goRight)
                playerSprite.setFrame(runRight);
            else
                playerSprite.setFrame(goRight);
            
            playerSprite.setPosition(playerSprite.getX()+1, playerSprite.getY());             
            if (!collision(backGround, bombsVec)) 
            {
                playerSprite.setPosition(playerSprite.getX()+Game.step-1, playerSprite.getY());
            }
            else
            {                
                collied = true ;
                playerSprite.setPosition(playerSprite.getX()-1, playerSprite.getY());
            }
        }
        
        if( (keyStates & Game.UP_PRESSED)!= 0)
        {
             playerSprite.setFrame(goUp);
            playerSprite.setPosition(playerSprite.getX(), playerSprite.getY()-1);
            if (!collision(backGround, bombsVec))
            {
                playerSprite.setPosition(playerSprite.getX(), playerSprite.getY()-Game.step+1);            
            }
            else
            {
                collied = true ;
                playerSprite.setPosition(playerSprite.getX(), playerSprite.getY()+1);
            }
        }
        
        else if( (keyStates & Game.DOWN_PRESSED)!= 0)
        {
             playerSprite.setFrame(goDown);   
            playerSprite.setPosition(playerSprite.getX(), playerSprite.getY()+1);
            if (!collision(backGround, bombsVec ) )
            {
                playerSprite.setPosition(playerSprite.getX(), playerSprite.getY()+Game.step-1);
            }
            else
            {
                collied = true ;
                 playerSprite.setPosition(playerSprite.getX(), playerSprite.getY()-1);
            }
        }            
                
        // sound 
            if (collied)
            {
//                Game.tone.start();
            }
            
            
            this.tileX =( playerSprite.getY()+16) / 32;
            this.tileY = (playerSprite.getX()+16) / 32;
            if(this.tileX != this.prevTileX || this.tileY != this.prevTileY){
                if(Game.map[this.prevTileX][this.prevTileY] != MapGenerator.BOMB)
                    Game.map[this.prevTileX][this.prevTileY] = MapGenerator.EMPTY;
                if(Game.map[this.tileX][this.tileY] != MapGenerator.BOMB)
                 Game.map[this.tileX][this.tileY] = MapGenerator.PLAYER;
                this.prevTileX = this.tileX;
                this.prevTileY = this.tileY;
      }
}
    
}
