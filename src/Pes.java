/**
@class Pes
@brief Classe que conté el temps i distància d'una connexió
@author Xavier Rodríguez i Martínez
 */
public class Pes implements Comparable<Pes> {
    private final float dist;
    private final Temps temp;
    
    /**
    @pre --
    @post Constructor per defecte
    */    
    Pes(){
        dist = 0;
        temp = new Temps(0,0);
    }
    
    /**
    @pre {d,t} >= 0
    @post Pes creat segons paràmetres
    */    
    Pes(float d, Temps t){
        this.dist = d;
        this.temp = t;
    }
    
    /**
    @pre --
    @post Retorna el valor de distància
    */
    public float distancia(){
        return dist;
    }
    
    /**
    @pre --
    @post Retorna el cost en temps
    */
    public Temps temps(){
        return temp;
    }
    
    /**
    @pre --
    @post Se sumen els pesos de  p  a this
    */
    public Pes mes(Pes p){
        float d = dist+p.dist;
        Temps t = temp.mes(p.temp);
        return new Pes(d,t);
    }
    
    /**
    @pre --
    @post Es resten els pesos de  p  a this.
    */
    public Pes menys(Pes p){
        float d = dist-p.dist;
        Temps t = temp.menys(p.temp);
        return new Pes(d,t);
    }
    
    @Override
    public int compareTo(Pes p){
        return (int) ((int) this.dist - p.dist);
    }
    
    @Override
    public String toString(){
        return " Km: " + dist + "   Temps: " + temp;
    }
}
