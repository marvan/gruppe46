import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Renderer {
	
	private static int width, height, fps;
	private static float clearR, clearG, clearB, clearA;
	/**
	 * Grafik fuer leeres Feld
	 */
	public static final LWJGL_Sprite Tile_Empty = new LWJGL_Sprite("empty.png");
	/**
	 * Grafik fuer Mauer
	 */
	public static final LWJGL_Sprite Tile_Wall = new LWJGL_Sprite("wall.png");
	/**
	 * Grafik fuer Bombe
	 */
	public static final LWJGL_Sprite Tile_Bomb = new LWJGL_Sprite("bomb.png");
	/**
	 * Grafik fuer Explosion
	 */
	public static final LWJGL_Sprite Tile_Explosion = new LWJGL_Sprite("explosion.png");
	/**
	 * Grafik fuer teilweise zerstoerte Mauer
	 */
	public static final LWJGL_Sprite Tile_Break = new LWJGL_Sprite("breakable.png");
	/**
	 * Grafik fuer Extrafeld 1 (extra Leben)
	 */
	public static final LWJGL_Sprite Tile_Health = new LWJGL_Sprite("health.png");
	/**
	 * Grafik fuer Extrafeld 2 (extra Bombe)
	 */
	public static final LWJGL_Sprite Tile_addbomb = new LWJGL_Sprite("extrabomb.png");
	/**
	 * Grafik fuer Extrafeld 2 (extra Bombe)
	 */
	public static final LWJGL_Sprite Tile_kick = new LWJGL_Sprite("kick.png");
	/**
	 * Grafik fuer Extrafeld 5 (steuerungstausch)
	 */
	public static final LWJGL_Sprite Tile_confuse = new LWJGL_Sprite("confuse.png");
	/**
	 * Grafik fuer Extrafeld 6 (Teleport)
	 */
	public static final LWJGL_Sprite Tile_teleport = new LWJGL_Sprite("teleport.png");
	/**
	 * Grafik fuer Extrafeld 7 (speedup)
	 */
	public static final LWJGL_Sprite Tile_speed = new LWJGL_Sprite("speed.png");
	/**
	 * Grafik fuer Extrafeld 8 (slowdown)
	 */
	public static final LWJGL_Sprite Tile_slow = new LWJGL_Sprite("slow.png");
	
	/**
	 * Font Object zur Textausgabe
	 */
	private static LWJGL_Font lucida;
	
	//public static Audio Theme;	
	//public static Audio Bomb_Explode;
	public static void initDisplay() {
		initDisplay(640,480,60);
	}
	
	public static void initDisplay(int w, int h) {
		initDisplay(w,h,60);
	}
	
	public static void initDisplay(int fps) {
		initDisplay(640,480,fps);
	}
	
	public static void initDisplay(int w, int h, int fps) {
		width = w;
		height = h;
		Renderer.fps = fps;
		
		/* set up display mode */
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	

	public static void initGL() {
		
		/* set up 'camera' as Ortho */
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        
        /* enable backface culling */
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        /* enable transparency */
        GL11.glEnable (GL11.GL_BLEND); 
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        Tile_Empty.init();
        Tile_Empty.setScaleX(Feld.getSize()/(float)Tile_Empty.getWidth()); Tile_Empty.setScaleY(Feld.getSize()/(float)Tile_Empty.getHeight());
        Tile_Wall.init();
        Tile_Wall.setScaleX(Feld.getSize()/(float)Tile_Wall.getWidth()); Tile_Wall.setScaleY(Feld.getSize()/(float)Tile_Wall.getHeight());
        Tile_Bomb.init();
        Tile_Bomb.setScaleX(Feld.getSize()/(float)Tile_Bomb.getWidth()); Tile_Bomb.setScaleY(Feld.getSize()/(float)Tile_Bomb.getHeight());
        Tile_Explosion.init();
        Tile_Explosion.setScaleX(Feld.getSize()/(float)Tile_Explosion.getWidth()); Tile_Explosion.setScaleY(Feld.getSize()/(float)Tile_Explosion.getHeight());
        Tile_Break.init();
        Tile_Break.setScaleX(Feld.getSize()/(float)Tile_Break.getWidth()); Tile_Break.setScaleY(Feld.getSize()/(float)Tile_Break.getHeight());
        Tile_Health.init();
        Tile_Health.setScaleX(Feld.getSize()/(float)Tile_Health.getWidth()); Tile_Health.setScaleY(Feld.getSize()/(float)Tile_Health.getHeight());
        Tile_addbomb.init();
        Tile_addbomb.setScaleX(Feld.getSize()/(float)Tile_addbomb.getWidth()); Tile_addbomb.setScaleY(Feld.getSize()/(float)Tile_addbomb.getHeight());
        Tile_kick.init();
        Tile_kick.setScaleX(Feld.getSize()/(float)Tile_kick.getWidth()); Tile_kick.setScaleY(Feld.getSize()/(float)Tile_kick.getHeight());
        Tile_confuse.init();
        Tile_confuse.setScaleX(Feld.getSize()/(float)Tile_confuse.getWidth()); Tile_confuse.setScaleY(Feld.getSize()/(float)Tile_confuse.getHeight());
        Tile_teleport.init();
        Tile_teleport.setScaleX(Feld.getSize()/(float)Tile_teleport.getWidth()); Tile_teleport.setScaleY(Feld.getSize()/(float)Tile_teleport.getHeight());
        Tile_speed.init();
        Tile_speed.setScaleX(Feld.getSize()/(float)Tile_speed.getWidth()); Tile_speed.setScaleY(Feld.getSize()/(float)Tile_speed.getHeight());
        Tile_slow.init();
        Tile_slow.setScaleX(Feld.getSize()/(float)Tile_slow.getWidth()); Tile_slow.setScaleY(Feld.getSize()/(float)Tile_slow.getHeight());
        
		try {
			lucida = new LWJGL_Font("lucida_console2.png");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		
        //Musik und Sounds laden. 
        /*try {
			Theme = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("theme.wav"));
			Bomb_Explode = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sound.wav"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public static void setClearColor(float r, float g, float b, float a) {
		clearR = r;
		clearG = g;
		clearB = b;
		clearA = a;
	}
	
	
	public static void clearGL() {
		// Clear The Screen And The Depth Buffer
		GL11.glClearColor(clearR, clearG, clearB, clearA);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void sync() {
		Display.update();
		Display.sync(fps);
	}
	
	public static void destroy() {
		Display.destroy();
	}
	
	public static void print(int x, int y, String text, float scale){
		lucida.setScale(scale);
		lucida.print(x, y, text);
	}
}
