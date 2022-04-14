## How to Run Program:

1. Open a terminal
2. Run the following command to compile the program:
```bash
javac TempReader.java
```
3. Run the following command to execute the program:
```bash
java TempReader
```

## Program Design:

### Simulation:
The problem states to run the program for an hour to simulate this for the purpose of the assignment the program runs
for ```One Minute``` and it takes temperature readings every ```One Second```. To run for an hour modify the ```RUN_FOR_IN_SECONDS```
variable and modify ```Thread.sleep(1000)``` to take temperature readings every n seonds/minutes/etc.


### Collection of Data:
The collection of data is done by the usage of Java's ```TreeSet``` which allows for every insertion to be automatically
sorted, this also allows us to elimate redudant values that could potentially be generated. Every time a sensor/thread
gets dispatched and enters the critical section is when they are able to retrieve the random temperature. To ensure 
synchronization this is done by using a synrhonized method:

```Java
    public static synchronized void insertIntoTreeSet()
    {       
            Random randomTempGenerator = new Random();
            int currentTemp = randomTempGenerator.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
            TempReader.collectsAllReadings.add(currentTemp);
            TempReader.containsReadings.get(TempReader.containsReadings.size() - 1).add(currentTemp);
    }
```
This allows the insertion into two TreeSets to be threadsafe. The first TreeSet continously collects all readings.
The second TreeSet gets the current index we are at and adds an element into the next available index in the TreeSet.

A representation of my data structure is a List of TreeSets. Each index contains a TreeSet and each TreeSet is of size
8 due to there being 8 threads running at a time and reading temperatures.

<img width="332" alt="Screen Shot 2022-04-13 at 11 17 28 PM" src="https://user-images.githubusercontent.com/62866287/163307135-da4a87a9-add4-4227-983d-3663aef3bbff.png">


### Proof of Correctness:
Each thread is expected to go inside the critical section and there are 8 threads and this simulation lasts for
```One Minute``` and they are currently dispatched every ```One Second```. So I have a counter that counts the 
numerous times a thread goes inside the critical section to ensure proper synchronization. This counter should
always give me 480 due to 8 * 60 = 480. So there are 60 total iterations. This also means that my List of TreeSet
is always going to be of size 60.


### Collecting End Results:
I would say that my program definately lacks efficiency in regards to memory. I allocate tons of memory to accomplish
simple tasks. However, at the end I am able to get the result after one minute. The reason why it not very efficient
is because the nature of TreeSets and how to iterate through them.

```Collecting Highest Temperatures:```
To collect the highest temperatures I had to loop through my list of TreeSets and get each individual TreeSet
and reverse the list to get the first 5 elements and collect those elements into another TreeSet which holds
the highest elements from each index of the list. Finally I would need to retrieve the last five elements
from the newly created sorted list.

```Collecting Lowest Temperatures:```
To collect the lowest temperatures I had to loop through my list of TreeSets and get each individual TreeSet
and get the first 5 elements and continue to collect those elements into another TreeSet which holds lowest elements
from each index of the list. Finallu I would need to retrieve the first five elements from the newly created sorted list.


```Collecting Intervals:```
To collect intervals I kept checking when my list of TreeSets had a size that by doing the ```mod``` operation
would result in 0

```Java
 if(containsReadings.size() % 10 == 0)
 {   
     int res = collectsAllReadings.last() - collectsAllReadings.first();
     temperatureDifferenceCollected.add(Math.abs(res));
 }

```
This would result in a final TreeSet that would contain the difference of the intervals that I wanted. The code states
that I would retrieve the last item and the first item from my collected interval. This would always result
in the TreeSet being less than size or equal to size 6 because the TreeSet would eliminate redundant values.











