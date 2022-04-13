public class Operations {

	private Node head;

	public Operations()
	{
		this.head = new Node(Integer.MIN_VALUE);
		this.head.next = new Node(Integer.MAX_VALUE);
	}

    
    private boolean validate(Node pred, Node curr)
	{
		return !pred.marked && !curr.marked && pred.next == curr;
	}

	public boolean add(int item) {
		while (true) 
		{
			Node pred = this.head;
			Node curr = head.next;

			//list traversal
			while (curr.uniqueNum < item) 
			{
				pred = curr;
				curr = curr.next;
			}

			pred.lock();

			try 
			{
				curr.lock();
				try 
				{
					if (validate(pred, curr))
					 {	
						//if node is already in list return false
						if (curr.uniqueNum == item) 
						{ 
							return false;
						} 

						//node is not list so we will allocate memory and insert node
						else 
						{ 
							Node newNode = new Node(item);
							newNode.next = curr;
							pred.next = newNode;
							
							return true;
						}
					}
				} 
				//unlock current node
				finally 
				{
					curr.unlock();
				}
			} 
			//unlock previous node
			finally 
			{ 
				pred.unlock();
			}
		}
	}

	public boolean remove(int item) {
	
		while (true)
		 {
			Node pred = this.head;
			Node curr = head.next;
			//list traversal find node
			while (curr.uniqueNum < item) 
			{
				pred = curr;
				curr = curr.next;
			}

			pred.lock();
			
			try 
			{
				curr.lock();
				try 
				{	
					if (validate(pred, curr)) 
					{	
						//item is not present cannot remove
						if (curr.uniqueNum != item)
						{ 
							return false;
						} 

						//item is present remove item
						else 
						{ 
							curr.marked = true; 
							pred.next = curr.next; 
							return true;
						}
					}
				} 
				finally 
				{ 
					curr.unlock();
				}
			} 
			finally 
			{ 
				pred.unlock();
			}
		}
	}


	public boolean contains(int item) {
		
		Node curr = this.head;
		while (curr.uniqueNum < item)
			curr = curr.next;
		return curr.uniqueNum == item && !curr.marked && !curr.tagged;
	}
		
	

}
