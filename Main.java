
public class Main
{
	static Spielmenue spiel;
	static Hauptmenue haupt;
	static Optionsmenue option;
	static Game game;
	static int window;
	
	protected void schliesseSpiel()
	{
		spiel.dispose();
	}
	protected void schliesseHaupt()
	{
		haupt.dispose();
	}
	protected void schliesseOption()
	{
		option.dispose();
	}
	protected void setWert(int zahl)
	{
		window = zahl;
	}
	
	public static void main(String[] args)
	{
		haupt = new Hauptmenue("Hauptmenue");
	}
	
	public static void main2(String[] args)
	{
		if(window == 1)
		{
			spiel = new Spielmenue("Spielmenue");
		}
		if(window == 2)
		{
			option = new Optionsmenue("Optionsmenue");
		}
		if(window == 3)
		{
			haupt = new Hauptmenue("Hauptmenue");
		}
		if(window == 4)
		{
			//neues Spielfenster.
		}
	}

}
