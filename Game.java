package hello;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.Player;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;

public class Game extends GameCanvas implements Runnable{
   
    static boolean mute = false ;
    static final int playerPriority = 0 ;
    static final int bomPriority = 1 ;
    static final int monsterPriority = 0 ;
    static final int boxPriority = 3 ;
    static final int numberPriority = 4 ; 
    private Graphics g ;
    
    private int visibleTimer ;
    int equationOperator;
    static LayerManager layerManager ;
    static TiledLayer backGround_layer;
    Character player ;
    Vector monstersVec ;        
    Vector bombsVec;
    Vector Sprits_nubmVec;
    int w , h ;
    static int [][] map ;
    int sight = 3 ;
    int keyStates;
    static int maxBombs ;
    static int step ; // player and monsters step
    static int level = 1 ;
    int [] equation ;
    int answer ;
    int wrongCounter;
    int spirites = 3;
    Vector power_sprites ;
    
    
    int nOfColumns_Rows = 16 ;
    int max = nOfColumns_Rows * 32 ;
    int x_view ;
    int y_view;
    int titleHeight;
    String title;    
    static Player tone;
    Player di_tone;
    Player vis_tone;
    static Player bomb_tone;
    MapGenerator mapGen;
    
public Game () throws IOException
{    
    super(true);                             
    InputStream in = getClass().getResourceAsStream("/ow.wav");
    InputStream in1 = getClass().getResourceAsStream("/uhh.wav");
    InputStream in2 = getClass().getResourceAsStream("/bomb.wav");
    InputStream in3 = getClass().getResourceAsStream("/vis.wav");
        try {
            tone = Manager.createPlayer(in, "audio/x-wav");
            di_tone = Manager.createPlayer(in1, "audio/x-wav");
            bomb_tone = Manager.createPlayer(in2, "audio/x-wav");
            vis_tone = Manager.createPlayer(in3, "audio/x-wav");
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    initialize();
    w = getWidth();
    h = getHeight();
} 

private void initialize () throws IOException
{
    Sprits_nubmVec = new Vector();
    monstersVec = new Vector();
    bombsVec = new Vector();
    power_sprites = new Vector();
    layerManager = new LayerManager();
    wrongCounter = 0;
    maxBombs = level ;
    layerManager.setViewWindow(0, 0, w, h);
    mapGen = new MapGenerator();       
        
    createBackGround();    
    createPlayer();
    addNumbers_Power_Sprits();
    createEquation();
}

private void reStart () throws IOException, InterruptedException
{    
    Thread.sleep(1000);
    initialize();
}

public void addNumbers_Power_Sprits() throws IOException
{
    Image backgImage =  Image.createImage("/allTiles.PNG"); 
    Image powerImage = Image.createImage("/apple.png");
    int [] seq = {0};
    for (int i=0;i<10;i++)
    {
        Sprite numSprite = new Sprite(backgImage,backgImage.getWidth()/2,backgImage.getHeight()/7);
        numSprite.setFrame(i+4);
        Sprits_nubmVec.addElement(numSprite);
        numSprite.setPosition(MapGenerator.boxDigits[i][1]*32,MapGenerator.boxDigits[i][0]*32);
        layerManager.append(numSprite);
        
        Sprite power_sprite = new Sprite(powerImage, powerImage.getWidth(), powerImage.getHeight());        
        power_sprite.setFrameSequence(seq);        
        power_sprite.setPosition(32, 64 );
        power_sprites.addElement(power_sprite);
        power_sprite.setPosition(MapGenerator.boxPower[i][1]*32,MapGenerator.boxPower[i][0]*32);
        layerManager.append(power_sprite);        
    }
    
}

public void PlayerCollMonsters() throws IOException, MediaException, InterruptedException
{
    Monster mon ;
    boolean maat = false ;
    for (int i=0;i<monstersVec.size();i++ )
    {
        mon =  (Monster)monstersVec.elementAt(i);
        if(player.playerSprite.collidesWith(mon.monsterSprite, false) && mon.alive)
        {
            maat = true ;
//            break;
        }
    }
    
    if (maat)
        die();
}

public void die() throws IOException, MediaException, InterruptedException 
{
        
    if (!mute)
        di_tone.start();
    if (spirites == 0)
    {
        spirites = 3;
        level = 1;
    }
    else
        spirites -- ;
    reStart();
 }

private void createBackGround () throws IOException
{
    Image backgImage =  Image.createImage("/allTiles.PNG");  

    backGround_layer = new TiledLayer(nOfColumns_Rows,nOfColumns_Rows, backgImage,backgImage.getWidth()/2,backgImage.getHeight()/7);
    
    backGround_layer.setPosition(0,0);    
    
    map  = mapGen.generateMap(nOfColumns_Rows-2, nOfColumns_Rows-2,level*6,1 ,1); // ana 3ayz size l map zy ma ana ba3to y? +2 
//    printMap(map);
    
    for (int i = 0 ; i <map.length ; i++)
    {
        for (int j=0;j<map[0].length ;j++)
        {
            switch (map[i][j])
            {
                case MapGenerator.WALL:
                {
                    backGround_layer.setCell(j, i, 2);
                    break;
                }
                case MapGenerator.MONSTER:
                {
                    Monster mon = new Monster(i,j, sight);
                    monstersVec.addElement(mon);                    
                    layerManager.insert(mon.monsterSprite, monsterPriority);
                    break;
                }
                case MapGenerator.PLAYER:                    
                    break;
                case MapGenerator.EMPTY:                    
                    break;
                case MapGenerator.BOX:  
                    backGround_layer.setCell(j, i, 1);
                    break ;
                default:
                    break;
            }
        }
    }
    
    layerManager.append(backGround_layer);
}

public void createPlayer() throws IOException
{
     player = new Character();
     layerManager.insert(player.playerSprite, playerPriority);
}

public void setPortionView ()
{
        x_view = player.playerSprite.getX()-(getWidth()/2) +16; // l 3rd
        y_view = player.playerSprite.getY()-(getHeight()/2)+16; // l tol
        titleHeight = getHeight()/10;
        if (x_view <1)
            x_view = 1;
        if (y_view <-titleHeight)
            y_view = -titleHeight;
        if (x_view > max-getWidth())
            x_view = max-getWidth();
        if (y_view > max-getHeight())
            y_view = max-getHeight();
        layerManager.setViewWindow(x_view, y_view, w, h);   
}


public void moveMonsters()
{
    Monster mon ;
    for (int i=0;i<monstersVec.size();i++ )
    {
        mon =  (Monster)monstersVec.elementAt(i);
        mon.check_Move_Dest(monstersVec);
    }
}

public void checkBombStatus() throws IOException, InterruptedException, MediaException
{
    Bomb bomb ;
    for (int i=0;i<bombsVec.size();i++ )
    {
        bomb =  (Bomb)bombsVec.elementAt(i);
        if (bomb.checkStatus(layerManager,player,backGround_layer,monstersVec))
        {
            bombsVec.removeElement(bomb);
        }
        if (Bomb.mat)
            die();
    }    
}

public void success () throws IOException, InterruptedException
{
    level ++ ;
    reStart();
}

public void fail () throws IOException, InterruptedException
{    
    level = 1;
    reStart();
}

public void selectNumber () throws IOException, InterruptedException
{
    Sprite numSprinte ;
    for (int i=0;i<Sprits_nubmVec.size();i++)
    {
        numSprinte = (Sprite) Sprits_nubmVec.elementAt(i);
        if (player.playerSprite.collidesWith(numSprinte, true))
        {
            if (numSprinte.getFrame() == answer+4)
                success();               
            else if(wrongCounter == 2)
                fail();
            else
                wrongCounter ++ ;            
            break;
        }
    }            
}

public void selectPower () throws MediaException
{        
    Sprite power ;
    for (int i=0;i<power_sprites.size();i++)
    {
        power = (Sprite) power_sprites.elementAt(i);
        if (player.playerSprite.collidesWith(power, true))
        {
            player.visible = false ;
            visibleTimer = 200 ;
            maxBombs = (level * 2) ;
            if (!mute)
                vis_tone.start();
            
            layerManager.remove(power);
            power_sprites.removeElement(power);
            break;
        }
    }          
}

public void checkVisabality() throws MediaException
{
    if (!player.visible)
    {
        visibleTimer --;
    }
    
    if (visibleTimer == 0)
    {
        player.visible = true ;
        vis_tone.stop();
    }
}

public void run() 
{    
    g = getGraphics();

    while (true)
    {                        
      try {
            // set VIEW
            setPortionView();        
           
            if (bombsVec.size() > 0)
                checkBombStatus();
            
             // PlayerCollMonsters
            if (player.visible)
                PlayerCollMonsters();
            checkVisabality();
            
             // selectNumber
            if ((keyStates & GAME_A_PRESSED)!= 0)
            {
                selectPower();
                selectNumber();
            }
            
            // mute
            if ((keyStates & GAME_B_PRESSED)!= 0)
            {
                System.out.println(44444);
                mute = !mute ;
            }
            
            keyStates = getKeyStates();                        
            // bomp we sraweeeeeee5 :D
            if ((keyStates & FIRE_PRESSED)!= 0)
            {                
                if (maxBombs > bombsVec.size())
                {
                    Bomb  bom = new Bomb(player.getTileX(),player.getTileY(),1,MapGenerator.BOMB);  
                    layerManager.insert(bom.bombSprite, bomPriority);
                    bombsVec.addElement(bom);                    
                }
            }                                               
            
            //detect player Motion
            player.detectMotion(backGround_layer,keyStates , bombsVec);

            // monstersMovment
            moveMonsters();

            g.setColor(0xffffff); // white
            g.fillRect(0, 0,w,h);

            layerManager.paint(g, 0, 0);
//            setFullScreenMode(true);
            g.setColor(0, 0, 0);
            g.fillRect(0, 0, getWidth(),titleHeight);
            g.setColor(255, 0, 0);
             g.drawString(title, 0, 0, 0);
            flushGraphics();

            Thread.sleep(55);
            
        }   catch (MediaException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
            ex.printStackTrace();
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }            
}

public void putTitle(int level , int[] equation,int operator , int spirits){
    String title = "LEVEL:";
    title+=level;
    title+=" ";
    title+=EquationGenerator.EquationToString(equation, operator);
    title+=" SPIRITS: ";
    title+=spirits;
    this.title = title;
}

private void createEquation() {
        EquationGenerator mg = new EquationGenerator();
        if (level ==1)
            this.equationOperator = mg.PLUS ;
        else
            this.equationOperator = mg.getRandom(mg.OPERATORNUM);       
       equation = mg.generateEquation(this.equationOperator);
       answer = equation [equation[9]];
       putTitle(level, equation,equationOperator,spirites);
    }

 public static void printMap(int [][] map){
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

    public void itemStateChanged(Item item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}