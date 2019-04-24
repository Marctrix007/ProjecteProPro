/**
 *
 * @author Xavier Rodríguez i Martínez
 */
public class Pes implements Comparable<Pes> {
    private final float dist;
    private final Temps temp;
    
    Pes(){
    //Pre: --
    //Post: Constructor per defecte
        dist = 0;
        temp = new Temps(0,0);
    }
    
    Pes(float d, Temps t){
    //Pre: {d,t} >= 0
    //Post: Pes creat segons paràmetres
        this.dist = d;
        this.temp = t;
    }
    
    public float distancia(){
    //Pre: --
    //Post: Retorna el valor de distància
        return dist;
    }
    
    public Temps temps(){
    //Pre: --
    //Post: Retorna el cost en temps
        return temp;
    }
    
    public Pes mes(Pes p){
    //Pre: --
    //Post: Es sumen els pesos de  p  a this.
        float d = dist+p.dist;
        Temps t = temp.mes(p.temp);
        return new Pes(d,t);
    }
    
    public Pes menys(Pes p){
    //Pre: --
    //Post: Es resten els pesos de  p  a this.
        float d = dist-p.dist;
        Temps t = temp.menys(p.temp);
        return new Pes(d,t);
    }
    
    @Override
    public int compareTo(Pes p){
        return (int) ((int) this.dist - p.dist);
    }
}
