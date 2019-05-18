
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
        System.out.println("Concatenar:");
        System.out.println(this);
        System.out.println(r);
        pes = pes.mes(r.pes);
        Iterator<Integer> ite = r.cami.iterator();
        if (ite.next() != r.cami.getLast()) throw new Exception("Les rutes no coincideixen");
        while (ite.hasNext())
            cami.add(ite.next());
        System.out.println("Resultat:");
        System.out.println(this);
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
