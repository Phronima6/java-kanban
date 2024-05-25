package managers.history;

class Node<E> { // Класс отвечающий за создание узла для хранения истории задач

    public Node<E> prev; // Предыдущий узел Node
    public E element; // Текущий узел Node
    public Node<E> next; // Следующий узел Node

    public Node(Node<E> prev, E element, Node<E> next) {
        this.prev = prev;
        this.element = element;
        this.next = next;
    }

}