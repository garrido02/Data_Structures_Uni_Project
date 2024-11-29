/**
 * Class Entry
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package dataStructures;
import Exceptions.EmptyStackException;
import java.io.Serial;

/**
 * Class StackInList responsible to implement the methods of the Stack Interface
 */
public class StackInList<E extends Comparable<E>> implements Stack<E>
{

    @Serial
    private static final long serialVersionUID = 0L;

    protected List<E> list;                     

    public StackInList( )
    {     
        list = new DoubleList<>();
    }

    public boolean isEmpty( )
    {     
        return list.isEmpty();
    }

    public int size( )
    {     
        return list.size();
    }

    public E top( ) throws EmptyStackException
    {     
        if ( list.isEmpty() )
            throw new EmptyStackException();
        
        return list.getFirst();
    }

    public void push( E element )
    { 
        list.addFirst(element);
    }

    public E pop( )
    {     
        if ( list.isEmpty() )
            return null;

        return list.removeFirst();
    }
}

/**
 * End of class StackInList
 */
