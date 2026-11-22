/**
 * Node untuk struktur data singly linked list generik.
 */
public class Node<T> {
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
    }
}
