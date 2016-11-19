package Tests;


import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.junit.runner.*;

public class ParallelTests {  

   @Test  
   public void test() {      
      Class[] cls = { KonkurrenceRandomMapTests.class,
    		  		  ObligatoriskMapTests.class,
    		  		  SimpleTests.class,
    		  		  TestMaps.class };

      //Parallel all methods in all classes  
      Result fish = JUnitCore.runClasses(new ParallelComputer(true, true), cls); 
      assertEquals(0, fish);
   } 
} 
