import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


class Servants extends Thread
{   

    public Servants(int threadId)
    {
        this.threadId = threadId;   
    }

    int threadId;
    int assignedTask = -1;
    public static AtomicInteger numOfThankYouLetters = new AtomicInteger(0);
    public static AtomicInteger sizeOfLinkedListBuild = new AtomicInteger(0);

    public static AtomicInteger numOfCorrectSearchesInBuild = new AtomicInteger(0);
    public static AtomicInteger numOfTotalSearchesInBuild = new AtomicInteger(0);


    public static AtomicBoolean busyInserting = new AtomicBoolean(false);
    public static AtomicBoolean busyRemovingAndWriting = new AtomicBoolean(false);
    public static AtomicBoolean busyChecking1 = new AtomicBoolean(false);
    public static AtomicBoolean busyChecking2 = new AtomicBoolean(false);

    Random randomTask = new Random();
    Random randomPresent = new Random();

    

    public static synchronized int getVal()
    {   
        if (!Main.presentListUnordered.isEmpty())
        {
           int item = Main.presentListUnordered.remove(0);
           return item; 
        }
        
        else
        {
            return -9999;
        }
        
    }





    @Override
    public void run()
    {
       //a thread/servant can do the following actions:

       //0. Get a present from the unordered list and insert the present into ordered list
       //1. Remove a Node from Linked List and write a thank you letter
       //2. Check if present is in Linked List
        
       int flag = 1;
       while (flag == 1)
        {   
            //thread safe operation
            assignedTask = randomTask.nextInt(3);
            
            //thread 0 = 
            //thread 1 = 
            //thread 2 = 
            //thread 3 = 
          

            //System.out.println(assignedTask);

            if (assignedTask == 0)
            {   
                int val = getVal();
               // System.out.println("item popped: "+ val+" by threadId: "+threadId);
                if (val != -9999)
                {
                    
                    if(Main.linkedListOperations.add(val))
                    {
                        //System.out.println("Item added to list by threadId: "+ threadId);
                        sizeOfLinkedListBuild.incrementAndGet();
                        
                    }
                    
                }

                //check how big the linked list is
                if (sizeOfLinkedListBuild.compareAndSet(0, 0) && numOfThankYouLetters.compareAndSet(Main.NUM_OF_PRESENTS, Main.NUM_OF_PRESENTS))
                {   
                    synchronized(this)
                    {
                       flag = -1; 
                    }
                    
                }
                
                else
                {
                    synchronized(this)
                    {
                       flag = 1; 
                    }
                }
                busyInserting.set(false);
            }
            
            //remove node from linked list and write a thank you letter
            else if (assignedTask == 1)
            {   
                //probably change
                int presentItem = randomPresent.nextInt(Main.NUM_OF_PRESENTS);
                //int presentItem = sizeOfLinkedListBuild.get() % 7;
                

                if(Main.linkedListOperations.remove(presentItem))
                {
                    //System.out.println("Item removed from list by threadId: "+threadId);
                    sizeOfLinkedListBuild.decrementAndGet();
                    numOfThankYouLetters.incrementAndGet();
                }
            
                else
                {
                    //System.out.println("Item was not removed by threadId: "+threadId);
                }

                if (sizeOfLinkedListBuild.compareAndSet(0, 0) && numOfThankYouLetters.compareAndSet(Main.NUM_OF_PRESENTS, Main.NUM_OF_PRESENTS))
                {   
                    synchronized(this)
                    {
                       flag = -1; 
                    }
                    
                }
                
                else
                {
                    synchronized(this)
                    {
                       flag = 1; 
                    }
                }

                busyRemovingAndWriting.set(false);
            }
            
             else
            {   
                //Minotaur requests that you check if a random gift is in the linked list build
                int presentItem = randomPresent.nextInt(Main.NUM_OF_PRESENTS);

                if (Main.linkedListOperations.contains(presentItem))
                {   
                    //System.out.println("Item does exist list by threadId "+threadId);
                    numOfCorrectSearchesInBuild.incrementAndGet();
                    numOfTotalSearchesInBuild.incrementAndGet();
                }
                else
                {   
                    //System.out.println("Item does NOT exist list by threadId "+threadId);
                    numOfTotalSearchesInBuild.incrementAndGet();
                }

                busyChecking1.set(false);
                busyChecking2.set(false);
            } 
        }
    }




}


public class Main {

    public static List<Integer> collectPresents()
    {
        List<Integer> listOfPresents = new ArrayList<>();

        for (int i = 0; i < Main.NUM_OF_PRESENTS; i++)
            listOfPresents.add(i);

        Collections.shuffle(listOfPresents);

        List<Integer> synchronizedList = Collections.synchronizedList(listOfPresents);

        return synchronizedList;
    }

 

    static final int NUM_OF_PRESENTS = 50_000;
    static final int NUM_OF_SERVANT_THREADS = 4;
    static List<Integer> presentListUnordered = null;
    public static Thread arrayOfServants [] = new Servants[NUM_OF_SERVANT_THREADS];
    
    public static Operations linkedListOperations = new Operations();

    public static void main(String [] args) throws InterruptedException
    {

        System.out.println("Initializing and getting all " + NUM_OF_PRESENTS + " presents...");
        
        presentListUnordered = collectPresents();
        

        if (presentListUnordered.size() == Main.NUM_OF_PRESENTS)
            System.out.println("Successfully initialized");
 
        else
        {
            System.out.println("The program was not initialized correctly, goodbye");
            System.exit(0);
        }
            
        
        

        //how to take a present from the unordered bag?
        //create a synchronized block to remove items from a list

        //add the removed item to the correct location in the list
        //locked syncrhonization needs to be implemented
        
        long startTime = System.nanoTime();
        for(int i = 0; i < NUM_OF_SERVANT_THREADS; i++)
        {
            Thread aServantThread = new Servants(i);
            aServantThread.start();
            arrayOfServants[i] = aServantThread;
        }

        
        
        for(Thread aThread : arrayOfServants)
            aThread.join();
        
        long endTime = System.nanoTime();
        long totalTime = (endTime - startTime);
        
        if (Servants.numOfThankYouLetters.compareAndSet(NUM_OF_PRESENTS, NUM_OF_PRESENTS) && Servants.sizeOfLinkedListBuild.compareAndSet(0, 0))
        {   
            System.out.println("All the thank you letters have been generated");
            System.out.println("The number of thank you letters generated: "+ Servants.numOfThankYouLetters);
            System.out.println("The size of the concurrent linked list is: " + Servants.sizeOfLinkedListBuild);

            System.out.println("There were "+ Servants.numOfCorrectSearchesInBuild+" out of "+Servants.numOfTotalSearchesInBuild +" correct searches");
        
            System.out.println();

            System.out.println("Total time: " +totalTime);
        }
        
        else
        {
            System.out.println("THERE WAS NO SYNCRHONIZATION!");
            System.out.println("The number of thank you letters generated: "+ Servants.numOfThankYouLetters);
            System.out.println("The size of the concurrent linked list is: " + Servants.sizeOfLinkedListBuild);
            System.out.println("There were "+ Servants.numOfCorrectSearchesInBuild+" out of "+Servants.numOfTotalSearchesInBuild +" correct searches");

        
            System.out.println();
            double time = (totalTime) /(Math.pow(10, 9));
            System.out.println("Total time in nano seconds: " +time);
        }
        
        
        
    }

}
