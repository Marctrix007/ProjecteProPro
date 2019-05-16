import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/** 
@class Mapa
@brief Mapa amb diferents localitzacions, algunes d'elles són punts de recàrrega, i les connexions entre elles
@author Xavier Rodríguez Martínez
*/

public class Mapa {        
    
    private ArrayList<Localitzacio> localitzacions;
    private ArrayList<Map<Integer,Pes>> connexions;
    
    private TreeSet<Integer> indexsPR;
    private ArrayList<ArrayList<Pes>> distancies;
    private ArrayList<ArrayList<Integer>> previs;
    boolean esFinal;
    
    /**
    @pre --
    @post Mapa buit
    */
    Mapa(){
        localitzacions = new ArrayList<>();
        connexions = new ArrayList<>();
        indexsPR = new TreeSet<>();
        distancies = new ArrayList<>();
        previs = new ArrayList<>();
        esFinal = false;
    }
    
    /**
    @pre --
    @post Afegeix la localització al mapa
    */
    public void AfegirLocalitzacio(Localitzacio l){
        localitzacions.add(l);
        connexions.add(new HashMap());
        distancies.add(new ArrayList<Pes>());
        previs.add(new ArrayList<Integer>());
        esFinal = false;
        if (l.esPuntDeRecarrega())
            indexsPR.add(localitzacions.size()-1);
    }
    
    /**
    @pre 0 <= {o,d} < localitzacions.size()
    @post Crea una connexió entre la localització origen i la localització desti amb pes p, si ja existeix la modifica amb el nou pes.
    */
    public void AfegirConnexio(int o, int d, float dist, Temps t) throws IndexOutOfBoundsException{       
        if (localitzacions.size()<=o || localitzacions.size()<=d)
            throw new IndexOutOfBoundsException("Mida: "+localitzacions.size()+" Valors: "+o+", "+d);
        connexions.get(o).put(d, new Pes(dist, t));
        esFinal = false;
    }
    
    /**
    @pre 0 <= loc < localitzacions.size()
    @post Retorna la cua de punts de recàrrega ordenats per proximitat a loc (de PR a loc)
    */
    public ArrayDeque<Integer> PRMesProximA(int loc) throws Exception{
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
        return PRMesProxim(loc,false);
    }
    
    /**
    @pre 0 <= loc <localitzacions.size()
    @post Retorna la cua de punts de recàrrega ordenats per proximitat a loc (de loc a PR)
    */
    public ArrayDeque<Integer> PRMesProximDesde(int loc) throws Exception{
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
        return PRMesProxim(loc,true);
    }
    
    /**
    @pre 0 <= loc <localitzacions.size()
    @post Retorna la cua de punts de recàrrega ordenats per proximitat a loc en la direcció indicada (true: loc->PR, false: PR->loc)
    */
    private ArrayDeque<Integer> PRMesProxim(int loc, boolean dir) throws Exception{
        TreeSet<Integer> iPR = (TreeSet<Integer>) indexsPR.clone();
        ArrayDeque<Integer> ret = new ArrayDeque<>();
        
        Pes paux, pmin;
        pmin = new Pes();
        int aux, PRmin;
        PRmin = -1;
        
        while (!iPR.isEmpty()){
            Iterator<Integer> ite = iPR.iterator();
            if (ite.hasNext()) pmin = distancies.get(loc).get(ite.next()); //
            while (ite.hasNext()){
                aux = ite.next();
                if (dir) paux = distancies.get(loc).get(aux);
                else paux = distancies.get(aux).get(loc);
                if ( paux.compareTo(pmin) < 0){
                    pmin = paux;
                    PRmin = aux;
                }
            }
            if (PRmin==-1) throw new Exception("No existeixen PRs per anar a/desde" + loc);
            ret.addLast(PRmin);
            iPR.remove(PRmin);
        }
        return ret;
    }
    
    /**
    @pre 0 <= {o,d} < localitzacions.size()
    @post Retorna la ruta amb el camí mínim desde  o  fins a  d
    */
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
    
    /**
    @pre --
    @post Retorna el nombre de localitzacions del mapa
    */
    public int nLocalitzacions() {
        return localitzacions.size(); 
    }
    
    /**
    @pre --
    @post Retorna el nombre de punts de recàrrega del mapa
    */
    public int nPuntsRecarrega(){
        return indexsPR.size();
    }
    
    /**
    @pre 0 <= i < nLocalitzacions
    @post Retorna la localització d'índex i
    */
    public Localitzacio loc(int i) {      
        return localitzacions.get(i);
    }
        
    /**
    @pre 0 <= origen < localitzacions.size()
    @post Calcula la taula de distàncies i previs de Dijkstra des de l'origen
    */
    private void Dijkstra(int origen) throws Exception{
        ArrayList<Pes> dist;
        dist = new ArrayList<Pes>(localitzacions.size());
        ArrayList<Integer> prev;
        prev = new ArrayList<Integer>(localitzacions.size());
        TreeSet<Integer> Q = new TreeSet<>;
        
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
                if(aux<0 || aux>=dist.size()){
                    throw new Exception("Aux = " + aux + "dist size = " + dist.size());
                }
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
    
    /**
    @pre --
    @post Calcula la matriu de distàncies i previs per Dijkstra
    */
    private void Dijkstra() {
        for (int i=0; i<localitzacions.size(); i++){
            try {
                Dijkstra(i);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    
}
