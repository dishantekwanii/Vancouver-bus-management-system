package busoute;

import java.util.ArrayList;

public class TST {

    private class Node {
        public char data; // character stored in the node
        public Node left;
        public Node middle;
        public Node right;
        boolean isLast = false;

        public Node(char data) {
            this.data = data;
        }
    }

    Node head;

    public TST() {
        head = null;
    }

    // add a node to the end of the trie

    public void put(String word) {
        head = put(head, word, 0);
    }

    private Node put(Node node, String word, int index) {
        char letter = word.charAt(index);
        if (node == null)
            node = new Node(letter);
        if (letter < node.data)
            node.left = put(node.left, word, index);
        else if (letter > node.data)
            node.right = put(node.right, word, index);
        else if (index < word.length() - 1)
            node.middle = put(node.middle, word, index + 1);
        else
            node.isLast = true;
        return node;
    }

    public String get(String word) {
        return get(head, word, 0, "");
    }

    private String get(Node node, String word, int index, String result) {
        if (node == null)
            return null;
        char letter = word.charAt(index);
        if (letter < node.data) {
            return get(node.left, word, index, result);
        } else if (letter > node.data) {
            return get(node.right, word, index, result);
        } else if (index < word.length() - 1) {
            return get(node.middle, word, index + 1, result + letter);
        } else
            return result + letter;
    }

    public String[] getMultiple(String word) {
        return getMultiple(head, word, 0, "");
    }

    private String[] getMultiple(Node node, String word, int index, String substring) {
        if (node == null)
            return null;
        char letter = word.charAt(index);
        if (letter < node.data) {
            return getMultiple(node.left, word, index, substring);
        } else if (letter > node.data) {
            return getMultiple(node.right, word, index, substring);
        } else if (index < word.length() - 1) {
            return getMultiple(node.middle, word, index + 1, substring + letter);
        } else {
            ArrayList<String> res = new ArrayList<>();
            collect(node, substring, res);
            return res.toArray(new String[res.size()]);
        }
    }

    private void collect(Node node, String substring, ArrayList<String> soFar) {
        if (node != null) {
            collect(node.left, substring, soFar);
            if(node.isLast)
                soFar.add(substring + node.data);
            collect(node.middle, substring + node.data, soFar);
            collect(node.right, substring, soFar);
        }
    }
}