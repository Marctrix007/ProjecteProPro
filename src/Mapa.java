import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

// @author Xavier Rodríguez Martínez

public class Mapa {
    // Descripció general: Mapa amb diferents localitzacions, algunes d'elles 
    //                     són punts de recàrrega, i les connexions entre elles
        
    private ArrayList<Localitzacio> localitzacions;
    private ArrayList<Map<Integer,Pes>> connexions;
    
    private TreeSet<Integer> indexsPR;
    private ArrayList<ArrayList<Pes>> distancies;
    private ArrayList<ArrayList<Integer>> previs;
    boolean esFinal;
    
    Mapa(){
    //Pre: --
    //Post: Mapa buit
        localitzacions = new ArrayList<Localitzacio>();
        connexions = new ArrayList<Map<Integer,Pes>>();
        indexsPR = new TreeSet<Integer>();
        distancies = new ArrayList<ArrayList<Pes>>();
        previs = new ArrayList<ArrayList<Integer>>();
        esFinal = false;
    }
    
    public void AfegirLocalitzacio(Localitzacio l){
    //Pre: --
    //Post: Afegeix la localització al mapa
        localitzacions.add(l);
        connexions.ensureCapacity(localitzacions.size());
        if (l.esPuntDeRecarrega())
            indexsPR.add(localitzacions.size()-1);
    }
    
    public void AfegirConnexio(int o, int d, float dist, Temps t) throws Exception{
    //Pre: {o,d} < localitzacions.size()
    //Post: Crea una connexió entre la localització origen i la localització
    //      desti amb pes p, si ja existeix la modifica amb el nou pes.
        if (localitzacions.size()<=o || localitzacions.size()<=d)
            throw new Exception("Localització no existent");
        connexions.get(o).put(d, new Pes(dist, t));
    }
    
    public int PRmesProximA(int loc) throws Exception{
        //Pre: loc < localitzacions.size()
        //Post: Retorna la ruta al punt de recàrrega més pròxim a loc que tingui places disponibles
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
    }
    
    public int PRmesProximDesde(int loc, int nPlaces, double dist) throws Exception{
        //Pre: loc < localitzacions.size()
        //Post: Retorna la ruta al punt de recàrrega més pròxim des de loc que disposi d'un vehicle amb nPlaces i autonomia suficeint
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
    }
    
    public Ruta CamiMinim(int o, int d) throws Exception{
        //Pre: {o,d} < localitzacions.size()
        //Post: Retorna la ruta de distància mínima entre  o  i  d.
        if (localitzacions.size()<o || localitzacions.size()<d)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
        Ruta r = new Ruta();
        r.afegirPes(distancies.get(o).get(d));
        int aux = d;
        while (aux != o){
            r.addFirst(aux);
            aux = previs.get(o).get(aux);
        }
        return r;
    }
    
    public int nLocalitzacions() {
    // Pre: --
    // Post: Retorna el nombre de localitzacions del mapa 
        return localitzacions.size(); 
    }
    
    public int nPuntsRecarrega(){
        //Pre:--
        //Post: Retorna el nombre de punts de recàrrega del mapa
        return indexsPR.size();
    }
    
    public Localitzacio loc(int i) {
    // Pre: 0 < i < nLocalitzacions 
    // Post: Retorna la localització de la posició i de la taula 
        
        return localitzacions.get(i);
    }
        
    private void Dijkstra(int origen) throws Exception{
    //Pre: 0 < origen < localitzacions.size()
    //Post: Calcula la taula de distancies i previs de Dijkstra des de l'origen
        ArrayList<Pes> dist;
        dist = new ArrayList<Pes>(localitzacions.size());
        ArrayList<Integer> prev;
        prev = new ArrayList<Integer>(localitzacions.size());
        Set<Integer> Q = null;
        
        Pes INFINIT = new Pes(2147483647, new Temps(24,00));
        
        for (int v=0; v < localitzacions.size(); v++){
            dist.set(v, INFINIT);
            prev.set(v, null);
            boolean add = Q.add(v);
            if (!add) throw new Exception("Q falla");
        }
        dist.set(origen, new Pes());
                
        while (!Q.isEmpty()){
            int u = -1;
            
            //u vertex a Q amb min dist[u]
            Iterator<Integer> ite = Q.iterator();
            
            if (ite.hasNext()) u = ite.next();                
            while(ite.hasNext()){
                int aux = ite.next();
                if (dist.get(aux).compareTo(dist.get(u)) < 0) u = aux;
            }
            
            if(u==-1) throw new Exception("Dijkstra out of range");
            Q.remove(u);
                        
            Iterator<Entry<Integer,Pes>> veins;
            veins = connexions.get(u).entrySet().iterator();
                    
            while (veins.hasNext()) {
                Entry<Integer,Pes> aux = veins.next();
                Integer v = aux.getKey();
                Pes p = aux.getValue();
                Pes alt = dist.get(u).mes(p);
                if (alt.compareTo(dist.get(v)) < 0){
                    dist.set(v, alt);
                    prev.set(v, u);
                }
            }          
        }
        distancies.set(origen, dist);
        previs.set(origen, prev);
        
        esFinal = true;
    }
    
    private void Dijkstra() {
    //Pre: --
    //Post: Calcula la matriu de distancies i previs per Dijkstra
        for (int i=0; i<localitzacions.size(); i++){
            try {
                Dijkstra(i);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    
}
