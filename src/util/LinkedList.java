package util;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * Custom list that extneds AbstractSequentialList and has a ListIterator to use for iterating though the list. 
 * Chose to use this list over the LinkedList in the java.util to practice data structures.
 * @author Matthew Welker
 * @param <E> Generic type represnting the object in the list
 */
public class LinkedList<E> extends AbstractSequentialList<E>
{
	/**Size of the list*/
	private int size;
	/**Front Node of the list*/
	private Node front;
	/**Last Node in the list*/
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
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		return size;
	}
	
	private class Node
	{
		/**The element that the Node holds, what data is actually added to the list*/
		public E data;
		/**Reference to the next Node in the list, or null if this Node is the last Node*/
		public Node next;
		/**Reference to the previous Node in the list, or null if this Node is the first Node*/
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
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext()
		{
			return nextIdx != size;
		}
		/**
		 * {@inheritDoc}
		 */
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
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPrevious()
		{
			return prevIdx != -1;
		}
		/**
		 * {@inheritDoc}
		 */
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
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int nextIndex()
		{
			return nextIdx;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int previousIndex()
		{
			return prevIdx;
		}
		/**
		 * {@inheritDoc}
		 */
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
		/**
		 * {@inheritDoc}
		 */
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
		/**
		 * {@inheritDoc}
		 */
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
