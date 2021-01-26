import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
* @author Amayas SADI
* @author Hamid KOLLI
*
* Classe qui permet de lancer le jeu
*/
public final class TestSimulation {

	/**
	* Bloque le constructeur
	* <p> Pour ne pas instancier de TestSimulation c'est le main du jeu </p>
	*/
	private TestSimulation() {}

	/**
	* La probabilite liée a la realisation des evenements
	*/
	public static final double PROBA_EVENT = 0.4;
	
	/**
	*  La probabilite liée a la realisation des regenerations
	*/
	public static final double PROBA_REGENER = 0.5;
	
	/**
	*  La veleur max de la generation des ressources
	*/
	public static final int MAX_GENERER = 10;

	/**
	* Le nombre d'agents initials
	*/
	public static final int NOMBRE_AGENTS = 5;

	/**
	* Le nombre de ressources initiales
	*/
	public static final int NOMBRE_RESSOURCES = 10;

	/**
	* Le nombre de lignes du terrain de ressources
	*/
	public static final int NOMBRE_LIGNES_TERRAIN = 10;

	/**
	* Le nombre de colonnes du terrain de ressources
	*/
	public static final int NOMBRE_COLONNES_TERRAIN = 10;

	/**
	* Varibale pour les input
	*/
	private static Scanner lire = new Scanner(System.in);
	
	/**
	* la methode main
	*
	* @param args : arguments de ligne de commande
	*/
	public static void main(String[] args) {	

		Simulation simulation = new Simulation(NOMBRE_LIGNES_TERRAIN, NOMBRE_COLONNES_TERRAIN);

		//initialiser la construction a 5 agents et 10 ressources initiaux
		simulation.intiSimulation(NOMBRE_AGENTS, NOMBRE_RESSOURCES);	

		System.out.println("");

		//afficher l'etat initiale du jeu
		simulation.afficher();
		simulation.update();

		System.out.print("\n\t ** Appuyez sur une entrer pour lancer le jeu ...");
		lire.nextLine();

		//la boucle du jeu
		while (true) {
			
			System.out.println("");

			// affichage de la simulation et du menu
 			simulation.afficher();
 			
			afficherMenu();	

			System.out.println("");

			//choisir selon le menu propose
			int choix = getChoix();

			if(choix == 1)
                construire(simulation);
            
            else if(choix == 2)
                demolir(simulation);
            
            else if(choix == 4) {
            	System.out.println("\n\t** A bientot ...");
                return;
            }
			
			//exercer tout les evenement aleatoire
			displayEvent(simulation);

			//mise a jour de la simulation a chaque tour de jeu
			simulation.update();

			System.out.println("");
			
			//afficher le recolte du tour
			simulation.afficherRessourcesRecoltees();

			//regenerer les ressource avec une probabilite PROBA_REGENER
			if(simulation.getRessources() != null)
				for(Ressource ress : simulation.getRessources()) {
					if(Math.random() < PROBA_REGENER) 
						Evenement.regenerer(ress);
				}

			//generer des ressource (fer et pierre) aleatoirement ()
			int nombreGener = 1 + (int) Math.random() * MAX_GENERER;
			for(int i = 0; i < nombreGener; i++)
				Evenement.generer(simulation);	

			//dupliquer les agent et affiche  un beau message si des agents ont ete ajouter
			if(Evenement.duplicateAgents(simulation))
				System.out.println("\n\t\t BONNE NOUVELLE ! Des nouveaux agents ont rejoints vos equipes");
 

			System.out.print("\n\t ** Appuyez sur une entrer pour continuer ...");
			lire.nextLine();
		}	
	}

	/**
	* Permet d'afficher le menu 
	*/
	public static void afficherMenu() {

		System.out.println("\t+-------------------+");
		System.out.println("\t| 1- Construire     |");
		System.out.println("\t| 2- Demolir        |");
		System.out.println("\t| 3- Passer le tour |");
		System.out.println("\t| 4- Quitter        |");
		System.out.println("\t+-------------------+");
		
	}

	/**
	* Laisser le choix au joueur pour choisir selon le menu @see afficheMenu()
	*

	* @return le choix de joueur
	*/
	public static int getChoix() {

		System.out.print("\tVeuillez choisir une operation : ");

		int choix;
		do {
			try {
				choix = lire.nextInt();

				if(choix != 1 && choix != 2 && choix != 3 && choix != 4)
					System.out.print("\n\tChoix incorrect ! veuillez réessayer :");
					
			}catch(Exception e) {
				lire.nextLine();
				System.out.print("\n\tChoix incorrect ! veuillez réessayer : ");
				choix = -1;
			}
			//si le choix n'existe pas boucle
		}while(choix != 1 && choix != 2 && choix != 3 && choix != 4);

		//vider le buffer
		if(lire.hasNextLine())
			lire.nextLine();

		return choix;
	}

