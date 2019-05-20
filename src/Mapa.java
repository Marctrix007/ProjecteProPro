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
    
    private ArrayList<Localitzacio> localitzacions; /** Localitzacions que formen el mapa */
    private ArrayList<Map<Integer,Pes>> connexions; /** Connexions dirigides entre les localitzacions */
    
    private TreeSet<Integer> indexsPR; /** Set d'índexos dels punts de recàrrega */
    private ArrayList<ArrayList<Pes>> distancies; /** Matriu de pesos de origen a destí. Resultat de Dijkstra */
    private ArrayList<ArrayList<Integer>> previs; /** Matriu de punts previs per arribar de origen a destí. Resultat de Dijkstra */
    boolean esFinal; /** Índica si les dades calculades per Dijkstra són vàlides o no */
    
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
    @pre 0 <= {A,B} < nLocalitzacions
    @post Mapa buit
    */
    public Pes pesEntre(Integer A, Integer B){
        return distancies.get(A).get(B);
    }
    
    public void mostrar(){
        for(int i=0; i<distancies.size(); i++){
            System.out.println("Linia: "+i);
            for(int j=0; j<distancies.get(i).size(); j++){
                System.out.println(distancies.get(i).get(j).toString());
            }
        }
        for(int i=0; i<previs.size(); i++){
            System.out.println("Linia: "+i);
            for(int j=0; j<previs.get(i).size(); j++){
                System.out.println(previs.get(i).get(j).toString());
            }
        }
        System.out.println("PRS:");
        Iterator<Integer> ite = indexsPR.iterator();
        while(ite.hasNext()) System.out.println(ite.next());
    }
    
    /**
    @pre --
    @post Afegeix la localització al mapa
    */
    public void AfegirLocalitzacio(Localitzacio l){
        localitzacions.add(l);
        connexions.add(new HashMap());
        if(!distancies.add(new ArrayList<Pes>())) System.out.println("Distancies mal creades");
        previs.add(new ArrayList<Integer>());
        esFinal = false;
        if (l.esPuntDeRecarrega())
            indexsPR.add(localitzacions.size()-1);
    }
    
    /**
    @pre 0 <= {o,d} < nLocalitzacions
    @post Crea una connexió entre la localització origen i la localització desti amb pes p, si ja existeix la modifica amb el nou pes.
    */
    public void AfegirConnexio(int o, int d, float dist, Temps t) throws IndexOutOfBoundsException{       
        if (localitzacions.size()<=o || localitzacions.size()<=d)
            throw new IndexOutOfBoundsException("Mida: "+localitzacions.size()+" Valors: "+o+", "+d);
        connexions.get(o).put(d, new Pes(dist, t));
        esFinal = false;
    }
    
    /**
    @pre 0 <= loc < nLocalitzacions
    @post Retorna la cua de punts de recàrrega ordenats per proximitat a loc (de PR a loc)
    */
    public ArrayDeque<Integer> PRMesProximA(int loc) throws Exception{
        if (localitzacions.size()<loc)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
        return PRMesProxim(loc,false);
    }
    
    /**
    @pre 0 <= loc < nLocalitzacions
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
        int aux, PRmin;
              
        while (!iPR.isEmpty() && ret.size()<indexsPR.size()){
            PRmin = -1;
            pmin = new Pes(2000, new Temps(24,00));
            Iterator<Integer> ite = iPR.iterator();
            while (ite.hasNext()){
                aux = ite.next();
                if (dir) paux = distancies.get(loc).get(aux);
                else paux = distancies.get(aux).get(loc);
                if ( paux.compareTo(pmin) < 0){
                    pmin = paux;
                    PRmin = aux;
                }
            }
            if (PRmin==-1 && dir) throw new Exception("No existeixen PRs per anar desde " + loc);
            else if (PRmin==-1 && !dir) throw new Exception("No existeixen PRs per anar a " + loc);
            ret.addLast(PRmin);
            iPR.remove(PRmin);
        }
        Iterator<Integer> tier = ret.iterator();
        System.out.println("Mostrar llistat de PRs trobats");
        while(tier.hasNext()) System.out.print(tier.next()+" ");
        System.out.println(" ");
        return ret;
    }
    
    /**
    @pre 0 <= {o,d} < nLocalitzacions
    @post Retorna la ruta amb el camí mínim desde  o  fins a  d
    */
    public Ruta CamiMinim(int o, int d) throws Exception{
        //Pre: {o,d} < localitzacions.size()
        //Post: Retorna la ruta de distància mínima entre  o  i  d.
        if (localitzacions.size()<o || localitzacions.size()<d)
            throw new Exception("Localització no existent");
        if (!esFinal) Dijkstra();
        
        //System.out.println("Ruta de " + o + " a " + d);
        
        Ruta r = new Ruta();
        r.afegirPes(distancies.get(o).get(d));
        int aux = d;
        while (aux != o){
            r.addFirst(aux);
            aux = previs.get(o).get(aux);
        }
        r.addFirst(o);
        //System.out.println(r);
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
    @pre 0 <= origen < nLocalitzacions
    @post Calcula la taula de distàncies i previs de Dijkstra des de l'origen
    */
    private void iDijkstra(int origen) throws Exception{
        ArrayList<Pes> dist;
        dist = new ArrayList<Pes>(localitzacions.size());
        ArrayList<Integer> prev;
        prev = new ArrayList<Integer>(localitzacions.size());
        TreeSet<Integer> Q = new TreeSet<>();
        
        Pes INFINIT = new Pes(2147483647, new Temps(24,00));
        
        //S'inicalitza tota la taula amb distància infinita
        for (int v=0; v < localitzacions.size(); v++){
            dist.add(v, INFINIT);
            prev.add(v, -1);
            boolean add = Q.add(v);
            if (!add) throw new Exception("Q falla");
        }
        //distancia d'origen a origen es marca com a 0
        dist.set(origen, new Pes());
        prev.set(origen, origen);
                
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
            
            //Si la distància de u als seus veïns és inferior que la que hi havia s'actualitza i es posa u com a previ
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
        //Al acabar es guarden les distancies de origen als punts i la taula de previs
        distancies.set(origen, dist);
        previs.set(origen, prev);
    }
    
    /**
    @pre --
    @post Calcula la matriu de distàncies i previs per Dijkstra
    */
    private void Dijkstra() {
        for (int i=0; i<localitzacions.size(); i++){
            try {
                iDijkstra(i);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        //Es marca que s'ha acabat de calcular Dijkstra i per tant les dades estan actualitzades
        esFinal = true;
    }
    
}
