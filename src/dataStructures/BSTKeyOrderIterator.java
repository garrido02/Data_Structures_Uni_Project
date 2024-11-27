package dataStructures;

import Exceptions.NoSuchElementException;

class BSTKeyOrderIterator<K extends Comparable<K>,V> implements Iterator<Entry<K,V>> {

	private static final long serialVersionUID = 0L;

	protected BSTNode<Entry<K,V>> root;

	protected Stack<BSTNode<Entry<K,V>>> p;


	BSTKeyOrderIterator(BSTNode<Entry<K,V>> root){
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
	private void pushPathToMinimum(BSTNode<Entry<K,V>> node) {
		while(node != null){
			p.push(node);
			node = node.left;
		}

	}

	//O(1) para todos os casos
	public boolean hasNext(){
		 return !p.isEmpty();
	 }


    public Entry<K,V> next( ) throws NoSuchElementException {
    	if (!hasNext()) throw new NoSuchElementException();
    	else {
			BSTNode<Entry<K,V>> toReturn = p.pop();
			pushPathToMinimum(toReturn.right);
			return toReturn.element;
    	}
    }

    public void rewind( ){
		p = new StackInList<>();
    	pushPathToMinimum(root);
    }
}
