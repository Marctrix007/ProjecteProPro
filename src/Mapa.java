
import java.util.ArrayList;
import java.util.HashSet;

// @author Xavier Rodríguez Martínez

public class Mapa {
    // Descripció general: Mapa amb diferents localitzacions, algunes d'elles 
    //                     són punts de recàrrega, i les connexions entre elles
        
    private class Pes{
        float dist;
        float temps;
        
        Pes(float d, float t){
            this.dist = d;
            this.temps = t;
        }
    }
    
    private class Connexio{
        int desti;
        Pes p;
        
        Connexio(int d, Pes p){
            this.desti = d;
            this.p = p;
        }

        public boolean equals(Connexio c){
            return desti==c.desti;
        }
    }
    
    private ArrayList<Localitzacio> localitzacions;
    private ArrayList<HashSet<Connexio>> connexions;
    
    Mapa(){
    //Pre: --
    //Post: Mapa buit
    }
    
    public void AfegirLocalitzacio(Localitzacio l){
    //Pre: --
    //Post: Afegeix la localització al mapa
        localitzacions.add(l);
        connexions.ensureCapacity(localitzacions.size());
    }
    
    public void AfegirConnexio(int o, int d, float dist, float t) throws Exception{
    //Pre: {o,d} < localitzacions.size()
    //Post: Crea una connexió entre la localització origen i la localització
    //      desti amb pes p, si ja existeix la modifica amb el nou pes.
        if (localitzacions.size()<o || localitzacions.size()<d)
            throw new Exception("Localització no existent");
        Connexio c;
        c = new Connexio(d, new Pes(dist,t));
        boolean hi_es = connexions.get(o).add(c);
        if (!hi_es){
            connexions.get(o).remove(c);
            connexions.get(o).add(c);
        }
    }
    
    public Ruta PRmesProxim(int loc) throws Exception{
        //Pre: loc < localitzacions.size()
        //Post: Retorna la ruta al punt de recàrrega més pròxim
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        
        
    }
    
    public Ruta CamiMinim(int o, int d) throws Exception{
        //Pre: {o,d} < localitzacions.size()
        //Post: Retorna la ruta de distància mínima entre  o  i  d.
        if (localitzacions.size()<o || localitzacions.size()<d)
            throw new Exception("Localització no existent");
        
    }
    
    public int nLocalitzacions() {
    // Pre: --
    // Post: Retorna el nombre de localitzacions del mapa 
    
        return localitzacions.size(); 
        
    }
    
    
    public Localitzacio loc(int i) {
    // Pre: 0 < i < nLocalitzacions 
    // Post: Retorna la localització de la posició i de la taula 
    
        return localitzacions.get(i);
        
    }
    
    public int Popularitat(int l) throws Exception{
        //Pre: l < localitzacions.size()
        //Post: Retorna l'índex de popularitat de la localització  l.
        if (localitzacions.size()<l)
            throw new Exception("Localització no existent");
        return localitzacions.get(l).popularitat();
    }
}
