import org.lwjgl.input.Keyboard;


public class Player{
	/**
	 * Name des Spielers
	 */
	private String name;
	/**
	 * Aktueller Lebenszustand
	 */
	private boolean alive;
	/**
	 * x- und y-Koordinate
	 */
	private int posx, posy;
	/**
	 * Bomben
	 */
	private int Bombs;
	/**
	 * individuelle Bombenreichweite
	 */
	private int bombsRange=Main.m.getRange();
	/**
	 * Anzahl der Leben
	 */
	protected int lives;
	/**
	 * Letzter Treffer
	 */
	private Bombe last_hit_by;	
	/**
	 * Tasten fuer verschieden Aktionen
	 */
	private int key_left, key_right, key_up, key_down, key_bomb, key_special;
	/**
	 * Ist die Bomben-Taste gedrueckt?
	 */
	private boolean press_bomb;	
	private LWJGL_Sprite Sprite;
	private long lastMove;
	private long invisibleTimer;
	public boolean kicker=false;
	private boolean invisible=false;
	private int item=0;
	private int latency=300;
	private long trapTimer=0;
	private long trapTimer2=0;
	private boolean vulnerable=true;
	private long shieldTimer;
	private boolean confused = false;
	
//SteuerungsAnpassung fuer Netzwerkfaehigkeit
	private boolean movingRight=false;
	private boolean movingLeft=false;
	private boolean movingUp=false;
	private boolean movingDown=false;
	private boolean plantingBomb=false;
	private boolean usingSpecials=false;
	
	
	
	
	/**
	 * Konstruktor
	 * @param name: Name des Spieler
	 * @param x: Start-x-Koordinate
	 * @param y: Start-y-Koordinate
	 */
	public Player (String name, int x, int y){
		this.name=name;
		this.posx=x;
		this.posy=y;
		this.alive=true;
		lastMove = 0;
		lives = Main.m.getLives();
		Bombs = 1;
	}
	/**
	 * Lebt der Spieler?
	 * @return: Lebenszustand
	 */
	public boolean isAlive(){
		return alive; // ES LEEEEEEEBT!
	}
	/**
	 * Wurde ein Treffer erreicht?
	 * @param Bomb: Bombe, die den Treffer verursacht hat
	 */
	public void hit(Bombe Bomb) {
		if(last_hit_by == Bomb) { return; }
		last_hit_by = Bomb;
		if (vulnerable==true){
			lives--;
		}
		if(lives<=0) alive=false;
	}
	/**
	 * X-Koordinate
	 * @return: x-Koordinate
	 */
	public int getx(){
		return posx;
	}
	/**
	 * Anzahl der Leben
	 * @return: Anzahl der Leben
	 */
	public int getLives(){
		return lives;
	}
	/**
	 * y-Koordinate
	 * @return: y-Koordinate
	 */
	public int gety(){
		return posy;
	}
	/**
	 * Laesst Spieler sterben
	 */
	public void die(){
		alive=false;
	}
	/**
	 * Bewegung des Spielers
	 * @param addx: neue x-Koordinate
	 * @param addy: neue y-Koordinate
	 */
	public void move(int addx, int addy){
		if((GameTime.getTime() - lastMove) > latency){
			if(!confused) {
				posx+=addx;
				posy+=addy;
			} else {
				posx-=addx;
				posy-=addy;
			}
			lastMove = GameTime.getTime();
		}
	}
	/**
	 * Ueberpruefung der Art des Extras
	 * @param field: Extrasfeld
	 */
	public void collect(Extrasfeld field){
		int art=field.art;
		switch(art){
			case 1: {lives++;break;}//Dieses Feld generiert ein zusätzliches Leben um einen Bombentreffer zu überleben}
			case 2: {Bombs++;break;}//Dieses Feld generiert ein Upgrade, um die Anzahl der Bomben, die man gleichzeitig legen kann, um 1 zu erhöhen
			case 3: {kicker=true;break;}//Dieses Feld generiert ein Upgrade, wodurch der Spieler dazu befaehigt wird, die Bomben linear zu treten
			case 4: {item=4;break;}//Dieses Feld generiert die einmalige Befähigung, sich für 3 Sekunden unsichtbar zu machen.
			case 5: {item=5;break;}//Dieses Feld generiert ein temporaere Spielerbehinderung, in Form einer Falle. Der Gegner wird langsamer.
			case 6: {break;}//Dieses Feld generiert ein Teleportationsfeld. Der Spieler der auf dieses Feld tritt wird mit sofortiger Wirkung zum entsprechenden Feld teleportiert
			case 7: {latency-=75;break;}//Dieses Feld generiert ein Upgrade, welches einen permanenten Geschwindigkeitsbonus für den Spieler gibt, der es aufsammelt.
			case 8: {item=8;break;}//Dieses Feld generiert eine temporäre Steuerungsbehinderung für alle feindlichen/anderen Spieler, in Form einer legbaren Falle.
			case 9: {item=9;break;}//Dieses Feld generiert einen auslösbaren Schild.
			case 10: {bombsRange++;break;}//Dieses Feld generiert ein Upgrade zur Verbesserung der Bombenreichweite.
		}
	}
	
