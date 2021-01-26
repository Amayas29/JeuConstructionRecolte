import java.util.Random;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Cette classe permet de gerer les ressources du type fer
*/
public class Fer extends Ressource {

	/**
	* Le nombre max du fer d'une case sur le terrain
	*/
	public static final int MAX_FER_MINE = 5;
	
	/**
	* Constructeur de fer
	*
	* @param quantite : la quantite de fer
	*/
	public Fer(int quantite) {

		super("Fer", quantite);

		if(quantite > MAX_FER_MINE)
			setQuantite(MAX_FER_MINE);

		else if (quantite < 0)
			setQuantite(0);
	}

	/**
	* Constructeur par defaut
	*/
	public Fer() {
		this(new Random().nextInt(MAX_FER_MINE - 1) + 1);
	}

	/**
	* Permet de cloner du fer 
	*
	* @return le fer cloner
	*/
	public Fer clone() {
		Fer f = new Fer(getQuantite());
		f.setPosition(getX(), getY());
		return f;
	}
}