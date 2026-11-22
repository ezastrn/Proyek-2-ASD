/**
 * Singly linked list generik buatan sendiri (dengan pointer head dan tail).
 * Tidak menggunakan koleksi bawaan Java.
 */
public class LinkedList<T> {
    Node<T> head;
    Node<T> tail;
    int size;

    /** Menambahkan elemen di akhir list dalam waktu O(1). */
    public void add(T data) {
        Node<T> n = new Node<>(data);
        if (head == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    /** Memeriksa apakah elemen ada di dalam list dalam waktu O(n). */
    public boolean contains(T target) {
        for (Node<T> n = head; n != null; n = n.next) {
            if (target == null ? n.data == null : target.equals(n.data)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Merge sort pada linked list (stabil, O(n log n)). */
    public void sort(Cmp<T> cmp) {
        head = mergeSort(head, cmp);
        tail = head;
        while (tail != null && tail.next != null) {
            tail = tail.next;
        }
    }

    private Node<T> mergeSort(Node<T> h, Cmp<T> cmp) {
        if (h == null || h.next == null) return h;
        Node<T> slow = h, fast = h.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node<T> right = slow.next;
        slow.next = null;
        return merge(mergeSort(h, cmp), mergeSort(right, cmp), cmp);
    }

    private Node<T> merge(Node<T> a, Node<T> b, Cmp<T> cmp) {
        Node<T> dummy = new Node<>(null);
        Node<T> t = dummy;
        while (a != null && b != null) {
            if (cmp.compare(a.data, b.data) <= 0) {
                t.next = a;
                a = a.next;
            } else {
                t.next = b;
                b = b.next;
            }
            t = t.next;
        }
        t.next = (a != null) ? a : b;
        return dummy.next;
    }
}