	public void loadSprite(String file) {
		Sprite = new LWJGL_Sprite(file);
		Sprite.init();
		Sprite.setScaleX(Feld.getSize()/(float)Sprite.getWidth());
		Sprite.setScaleY(Feld.getSize()/(float)Sprite.getHeight());
		
	}
	/**
	 * Zeichnen
	 */
	public void draw(){
		if (invisible==false) {
			Sprite.draw(posx * Feld.getSize(), posy * Feld.getSize());
		}
		if((GameTime.getTime() - invisibleTimer) > 3000){
			invisible=false;
		}
		if((GameTime.getTime() - trapTimer) > 3000){
			latency=300;
		}
		if((GameTime.getTime() - trapTimer2) > 3000){
			/*
    		if(name=="One") {
    			setKeys(Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_SPACE, Keyboard.KEY_RMENU);
    		} else if (name=="Two") {
    			setKeys(Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_F, Keyboard.KEY_E);
    		}
    		*/
			confused = false;
		}
		if((GameTime.getTime() - shieldTimer) > 3000){
			vulnerable=true;
		}
	}
	
	public void confuse(){
		confused = true;
		trapTimer2=GameTime.getTime();
	}
	
	public void shield(){
		vulnerable=false;
		shieldTimer=GameTime.getTime();
	}
	
	/**
	 * Setz neue Anzahl der Bomben
	 * @param n: Neue Anzahl an Bomben
	 */
	public void setBombs(int n) {
		Bombs = n;
	}
	/**
	 * Anzahl der Bomben
	 * @return: Anzahl der Bomben
	 */
	public int getBombs() {
		return Bombs;
	}
	/**
	 * Letzt Tasten zur Bewegung fest
	 * @param l: Taste fuer Bewegung nach links
	 * @param r: Taste fuer Bewegung nach rechts
	 * @param u: Taste fuer Bewegung nach unten
	 * @param d: Taste fuer Bewegung nach oben
	 * @param b: Taste fuer Bewegung fuer Bombe
	 * @param s: Taste fuer Bewegung fuer Extra
	 */
	public void setKeys(int l, int r, int u, int d, int b, int s) {
		key_left = l;
		key_right = r;
		key_down = d;
		key_up = u;
		key_bomb = b;
		key_special= s;
	}
	/**
	 * Taste fuer Bewegung nach oben
	 * @return: Taste fuer Bewegung nach oben
	 */
	public int getKeyUp() {
		return key_up;
	}
	/**
	 * Taste fuer Bewegung nach unten
	 * @return: Taste fuer Bewegung nach unten
	 */
	public int getKeyDown() {
		return key_down;
	}
	/**
	 * Taste fuer Bewegung nach links
	 * @return: Taste fuer Bewegung nach links
	 */
	public int getKeyLeft() {
		return key_left;
	}
	/**
	 * Taste fuer Bewegung nach rechts
	 * @return: Taste fuer Bewegung nach rechts
	 */
	public int getKeyRight() {
		return key_right;
	}
	/**
	 * Taste fuer Bewegung fuer Bomben
	 * @return: Taste fuer Bewegung fuer Bombe
	 */
	public int getKeyBomb() {
		return key_bomb;
	}
	/**
	 * Taste fuer Bewegung Extra
	 * @return: Taste fuer Bewegung nach Extra
	 */
	public int getKeySpecial(){
		return key_special;
	}
	
	/**
	 * Ist Bombe gedrueckt?
	 * @return: Zustand
	 */
	public boolean isPress_bomb() {
		return press_bomb;
	}
	/**
	 * Setzt Bombe press als gedrueckt
	 * @param press: gedrueckte Bombe
	 */
	public void setPress_bomb(boolean press) {
		press_bomb = press;
	}
	/**
	 * Gibt Name zurueckt
	 * @return: Name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setzt Name des Spieler mit name
	 * @param name: Name des Spielers
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean getKicker(){
		return kicker;
	}
	
	public void setKicker(){
		this.kicker=true;
	}
	
	public void setInvisible(){
		this.invisible=true;
		invisibleTimer = GameTime.getTime();
	}
	
	public boolean getInvisible(){
		return invisible;
	}
	
	public int getItem(){
		return item;
	}
	
	public void delItem(){
		item=0;
	}
	
	public void setLatency(int value){
		latency=value;
		trapTimer=GameTime.getTime();
	}
	
	public int getBombsRange(){
		return bombsRange;
	}
	
	public void setRightMovement(boolean value){
		movingRight=value;
	}
	
	public void setLeftMovement(boolean value){
		movingLeft=value;
	}
	
	public void setUpMovement(boolean value){
		movingUp=value;
	}
	
	public void setDownMovement(boolean value){
		movingDown=value;
	}
	
	public void setPlantingBomb(boolean value){
		plantingBomb=value;
	}
	
	public void setUsingSpecials(boolean value){
		usingSpecials=value;
	}
	
	public boolean isMovingRight(){
		return movingRight;
	}
	
	public boolean isMovingLeft(){
		return movingLeft;
	}
	
	public boolean isMovingUp(){
		return movingUp;
	}
	
	public boolean isMovingDown(){
		return movingDown;
	}
	
	public boolean isPlantingBomb(){
		return plantingBomb;
	}
	
	public boolean isUsingSpecials(){
		return usingSpecials;
	}
	
	public void setx(int x){
		posx=x;
	}
	
	public void sety(int y){
		posy=y;
	}
	
}
 