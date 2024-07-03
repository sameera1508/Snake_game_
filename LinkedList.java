class LinkedList {
    Node head, tail;

    public LinkedList(int startX, int startY) {
        head = new Node(startX, startY);
        tail = head;
    }

    public void addNode(int x, int y) {
        Node newNode = new Node(x, y);
        tail.next = newNode;
        tail = newNode;
    }

    public void removeTail() {
        Node current = head;
        while (current.next != null && current.next != tail) {
            current = current.next;
        }
        tail = current;
        tail.next = null;
    }
}

