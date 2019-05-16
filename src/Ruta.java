
import java.util.ArrayDeque;
import java.util.Iterator;

/**
@class Ruta
@brief Classe que conté un recorregut
@author Xavier Rodríguez i Martínez
 */
public class Ruta {
    
    private Pes pes;
    private ArrayDeque<Integer> cami;
    
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
    @post Es suma el  p  al pes de la ruta
    */
    void afegirPes(Pes p) {      
        pes = pes.mes(p);
    }
    
    /**
    @pre --
    @post Es concatenen la ruta actual i  r  afegint aquesta al final
    */
    public void Concatenar(Ruta r) {       
        pes = pes.mes(r.pes);
        Iterator<Integer> ite = r.cami.iterator();
        while (ite.hasNext())
            cami.add(ite.next());
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
        int cap = r.cami.peekFirst();
        int cua = r.cami.peekLast();
        
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
}
