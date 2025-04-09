package autoComplete;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree(){
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        TreeNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            
            if (!current.children.containsKey(character)) {
                TreeNode newNode = new TreeNode();
                newNode.letter = character;
                current.children.put(character, newNode);
            }
            current = current.children.get(character);
        }
        if (!current.isWord) {
            current.isWord = true;
            size++;
        }
    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        TreeNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);

            if (!current.children.containsKey(character)) {
                return false;
            }
            current = current.children.get(character);
        }
        return current.isWord;
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix){
        ArrayList<String> result = new ArrayList<>();
        TreeNode current = root;

        for (int i = 0; i < prefix.length(); i++) {
            char character = prefix.charAt(i);

            if (!current.children.containsKey(character)) {
                return result;
            }
            current = current.children.get(character);
        }
        
        Stack<Map.Entry<TreeNode, String>> stack = new Stack<>();
        stack.push(Map.entry(current, prefix));
        while (!stack.isEmpty()) {
            Map.Entry<TreeNode, String> entry = stack.pop();
            TreeNode node = entry.getKey();
            String word = entry.getValue();
            if (node.isWord) {
                result.add(word);
            }
            for (Map.Entry<Character, TreeNode> child : node.children.entrySet()) {
                stack.push(Map.entry(child.getValue(), word + child.getKey()));
            }
        }
        return result;
    }

    /**
     * @return the number of words in the tree
     */
    public int size(){
        return size;
    }
    
}
