import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// @author Xavier Rodríguez Martínez

public class Mapa {
    // Descripció general: Mapa amb diferents localitzacions, algunes d'elles 
    //                     són punts de recàrrega, i les connexions entre elles
       
    final float INFINIT = 2147483647;
    
    private ArrayList<Localitzacio> localitzacions;
    private ArrayList<Map<Integer,Pes>> connexions;
    private ArrayList<Integer> indexsPR;
    
    Mapa(){
    //Pre: --
    //Post: Mapa buit
        localitzacions = new ArrayList<Localitzacio>();
        connexions = new ArrayList<Map<Integer,Pes>>();
        indexsPR = new ArrayList<Integer>();
    }
    
    public void AfegirLocalitzacio(Localitzacio l){
    //Pre: --
    //Post: Afegeix la localització al mapa
        localitzacions.add(l);
        connexions.ensureCapacity(localitzacions.size());
        if (l.esPuntDeRecarrega())
            indexsPR.add(localitzacions.size()-1);
    }
    
    public void AfegirConnexio(int o, int d, float dist, float t) throws Exception{
    //Pre: {o,d} < localitzacions.size()
    //Post: Crea una connexió entre la localització origen i la localització
    //      desti amb pes p, si ja existeix la modifica amb el nou pes.
        if (localitzacions.size()<=o || localitzacions.size()<=d)
            throw new Exception("Localització no existent");
        connexions.get(o).put(d, new Pes(dist, t));
    }
    
    /*public Ruta PRmesProxim(int loc) throws Exception{
        //Pre: loc < localitzacions.size()
        //Post: Retorna la ruta al punt de recàrrega més pròxim
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        
        
    }*/
    
    private void BellmanFord(int o, ArrayList<Float> distance, ArrayList<Integer> predecessor){
        
        for (int i=0; i < localitzacions.size(); i++){
            distance.set(i, INFINIT);
            predecessor.set(i, null);
        }
        
        distance.set(o, (float) 0);
        
        for (int i=1; i < connexions.size(); i++){
            Iterator<Integer> ite = connexions.get(i).keySet().iterator();
            while (ite.hasNext()){
                Integer aux = ite.next();
                float w = connexions.get(i).get(aux).distancia();
                if (distance.get(i) + w < distance.get(aux)){
                    distance.set(aux, distance.get(i) + w);
                    predecessor.set(aux, i);
                }
            }
        }
    }
    
    private Ruta Dijkstra(int origen, int desti){
        ArrayList<Float> dist;
        dist = new ArrayList<Float>(localitzacions.size());
        ArrayList<Integer> prev;
        prev = new ArrayList<Integer>(localitzacions.size());
        Set<Integer> Q = null;
        
        for (int v=0; v < localitzacions.size(); v++){
            dist.set(v, INFINIT);
            prev.set(v, null);
            Q.add(v);
        }
        dist.set(origen, (float) 0);
        
        Ruta r = new Ruta();
        Pes p = new Pes();
        
        while (!Q.isEmpty()){
            int u;
            
            //u vertex a Q amb min dist[u]
            
            Q.remove(u);
            
            if(u==desti){
                boolean fi = prev.get(u)==null;
                while (!fi){
                    r.addFirst(u);
                    if (prev.get(u)==null) fi=true;
                    else{
                        r.afegirPes(connexions.get(u).get(prev.get(u)));
                        u = prev.get(u);
                    }
                }
                return r;
            }
        }
        return r;
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
    
    public int Popularitat(int l) throws Exception{
    //Pre: l < localitzacions.size()
    //Post: Retorna l'índex de popularitat de la localització  l.
        if (!(l<localitzacions.size()))
            throw new Exception("Localització no existent");
        return localitzacions.get(l).popularitat();
    }
}