	/**
	* Permet de gerer la construction du joueur 
	*
	* @param simulation : la simulation qui gere le terrain de construction et de recolte 
	*/
	public static void construire(Simulation simulation) {
          
        int x,y;
        int etat;
        
        String type;
        String catalogue = simulation.getCatalogue();
      
      	//si l'experience n'est pas suffisante donc le catalogue est vide le tour passe  
        if(catalogue == null) {
            System.out.println("Votre experiance ne vous permet pas de construire");
            return ;
        }

        System.out.println("");
        System.out.println("\tVoice le catalogue des batiments déverrouiller : \n\n" + catalogue);

        System.out.println("\tVeuillez indiquer les coordonnes et le type de la construction : ");
		
		//demande au joueur de taper le bon type de construction	
		while(true) {
		
			System.out.print("\t\t Type : ");
			type = lire.nextLine();

			if(Construction.getExigences(type) != null ) 
				break;

            System.out.println("\n\tLe type de batiment n'existe pas !");
		}

        System.out.println("");

        //demande a l'utilisateur de taper les possition ou il veut construire 
		while(true) {
			
			try {

				System.out.print("\t\t Numero de ligne : ");
				x = lire.nextInt();

				System.out.print("\t\t Numero de colonne : ");
				y = lire.nextInt();

        		System.out.println("");
				break;
			}
			catch(Exception e) {
				lire.nextLine();
				System.out.println("\n\t Veuillez indiquer une position correcte");
			}
		}
            
        etat = simulation.construire(type, x, y);

        System.out.println("");

        //selon l'etat de l'operation on affiche un message d'erreur ou un message de succée
        switch(etat) {
                
        	case Simulation.ERREUR_POSITION :
            	System.out.println("\tVous voullez construire dans une zone inexistante");
                break;
                
            case Simulation.ERREUR_CASE_OCCUPEE:
                System.out.println("\tVous voullez construire dans une zone deja prise");
                break;
                                 
            case Simulation.ERREUR_RESSOURCES_INSUFFISANTE:
            	System.out.println("\tVos ressources sont insuffisantes pour construire ce genre de batiment");
            	break;
                 
            case Simulation.ERREUR_EXP_INSUFFISANT:
            	System.out.println("\tVotre experiance ne vous permet pas de construire ce genre de batiment");
            	break;

            default:
            	System.out.println("\tL'ajout est fait avec succés");
        }

        if(lire.hasNextLine())
			lire.nextLine();
    }

    /**
    * Permet de demolir un batiment
    *
    * @param simulation : la simulation qui gere le terrain de construction et de recolte 
    */
    public static void demolir(Simulation simulation) {
    	
    	int x,y;
        int etat;

        System.out.println("");

        System.out.println("\tVeuillez indiquer les coordonnes la construction : ");

        //demande au joueur de taper la position du la construction a demolir
        //boucle tant que la position est fausse
        while(true) {
			
			try {

				System.out.print("\t\t Numero de ligne : ");
				x = lire.nextInt();

				System.out.print("\t\t Numero de colonne : ");
				y = lire.nextInt();

       			System.out.println("");
				break;
			}
			catch(Exception e) {
				lire.nextLine();
				System.out.println("\n\t Veuillez indiquer une position correcte");
			}
		}

		
        if(lire.hasNextLine())
			lire.nextLine();

        etat = simulation.demolir(x, y);

        //si la distruction a echoué revoie un message d'erreur
        if(etat != simulation.SUCCES){
            System.out.println("\n\tVous voullez demolir un batiment qui n'existe pas");
            return;
        }

        System.out.println("\n\tBatiment demolie avec succé");
    }

    /**
    * Permet de gerer tout les evenement aleatoire du jeu
    *
	* @param simulation : la simulation qui gere le terrain de construction et de recolte  
    */
    public static void displayEvent(Simulation simulation){

        int[] numEvent = new int[ 2 ];

        //generer les evenement avec la probabilite  PROBA_EVENT
        if(Math.random() < PROBA_EVENT) 
                numEvent = Evenement.event(simulation);
        else
         	return;

        //Afficher l'evenement effetue sur le terrain de construction 
        switch(numEvent[ 0 ]) {

            case Evenement.CATASTROPHE_NATURELLE_TREMBLEMENT_DE_TERRE:
            	System.out.println("\n\t\t ** UN TREMBLEMENT DE TERRE VERIFIEZ VOS BATIMENTS");
            	break;
           
            case Evenement.CATASTROPHE_NATURELLE_INONDATION:
            	System.out.println("\n\t\t ** VOTRE VILLE EST INONDÉE VERIFIEZ VOS BATIMENTS");
            	break;

            case Evenement.INCENDIE:
            	System.out.println("\n\t\t ** INCENDIE!!!!! APPELEZ LES POMPIERS");
           		break;

            case Evenement.SATURATION_HOPITAUX:
            	System.out.println("\n\t\t ** UNE PANDEMIE RESTEZ CHEZ VOUS (NOUS SOMME EN GUERRE) ");
				break;  
		}

		ArrayList<Construction> constructionsSaccagees = Evenement.getConstructionsSaccagees();

		//Afficher les constructions touch"es par les evenements
		if(!constructionsSaccagees.isEmpty()) {
	      
			System.out.println("\n\t Voici les batiments touches : ");

			for(Construction constr : constructionsSaccagees)
				System.out.println("\t\t " + constr.type + " (" + constr.getX() + ", " + constr.getY() + "), Etat courrant : " + constr.getEtat());
    	}

		//Afficher l'evenement effectue sur le terrain de recolte
		switch(numEvent[ 1 ]) {

            case Evenement.FEU_DE_BOIS:
	            System.out.println("\n\t\t ** VOS ARBRES SONT EN FEU");
	            break;

            case Evenement.ATTAQUE_ANIMAUX:
	            System.out.println("\n\t\t ** LE BOIS ET LES PIERRE ONT ETE DETRUIT PAR DES ANIMAUX SAUVAGE");
	            break;

            case Evenement.VOLE:
	            System.out.println("\n\t\t ** DES VOLEURS APPELEZ LA POLICE !!! ");
	            break;            
        }

		ArrayList<Ressource> ressourcesSaccagees = Evenement.getRessourcesSaccagees();

		//Afficher les ressources touchees par les evenements  
		if(!ressourcesSaccagees.isEmpty()) {
		
			System.out.println("\n\tVoici les ressources touchées : ");

			for(Ressource ress : ressourcesSaccagees)
				System.out.println("\t\t " + ress.type + " (" + ress.getX() + ", " + ress.getY() + "), Quantite restante : " + ress.getQuantite());
		}
	}
}
