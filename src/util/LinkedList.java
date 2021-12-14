package util;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * Custom list that extneds AbstractSequentialList and has a ListIterator
 * to use for iterating though the list.
 * @author Matthew Welker
 * @param <E> Generic type represnting the object in the list
 */
public class LinkedList<E> extends AbstractSequentialList<E>
{
	private int size;
	private Node front;
	private Node last;
	public LinkedList() {
		size = 0;
		front = new Node(null, null, null); 
		last = new Node(null, null, front); 
		front.next = last; 
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return new MyListIterator(index);
	}
	/**{@inheritDoc}*/
	@Override
	public int size()
	{
		return size;
	}
	
	private class Node
	{
		public E data;
		public Node next;
		//Doubly linked
		public Node prev;
		public Node(E data, Node next, Node last)
		{
			this.data = data;
			this.next = next;
			this.prev = last;
		}
	}
	private class MyListIterator implements java.util.ListIterator<E> {
		private Node prev = front;
		private Node next = prev.next;
		private int nextIdx = 0;
		private int prevIdx = -1;
		private Node lastRet;
		
		public MyListIterator(int index) {
			if (index < 0 || index > size) {
				throw new IndexOutOfBoundsException();
			}
			while (index != nextIdx) {
				next();
			}
		}
		@Override
		public boolean hasNext()
		{
			return nextIdx != size;
		}

		@Override
		public E next()
		{
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			nextIdx++;
			prevIdx++;
			next = next.next;
			prev = prev.next;
			lastRet = prev;
			return lastRet.data;
		}

		@Override
		public boolean hasPrevious()
		{
			return prevIdx != -1;
		}

		@Override
		public E previous()
		{
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			nextIdx--;
			prevIdx--;
			prev = prev.prev;
			next = next.prev;
			lastRet = next;
			return lastRet.data;
		}

		@Override
		public int nextIndex()
		{
			return nextIdx;
		}

		@Override
		public int previousIndex()
		{
			return prevIdx;
		}

		@Override
		public void remove()
		{
			if (lastRet == null) {
				throw new IllegalStateException();
			}
			if (lastRet.equals(next)) {
				next.next.prev = prev;
				prev.next = next.next;
				next = next.next;
			}
			else {
				next.prev = prev.prev;
				prev.prev.next = next;
				prev = prev.prev;
				prevIdx--;
				nextIdx--;
			}
			lastRet = null;
			size--;
		}

		@Override
		public void set(E e)
		{
			if(e == null) {
				throw new NullPointerException("Added data cannot be null");
			}
			if (lastRet == null) {
				throw new IllegalStateException();
			}
			lastRet.data = e;
		}

		@Override
		public void add(E e)
		{
			if (e == null) {
				throw new NullPointerException("Data to add cannot be null");
			}
			Node n = new Node(e, next, prev);
			next.prev = n;
			prev.next = n;
			prev = prev.next;
			nextIdx++;
			prevIdx++;
			size++;
			lastRet = null;
		}
		
	}
}
