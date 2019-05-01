/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proves.fitxers;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


/**
 *
 * @author qsamb
 */
public class ProvesFitxers {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception{
            
        System.out.println("---DoubleSummaryStatistics---");		
        DoubleSummaryStatistics dstats = DoubleStream.of(5.33,2.34,5.32,2.31,3.51).collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
	System.out.println("Max:"+dstats.getMax()+", Min:"+dstats.getMin());
	System.out.println("Count:"+dstats.getCount()+", Sum:"+dstats.getSum());
	System.out.println("Average:"+dstats.getAverage());	
        
            
        
    }
    
}
