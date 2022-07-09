package slangword;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SlangWordList {
    HashMap<String, String> slangWords;
    
    public SlangWordList() {
        slangWords = new HashMap<String, String>();
    }
    
    public boolean add(String word, String definition) {
        if(!slangWords.containsKey(word)) {
            slangWords.put(word, definition);
            return true;
        }
        
        return false;
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
    
    public String randomWord() {
        int randomIndex = (int) (Math.random() * slangWords.size());
        int i = 0;
        for (String word : slangWords.keySet()) {
            if (i == randomIndex) {
                return word;
            }
            i++;
        }
        return slangWords.keySet().iterator().next();
    }
    
    public String randomDefinition() {
        int randomIndex = (int) (Math.random() * slangWords.size());
        int i = 0;
        for (String word : slangWords.keySet()) {
            if (i == randomIndex) {
                return slangWords.get(word);
            }
            i++;
        }
        return slangWords.values().iterator().next();
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
    
    public String getDefinition(String word) {
        return slangWords.get(word);
    }
}
