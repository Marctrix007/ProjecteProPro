/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Xavier
 */
public interface MapaI
    Mapa();
    //Pre: --
    //Post: Mapa buit
    
    public void AfegirLocalitzacio(Localitzacio l);
    //Pre: --
    //Post: Afegeix la localització al mapa
    
    public void AfegirConnexio(int o, int d, float dist, Temps t) throws Exception;
    //Pre: {o,d} < localitzacions.size()
    //Post: Crea una connexió entre la localització origen i la localització
    //      desti amb pes p, si ja existeix la modifica amb el nou pes.
    
    public PuntDeRecarrega PRmesProximA(int loc) throws Exception;
        //Pre: loc < localitzacions.size()
        //Post: Retorna la ruta al punt de recàrrega més pròxim a loc

    public PuntDeRecarrega PRmesProximDesde(int loc) throws Exception;
        //Pre: loc < localitzacions.size()
        //Post: Retorna la ruta al punt de recàrrega més pròxim des de loc
        
    public Ruta CamiMinim(int o, int d) throws Exception;
        //Pre: {o,d} < localitzacions.size()
        //Post: Retorna la ruta de distància mínima entre  o  i  d.
        
    public int nLocalitzacions();
    // Pre: --
    // Post: Retorna el nombre de localitzacions del mapa 
    
    public int nPuntsRecarrega();
        //Pre:--
        //Post: Retorna el nombre de punts de recàrrega del mapa
    
    public Localitzacio loc(int i);
    // Pre: 0 < i < nLocalitzacions 
    // Post: Retorna la localització de la posició i de la taula 

    public int Popularitat(int l) throws Exception{
    //Pre: l < localitzacions.size()
    //Post: Retorna l'índex de popularitat de la localització  l.
}
