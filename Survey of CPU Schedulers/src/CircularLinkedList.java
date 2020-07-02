import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class CircularLinkedList<AnyType> implements List<AnyType>
{
	//Cannot modify the class below
  private static class Node<AnyType>
  {
    private AnyType data;
    private Node<AnyType> next;

    public Node(AnyType d, Node<AnyType> n)
    {
      setData(d);
      setNext(n);
    }

    public AnyType getData() { return data; }

    public void setData(AnyType d) { data = d; }

    public Node<AnyType> getNext() { return next; }

    public void setNext(Node<AnyType> n) { next = n; }
  } //end Node class

  private int theSize; //Will always be one greater than the greatest index value
  private int modCount; 
  private Node<AnyType> tail;

  public CircularLinkedList()
  {
	  theSize = 0;
	  modCount = 0;
	  tail = null;
  }

  public void clear() //Remove all values from the list, creating an empty list.
  {
	  /*Since garbage collection (memory management) is handled by the Java Virtual Machine, there is no need to 
	   * delete every element in the linked list individually. If we set the tail pointer to null, we will lose
	   * the only reference we have to the list, and will simutaneously makes the tail available to form a new list.
	   */
	  tail = null;
	  theSize = 0;
	  modCount++;
  }

  public int size() //Return the number of data values currently in the list
  {
	  return theSize;
  }

  public boolean isEmpty() //Return true if the list is empty; otherwise return false.
  {
	  if (tail == null) //If the tail is not pointing to any element, return true.
	  {
		  return true;
	  }
	  else //The tail is pointing to an element, indicating that there is a node in the linked list and it is not empty.\
	  {
		  return false;
	  }

  }

  private Node<AnyType> goToIndex(int index) //Moves a pointer to a requested index location.
  {
 Node<AnyType> temp = tail.getNext(); //Move the pointer to the beginning of the list.
	  
	  for(int i = 0; i < index; i++) //Navigate the temp pointer through the list until you reach the requested node.
	  {
		  temp = temp.getNext(); 
	  }
	  
	  return temp;
  }
  
  private Node<AnyType> goToIndexBefore(int index) //Moves a pointer to the node before a requested location
  {
	  Node<AnyType> temp = tail.getNext(); //Move the pointer to the beginning of the list.
	  
	  for(int i = 0; i < index-1; i++) //Navigate the temp pointer through the list until you reach the node before the requested node.
	  {
		  temp = temp.getNext(); 
	  }
	  
	  return temp;
  }
  
  public AnyType get(int index) //Returns the data of a node given an index as an argument.
  {
	  if(index >= 0 && index <= theSize-1)
	  {
	  //Use the goToIndex method to traverse to the requested position and return the data.
	  return goToIndex(index).getData();
	  }
	  else
		  throw new NoSuchElementException("The provided index is not in the linked list!");
  }

  public AnyType set(int index, AnyType newValue) //Return the old data value at position index and replace it with the data value newValue.
  {
	  	if(index >= 0 && index <= theSize-1)
	  	{
	  	AnyType oldData = get(index); //Extract the original data from the requested index.
	  	goToIndex(index).setData(newValue); //Update the data value at the requested index.
	  	return oldData; //Return the old data 
	  	}
	  	else
	  	{
	  		throw new NoSuchElementException("The provided index is not in the linked list!");
	  	}
  }

  public boolean add(AnyType newValue)
  {
    add(size(), newValue);
    return true;
  }

  public void add(int index, AnyType newValue)
  {
	  if(isEmpty()) //The list is empty so we are adding the first node to the list regardless of the index in which the user wishes to enter the value.
	  {
	 //Could also check if theSize == 0
		Node<AnyType> newNode = new Node<AnyType>(newValue, null); //Create a new node and have its next point to tail since it is the first in the list.
		newNode.setNext(newNode); //Have the first node's next pointer point to itself 
		tail=newNode; //Have tail point to the newNode since it is the only one in the list.
		theSize++; //Increase the size of the linked list by 1
		modCount++;
	  }
	  else //The list is not empty so we are adding to a linked list with at least one node
	  {
		  //Case 1 - Adding to the beginning of the linked list (index 0)
		  if(index == 0)
		  {
			 addToBeginning(newValue);
		  }
		  //Case 2 - Adding to the end of the list. 
		  else if(index==theSize) //Since the index is always theSize-1, when index == theSize we are adding to the end of the list. 
		  {
			  addToEnd(newValue);
		  }
		  //Case 3 - We are adding a node to somewhere in the middle of the list
		  else if (index > 0 && index < theSize)
		  {
			addToMiddle(newValue, index);  
		  }
		  
		  else //The given index is out of bounds
		  {
			  throw new NoSuchElementException("The index provided is out of bounds!");
			  
		  }
	  }
  }

  private void addToBeginning(AnyType newValue)
  {
	  /*We can use tail.getNext() to navigate to the first element in the linked list
	   * even if there is one node in the list, tail.getNext() will bring us to the only 
	   * element in the list*/
	  Node<AnyType>newNode = new Node<AnyType>(newValue, tail.getNext());
	  tail.setNext(newNode); // Set the last node of the linked list's next to point to the new first element in the list
	  theSize++; //Increase the size of the linked list by 1
	  modCount++;
  }

  private void addToEnd(AnyType newValue)
  {
	  Node<AnyType>newNode = new Node<AnyType>(newValue, tail.getNext());
	  newNode.setNext(tail.getNext());
	  tail.setNext(newNode); //The original tail's next must now point to this new element to keep the list linked
	  tail = newNode; //New node as added on the end, so it becomes the new tail.
	  theSize++; //Increase the size of the linked list by 1
	  modCount++;
  }

  private void addToMiddle(AnyType newValue, int index)
  {
	//Create a temporary pointer (reference object) to locate the position we need to insert the new node.
	  Node<AnyType> temp = goToIndexBefore(index);
	  Node<AnyType>newNode = new Node<AnyType>(newValue,temp.getNext()); //The new node's next is equal to the temporary reference object's next.
	  temp.setNext(newNode); // Insert the new node into the linked list by having temporary's next be equal to it.  
	  theSize++; //Increase the size of the linked list by 1
	  modCount++;
  }
  
  public AnyType remove(int index) //Remove and return the data value at position index in the list.
  {
	  AnyType oldData = null;
	  if (isEmpty()) //The list is empty, so there is nothing to remove.
	  {
		throw new NoSuchElementException("The list is empty so nothing can be removed!");  
		
	  }
	  else //The list isn't empty so we can remove an item.
	  {
		  //Case 1 - We are looking to remove the first node in the list.
		  if(index == 0)
		  {
			  oldData = tail.getNext().getData(); //Extract the data from the first node.
			  removeFirst();
		  }
		  else if(index > 0 && index < theSize-1) //Case 2 - We are removing a node in the middle of the linked list.
		  {
			  oldData = get(index); //Extract the data from the node to be deleted.
			  removeMiddle(index);			 
		  }
		  else if(index == theSize-1) //We are removing from the last element in the list.
		  {
			  oldData = tail.getData(); 
			  removeEnd(index);
		  }
		  else { //The given index is out of bounds.
			  throw new NoSuchElementException("The provided inbox is out of bounds! No element was removed!");
		  }
		  	  }
	  
	  return oldData;
  }
  
  private void removeFirst()
  {
	  if(tail.getNext() != tail) //Check to make sure we aren't deleting the only node in the list.
	  {
		
	  tail.setNext(tail.getNext().getNext()); //Set the next of the tail to the second element in the list.
	  }
	  else //We are deleting the last node in the list.
	  {
		  tail = null; //Have the tail point to null since there are no elements remaining.
	  }
	  theSize--;
	  modCount++;
  }

  private void removeMiddle(int index)
  {
	  Node <AnyType> current = goToIndexBefore(index);
	  current.setNext(current.getNext().getNext()); //Set the next pointer of the node before the node to be deleted to the node after the node to be deleted.
	  theSize--;
	  modCount++;
  }
  
  private void removeEnd(int index)
  {
	  goToIndexBefore(index).setNext(tail.getNext()); //Set the next of the second to last element to the next of the tail.
	  tail = goToIndexBefore(index); //Set the tail to the second to last element of the list.
	  theSize--;
	  modCount++;
  }
  public void rotate() //Moves the value at the head of the list to the tail of the list without removing and adding anything to the list.
  {
	  AnyType headData = get(0);
	  set(size()-1, headData); //Set the head data to the tail.
  }

  public Iterator<AnyType> iterator()
  {
    return new LinkedListIterator();    
  }

  private Node<AnyType> getNode(int index)
  {
    return (getNode(index, 0, size()-1));
  }

  private Node<AnyType> getNode(int index, int lower, int upper) //Return the pointer to the node at position index in the list.
  {
	if(index >= lower && index <= upper) //Check if the requested index is in the bounds of the linked list.
	{
		 return goToIndex(index);
	}
	else
	{
		throw new NoSuchElementException("The provided index is out of bounds!");
	}
  }

  private class LinkedListIterator implements Iterator<AnyType>
  {
    private Node<AnyType> previous;
    private Node<AnyType> current;
    private int expectedModCount;
    private boolean okToRemove;

    LinkedListIterator()
    {
    	/*Since the iterator is being used on a Circular Linked List we can set the
    	 previous to the last node (the tail) and the current to the first element in the linked list.
    	 Since a pointer always goes from the last element in the list to the first,having them in this position
    	 will allow us to traverse current from the first position (index 0) in the circular linked list*/ 
    	previous = tail;
    	current = tail.getNext();
    	expectedModCount = modCount; //Set the expectedModCount to the current modCount of the linked list
    	okToRemove = true;
    }

    public boolean hasNext() //Returns true if the iteration has more elements.
    {
    	if(!isEmpty()) //Since the list is circular, there is always a next value as long as the list is non-empty.
    	{
    		return true;
    	}
    	else 
    	{
    		return false;
    	}
    }

    public AnyType next() //Returns the next element in the iteration.
    {
    	if(hasNext())
    	{
    		previous = current; //Move the previous pointer to the current location.
    		current = current.getNext(); //Move the current pointer to the next element in the list.
    		return current.getData(); //Return the "next" element in the  
    	}
    	else
    	{
    		throw new NoSuchElementException("Cannot obtain an element as the list is empty!");
    	}
    }
    public void remove() //Removes from the underlying list the last element returned by the Iterator
    {
    	
    	//Looking at the implementation for next, we can see that the element returned is pointed to by previous.
    	//Therefore, we must remove current.
    	if(modCount != expectedModCount) //A change has been made since the iterator was declared, therefore we cannot make any changes.
    	{
    		okToRemove = false;
    		throw new ConcurrentModificationException();
    	}
    	if(okToRemove)
    	{
    	current = current.getNext();
    	previous.setNext(current); //Set the node prior to the one being removed so that next points to the new current.
    	//The original current is no longer in the linked list.
    	}
    }
  }
}