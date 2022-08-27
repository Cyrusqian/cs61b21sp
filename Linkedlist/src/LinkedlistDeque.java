import java.util.Iterator;

public class LinkedlistDeque<T> implements Iterable<T> {
    private class Node {
        Node prev;
        Node next;
        T item;

        Node(Node pre, Node nex, T ite) {
            prev = pre;
            next = nex;
            item = ite;
        }

        Node(Node pre, Node nex) {
            prev = pre;
            next = nex;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedlistDeque() {
        sentinel = new Node(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node one = new Node(sentinel, sentinel.next, item);
        sentinel.next.prev = one;
        sentinel.next = one;
        size += 1;
    }


    public void addLast(T item) {
        Node lastone = new Node(sentinel.prev, sentinel, item);
        sentinel.prev.next = lastone;
        sentinel.prev = lastone;
        size += 1;

    }

    public int size() {
        return size;
    }


    public void printDeque() {
        Node abc = sentinel.next;
        while (abc != sentinel) {
            System.out.print(abc.item);
            System.out.print(" ");
            abc = abc.next;
        }
        System.out.println();

    }


    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T returnitem = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return returnitem;
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T returnvalue = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            T returnvalue1 = returnvalue;
            return returnvalue1;
        }
    }

    public T get(int index) {
        if (index > size) {
            return null;
        } else {
            Node nownode = sentinel.next;
            while (index > 0) {
                nownode = nownode.next;
                index--;
            }
            return nownode.item;
        }
    }


    private T getRecursiveHelp(Node start, int index) {
        if (index == 0) {
            return start.item;
        } else {
            return getRecursiveHelp(start.next, index - 1);
        }
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelp(sentinel.next, index);
    }

    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator<T> implements Iterator {
        Node index;

        ListIterator() {
            index = sentinel.next;
        }


        public boolean hasNext() {
            return index.next == sentinel;
        }


        public Object next() {
            Object nowvalue = index.item;
            index = index.next;
            return nowvalue;
        }
    }

    public static void main(String args[]) {
        LinkedlistDeque test = new LinkedlistDeque();
        for (int i = 1; i < 5; i++) {
            test.addFirst(i);
        }
        test.printDeque();
    }
}
