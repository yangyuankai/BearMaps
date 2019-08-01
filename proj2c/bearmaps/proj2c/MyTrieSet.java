package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.List;

public class MyTrieSet {
    private Node root;

    private static class Node {
        private boolean isKey;
        private Node[] arr;

        private Node(boolean b) {
            arr = new Node[256];
            isKey = b;
        }

        private boolean containsChar(char c) {
            int num = (int) c; // character to ascii
            return (arr[num] != null);
        }

        private int charIndex(char c) {
            int num = (int) c;
            return num;
        }

        private Node charChildNode(char c) {
            Node n = arr[charIndex(c)];
            return n;
        }

        private void addChar(char c, boolean isKey) {
            int pos = charIndex(c);
            arr[pos] = new Node(isKey);
        }

        private List<Node> allChildrenNode() {
            List<Node> l = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                if (arr[i] != null) {
                    l.add(arr[i]);
                }
            }
            return l;
        }

        private List<String> allChildrenChar(Node n) {
            List<String> l = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                if (n.arr[i] != null) {
                    l.add(Character.toString((char) i));
                }
            }
            return l;
        }

    }

    // CCCCCConstructor
    public MyTrieSet() {
        clear();

    }

    /**
     * Clears all items out of Trie
     */
    public void clear() {
        root = new Node(false);
    }

    /**
     * Returns true if the Trie contains KEY, false otherwise
     */
    public boolean contains(String key) {
        Node curr = root;
        for (char c : key.toCharArray()) {
            if (curr == null) {
                return false;
            }
            if (!curr.containsChar(c)) {
                return false;
            }
            curr = curr.charChildNode(c);
        }
        return (curr.isKey);
    }


    /**
     * Inserts string KEY into Trie >>>>>>>>>>>>>>>>>>> @source SPEC
     */
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }

        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.containsChar(c)) {
                curr.addChar(c, false);
            }
            curr = curr.charChildNode(c);
        }
        curr.isKey = true;

    }

    /**
     * Returns a list of all words that start with PREFIX
     */
    public List<String> keysWithPrefix(String prefix) {
        Node curr = root;
        for (char c : prefix.toCharArray()) {
            if (!curr.containsChar(c)) {
                return null;
            }
            curr = curr.charChildNode(c); // now at the end of the prefix
        }
        List<String> wordsAfterPrefixx = collectAtNode(curr);
        List<String> result = new ArrayList<>();
        for (String str : wordsAfterPrefixx) {
            result.add(prefix + str);
        }
        if (this.contains(prefix)) {
            result.add(prefix);
        }
        return result;
    }

    private List<String> collectAtNode(Node n) {
        List<String> x = new ArrayList<>();
        for (String c : n.allChildrenChar(n)) {
            collectHelp(c, x, n.charChildNode(c.charAt(0)));
        }
        return x;
    }

    private void collectHelp(String s, List<String> x, Node n) {
        if (n.isKey) {
            x.add(s);
        }
        for (String c : n.allChildrenChar(n)) {
            collectHelp(s + c, x, n.charChildNode(c.charAt(0)));
        }
    }

    public static void main(String[] args) {
        MyTrieSet my = new MyTrieSet();
        my.add("sp");
        my.add("spp");
        my.add("sppp");
        my.add("spppp");

        System.out.print(my.keysWithPrefix("spp"));
    }



}


