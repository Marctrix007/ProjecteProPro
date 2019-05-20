
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;

/**
@class Ruta
@brief Classe que conté un recorregut
@author Xavier Rodríguez i Martínez
 */
public class Ruta {
    
    private Pes pes; /** Pes de la ruta */
    private ArrayDeque<Integer> cami; /** Índexos de les localitzacions per les que passa ruta */
    
    //INVARIANT: pes >= Pes(0,0)
    
    /**
    @pre --
    @post Constructor per defecte
    */
    Ruta(){
        cami = new ArrayDeque<>();
        pes = new Pes();
    }
    
    /**
    @pre --
    @post u  afegit al principi del camí
    */
    void addFirst(int u) {
        cami.addFirst(u);
    }
    
    /**
    @pre --
    @post u  afegit al final del camí
    */
    void addLast(int u) {
        cami.addLast(u);
    }

    /**
    @pre --
    @post Es suma el  p  al pes de la ruta
    */
    void afegirPes(Pes p) {      
        pes = pes.mes(p);
    }
    
    /**
    @pre L'última localitació de  this  i la primera de  r són iguals
    @post Es concatenen la ruta actual i  r  afegint aquesta al final
    */
    public void Concatenar(Ruta r) throws Exception { 
        /*System.out.println("Concatenar:");
        System.out.println(this);
        System.out.println(r);*/
        pes = pes.mes(r.pes);
        Iterator<Integer> ite = r.cami.iterator();
        if (!cami.isEmpty()) {
            Integer a = ite.next();
            if (!Objects.equals(cami.getLast(), a)) 
                throw new Exception("Les rutes no coincideixen, darrer de this " + r.cami.getLast() + " primer de r " + a);
        }
        while (ite.hasNext())
            cami.add(ite.next());
        /*System.out.println("Resultat:");
        System.out.println(this);*/
    }
    
    /**
    @pre --
    @post Retorna el primer element de la ruta, null si és buida
    */
    public Integer primerElement(){
        return cami.peekFirst();
    }
    
    /**
    @pre --
    @post Retorna la darrera localització de la ruta, null si és buida
    */
    public int ultimaLoc() {
        return cami.peekLast(); 
    }
    
    /**
    @pre --
    @post Retorna cert si la ruta conté el recorregut de  r
    */
    public boolean Conte(Ruta r){       
        boolean primer = false, desti = false;
        Integer cap = r.cami.peekFirst();
        Integer cua = r.cami.peekLast();
        
        Iterator<Integer> ite = cami.iterator();
        
        if (cap==null || cua==null) return true;
        
        while (ite.hasNext() && !primer){
            if (cap==ite.next()) primer = true;
        }
        
        if (primer){
            while (ite.hasNext() && !desti){
                if (cua==ite.next()) desti = true;
            }
        }
        
        return primer && desti;
    } 
    
    /**
    @pre --
    @post Retorna un iterador del camí ordenat d'origen a destí
    */
    public Iterator<Integer> iterator(){
        return cami.iterator();
    }
    
    /**
    @pre --
    @post Retorna el pes de la ruta
    */
    public Pes cost() {
        return pes; 
    }
    
    /**
    @pre --
    @post Retorna la distància de la ruta
    */
    public double kmFets(){
        return pes.distancia();
    }
    
    /**
    @pre --
    @post Retorna el temps en format double
    */
    public double tempsRuta()
    {
        return pes.temps().conversioDouble();
    }
    
    /**
    @pre --
    @post Retorna un copia de this
    */
    public Ruta copia(){
        Ruta copia = new Ruta();
        copia.cami = cami.clone();
        copia.pes = pes;
        return copia;
    }
    
    /**
    @pre --
    @post Retorna ture si la ruta és buida, fals altrament
    */
    public boolean buida(){
        return cami.isEmpty();
    }
    
    /**
    @pre --
    @post Retorna la primera localització i l'elimina, retorna null si la ruta és buida
    */
    public Integer treureActual(){
        return cami.pollFirst();
    }
   
    /**
    @pre --
    @post Retorna el nombre de localitzacions pels que passa la ruta
    */
    public int mida(){
        return cami.size();
    }
    
    @Override
    public String toString(){
        String s = "[ ";
        for(Integer i:cami){
            s = s + i + " ";
        }
        s = s + "] Pes: " + pes.toString();
        return s;
    }
}
