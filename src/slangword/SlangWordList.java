package slangword;

import java.util.HashMap;
import java.util.regex.Pattern;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author bin
 */
public class SlangWordList {
    HashMap<String, String> slangWords;
    
    public SlangWordList() {
        slangWords = new HashMap<String, String>();
    }
    
    public void add(String word, String definition) {
        if(!slangWords.containsKey(word)) {
            slangWords.put(word, definition);
        }
    }
    
    public int size() {
        return slangWords.size();
    }

    public String get(String word) {
        return slangWords.get(word);
    }

    public void remove(String word) {
        slangWords.remove(word);
    }

    public SlangWordList search(String definition) {
        SlangWordList result = new SlangWordList();
        for (String word : slangWords.keySet()) {
            if (Pattern.compile(Pattern.quote(definition),Pattern.CASE_INSENSITIVE).matcher(slangWords.get(word)).find()) {
                result.add(word, slangWords.get(word));
            }
        }
        return result;
    }
}
