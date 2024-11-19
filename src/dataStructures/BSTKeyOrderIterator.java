package dataStructures;
import Exceptions.*;

class BSTKeyOrderIterator<K extends Comparable<K>,V> implements Iterator<Entry<K,V>> {

	
	protected BSTNode<Entry<K,V>> root;

	protected Stack<BSTNode<Entry<K,V>>> p;


	BSTKeyOrderIterator(BSTNode<Entry<K,V>> root) throws FullStackException {
		this.root=root;
		this.p = new StackInList<>();
		rewind();
	}

	/**
	 * Best Case: O(1)
	 * Worst Case: O(n)
	 * Expected Case: O(log(n))
	 * @param node
	 */
	private void pushPathToMinimum(BSTNode<Entry<K,V>> node) throws FullStackException {
		while(node != null){
			p.push(node);
			node = node.left;
		}

	}

	//O(1) para todos os casos
	public boolean hasNext(){
		 return !p.isEmpty();
	 }


    public Entry<K,V> next( ) throws NoSuchElementException, FullStackException, EmptyStackException {
    	if (!hasNext()) throw new NoSuchElementException();
    	else {
			BSTNode<Entry<K,V>> toReturn = p.pop();
			pushPathToMinimum(toReturn.right);
			return toReturn.element;
    	}
    }

    public void rewind( ) throws FullStackException {
		p = new StackInList<>();
    	pushPathToMinimum(root);
    }
}
