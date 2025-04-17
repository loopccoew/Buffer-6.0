package dsminiproject;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;

    private static class Node<T> {
        T data;
        Node<T> left, right;

        public Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node<T> insertRec(Node<T> root, T data) {
        if (root == null) {
            root = new Node<>(data);
            return root;
        }

        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        }

        return root;
    }

    public void inOrderTraversal(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add(node.data);
            inOrderTraversal(node.right, result);
        }
    }

    public List<T> getAllNodes() {
        List<T> nodes = new ArrayList<>();
        inOrderTraversal(root, nodes);
        return nodes;
    }
}
