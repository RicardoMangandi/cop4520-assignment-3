## How to Run Program:

1. Open a terminal
2. Run the following command to compile the program:
```bash
javac Main.java
```
3. Run the following command to execute the program:
```bash
java Main
```
## Program Design:

### Initialization:
To simulate the problem scenario I first initialized a list of unordered items. The list contain unique integer values
from 0 to ```NUM_OF_PRESENTS```. To ensure a form of sychronization was followed the Collections library was called on it
which makes the list thread safe to accomplish any sort of operations on it.

### Retrieval of items from the bag:
The removal and retrieving items from the list had a run time of O(1) due to always taking from index 0. 


### Task 0,2,3:

To decide what task a servant would do at a given time they were randomly assigned their task with the Java randomizer
class which gave each thread a task to accomplish. 

Task 0 - Retrieve item from unordered list and insert node into concurrent linked list
Task 1 - Remove node from concurrent linked list and "write a thank you letter"
Task 2 - Search for a node

The randomization of the problem slows down the runtime dramatically. Due to never exactly knowing when a random node 
value will be picked that is actually in the list. Or even looking an item up seems to be a waste of resource and time.

A different alternative would be to replace the first item from the list, but that would defeat the purpose of the
problem statement. The problem statement states that the Minotaur will ask the servant to look for an item meaning
he never specifies what item and it possible that the item could not be in the list.


### Concurrent Linked List

The concurrent linked list is based on the ```Operations.java``` file. There you will find the standard add function,
remove function, and contains function. The validate function is a helper function that ensures the proper removal 
and addition of nodes. It returns a boolean based if the nodes predecessor and current are marked and tagged.

### Conclusion

The usage of atomic variables and usage of the keyword syncronization helped the problem come to a correct conclusion.
Whenever the concurrent linked list is empty AND the number of written thank you card reaches 500,000 the program
comes to an end. 

Please note at the moment I have my program set to 50,000 presents due to the runtime being undefined for 500,000 
due to the way the program randomizes the search and removal of elements which causes the runtime to become O(n^2).


