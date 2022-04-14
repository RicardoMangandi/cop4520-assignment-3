import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;
class Readers extends Thread
{

    public static List<Integer> collectsTemps = new ArrayList<Integer>();
    public static AtomicInteger enteredCriticalSection = new AtomicInteger(0);
    public static final int LOWER_BOUND = -100;
    public static final int UPPER_BOUND = 70;    

    @Override
    public void run()
    {   
        insertIntoTreeSet();
        enteredCriticalSection.incrementAndGet();
    }

    public static synchronized void insertIntoTreeSet()
    {       
            Random randomTempGenerator = new Random();
            int currentTemp = randomTempGenerator.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
            TempReader.collectsAllReadings.add(currentTemp);
            TempReader.containsReadings.get(TempReader.containsReadings.size() - 1).add(currentTemp);
    }
}

public class TempReader {

    public static final int RUN_FOR_IN_SECONDS = 60;

    public static final int NUM_OF_READERS = 8;
    public static ArrayList<Thread> arrayOfReaders = new ArrayList<>(8);

    public static TreeSet<Integer> collectsAllReadings = new TreeSet<>();
    public static List<TreeSet<Integer>> containsReadings = new ArrayList<>();
    public static TreeSet<Integer> temperatureDifferenceCollected = new TreeSet<>();


    public static void main(String[] args)
    throws InterruptedException
    {

        for(int i = 0; i < NUM_OF_READERS; i++)
        {
            Thread aReaderThread = new Readers();    
            arrayOfReaders.add(aReaderThread);
        }
        


        final long NANOSEC_PER_SEC = 1000l*1000*1000;
        long starTime = System.nanoTime();
        
        containsReadings.add(new TreeSet<Integer>());


        while((System.nanoTime() - starTime) < RUN_FOR_IN_SECONDS*NANOSEC_PER_SEC)
        {   


            //start my 8 threads
            for(int i = 0; i < NUM_OF_READERS; i++)
            {   
                //collect 8 temperatures
                if(arrayOfReaders.get(i).getState().toString() == "NEW")
                {
                    arrayOfReaders.get(i).start();
                }
                
            }

            //join the first 8 threads
            for(Thread aThread : arrayOfReaders)
            {
                aThread.join();
            }

            //remove them all and clear memory
            arrayOfReaders.removeAll(arrayOfReaders);

            //allocate new threads
            for(int i = 0; i < NUM_OF_READERS; i++)
            {  
                arrayOfReaders.add(new Readers());
            }           

            //at every interval of size 10 I am collecting all values to find biggest difference
            if(containsReadings.size() % 10 == 0)
            {   
                int res = collectsAllReadings.last() - collectsAllReadings.first();
                temperatureDifferenceCollected.add(Math.abs(res));
            }

            Thread.sleep(1000);
            
            System.out.println(containsReadings.get((containsReadings.size() - 1)));       
            containsReadings.add(new TreeSet<Integer>());
            
        }

        //the last item does not get any elements inserted therefore it is useless
        containsReadings.remove(containsReadings.size() - 1);
        
        for(Thread aThread : arrayOfReaders)
            aThread.join();
        
        arrayOfReaders.removeAll(arrayOfReaders);
       

        TreeSet<Integer>highestVals = new TreeSet<>();
        TreeSet<Integer>lowestVals = new TreeSet<>();
        int counter = 0;
        int insideCounter = 0;

        while(counter < containsReadings.size())
        {
          TreeSet<Integer>getTreeList  = containsReadings.get(counter);
            
           for(Integer val : getTreeList)
           {
                if(insideCounter == 5)
                {
                    break;
                }
                else
                {
                    lowestVals.add(val);
                }

           }

           insideCounter = 0;

           NavigableSet<Integer> reverseList =  getTreeList.descendingSet();
           for(Integer val : reverseList)
           {
                if(insideCounter == 5)
                {
                break;
                }
                else
                {
                highestVals.add(val);
                }
           }
           
            insideCounter = 0;
            counter++;
        }

        

        counter = 0;
        System.out.println("============================================================");
        System.out.print("The highest temperatures recorded: ");
        NavigableSet<Integer> highestReverse = highestVals.descendingSet();
        for(Integer val : highestReverse)
        {   
            if(counter == 5)
            {
                break;
            }
            System.out.print(" "+val);
            counter++;
        }

        System.out.println();

        counter = 0;
        System.out.println("============================================================");
        System.out.print("The lowest temperatures recorded:");
        for(Integer val : lowestVals)
        {
            if(counter == 5)
            {
                break;
            }
            System.out.print(" " + val);
            counter++;
        }
        System.out.println();
        
        System.out.println("============================================================");
        System.out.println("Largest Temperature Difference within 10 minute intervals: "+temperatureDifferenceCollected.last());
        
        System.out.println("============================================================");
        System.out.println("Entered critical section : "+Readers.enteredCriticalSection);

   
    }
}
