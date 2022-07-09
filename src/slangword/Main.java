package slangword;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SlangWordApplication app = new SlangWordApplication();
        
        boolean isLoaded = app.loadData();
        if(isLoaded) {
            app.run();
        }
    }
}

class SlangWordApplication{
    SlangWordList slangWordList = new SlangWordList();
    LinkedHashMap history = new LinkedHashMap<String, String>();
    private String fileName = "slang.txt"; 
    
    public boolean loadData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            System.out.println("reader: " + reader);
            String line = reader.readLine();
            
            while(line != null) {
                String[] tokens = line.split("`");
                slangWordList.add(tokens[0], tokens[1]);
                line = reader.readLine();
            }
            
            reader.close();
            
            return true;
        } catch(IOException e) {
        }
        
        return false;
    }
    
    public void saveData() {
        try {
            File file = createFileIfNotExist();
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            
            for(String word : slangWordList.slangWords.keySet()) {
                var writeData = word + "`" + slangWordList.slangWords.get(word) + "\n";
                out.writeBytes(writeData);
            }
            out.close();
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
  
    public File createFileIfNotExist() {
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    
    public void run() {
        int id = 0;
        do {
            clearScreen();
            System.out.println("\t---Welcome to Slang Word List Application---");
            System.out.println("1. Search by slang word\t\t\t6. Delete slang word");
            System.out.println("2. Search by definition\t\t\t7. Restore backup");
            System.out.println("3. View search history\t\t\t8. On this day slang word");
            System.out.println("4. Add new slang word\t\t\t9. Random game: guess the definition");
            System.out.println("5. Edit slang word\t\t\t10. Random game: guess the slang word");
            System.out.println("0. Exit");
            System.out.println("\t---------------------------------------------");
            if(id<0||id>10) {
                System.out.print("Invalid choice! Please enter your choice again: ");
            }
            else
            {
                System.out.print("Enter your choice: ");
            }

            BufferedReader bReader;
            try {
                bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
                String choice = bReader.readLine();
                if(isNumeric(choice)) {
                    id = Integer.parseInt(choice);
                }
                else{
                    id= - 1;
                }
                switch (id) {
                    case 1:
                        searchSlangWord();
                        break;
                    case 2:
                        searchDefinition();
                        break;
                    case 3:
                        viewSearchHistory();
                        break;
                    case 4:
                        addNewSlangWord();
                        break;
                    case 5:
                        editWord();
                        break;
                    case 6:
                        deleteWord();
                        break;
                    case 7:
                        // restoreBackup();
                        break;
                    case 8:
                        pickRandomWord();
                        break;
                    case 9:
                        guessDefinition();
                        break;    
                    case 10:
                        guessSlangWord();
                        break;
                     
                    
                    
                    case 0:                      
                        System.out.println("Thank you for using Slang Word List Application!");
                        // saveData("slang.txt");
                        System.exit(0);
                    default:
                        break;
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        } while (id < 0||id > 5);
    } 
     
    private void searchSlangWord() {
        System.out.println("Search a slang word");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String word = scanner.nextLine();
        
        if(slangWordList.slangWords.get(word) != null) {
            String value =  slangWordList.slangWords.get(word);
            System.out.println("Definition: " + value);
            history.put(word, value);
        }
        else {
            System.out.println("Not found!");
        }
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        run();
    }

    private void searchDefinition() {
        System.out.print("Enter the definition you want to search: ");
            Scanner scanner = new Scanner(System.in);
            String definition = scanner.nextLine();
            SlangWordList result = slangWordList.search(definition);
            if(result.size() == 0) {
                System.out.println("No result found!");
            }
            else {
                System.out.println("Found " + result.size() + " results:" + result.slangWords);
                for(String word : result.slangWords.keySet()) {
                   history.put(word , result.slangWords.get(word));
                }
            }
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            run();

    }
    
    private void viewSearchHistory() {
        if(history.isEmpty()){
            System.out.println("No search history!");
        }
        else{
            System.out.println("Search history:");
            int i =1;
            for(Object word : history.keySet()){
                System.out.println(i +". "+ word + ": " + history.get(word));
                i++;
            }
        }
        System.out.println("Press enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        run();
    }
    
    private void addNewSlangWord() {
        System.out.print("Enter the slang word: ");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String slangWord = bReader.readLine();
            System.out.print("Enter the definition: ");
            String definition = bReader.readLine();
            String writeData = slangWord + "`" + definition + "\n";
            
            OutputStream os = new FileOutputStream(new File(fileName), true);
            os.write(writeData.getBytes(), 0, writeData.length());
            

            if(slangWordList.add(slangWord, definition)) {
                System.out.println("Add new word successfully!");
            }
            else {
                System.out.println("Add new word failed! The word is already existed!");
            }
            
            System.out.println("Press enter to continue...");
            bReader.readLine();
            os.close();
            
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void editWord() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the word you want to edit: ");
            String word = sc.nextLine();
            if(slangWordList.slangWords.containsKey(word)){
                System.out.print("Old definition: " + slangWordList.slangWords.get(word) + "\n");
                System.out.print("Enter the new definition: ");
                String definition = sc.nextLine();
                slangWordList.slangWords.put(word, definition);
                saveData();
                System.out.println("Edit successfully!");
            }
            else{
                System.out.println("Word not found!");
            }
            System.out.println("Press enter to continue...");
            sc.nextLine();
            run();
        }
    }
    
    private void deleteWord() {
        System.out.print("Enter the word you want to delete: ");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String word = bReader.readLine();
            if(slangWordList.slangWords.containsKey(word)){
                
                System.out.println("Slang word: "+ word + " , definition: " + slangWordList.slangWords.get(word) );
                System.out.println("Are you sure you want to delete this word? (Y/N)");
                String choice = bReader.readLine();
                if(choice.equalsIgnoreCase("Y")){
                    slangWordList.slangWords.remove(word);
                    saveData();
                    System.out.println("Delete successfully!");
                }
                else{
                    System.out.println("Delete unsuccessfully!");
                }
            }
            else{
                System.out.println("Word not found!");
            }
            System.out.println("Press Enter to continue...");
            bReader.readLine();
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*private void restoreBackup() {
        System.out.println("Are you sure you want to restore backup? (Y/N)");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            System.out.print("Enter your choice: ");
            String choice = bReader.readLine();
            if(choice.equals("Y")||choice.equals("y")) {
                slangWordList.slangWords.clear();
                loadData("slang.txt.bak");
                slangWordList.size = slangWordList.slangWords.size();
                System.out.println("Backup restored!");

            }
            else {
                System.out.println("Backup not restored!");
            }
            System.out.println("Press any key to continue...");
            bReader.readLine();
            run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
    
    private void pickRandomWord() {
        String word = slangWordList.randomWord();
        System.out.println("Slang word: " + word +", definition: " + slangWordList.getDefinition(word));
        
        System.out.println("Press enter to continue...");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            bReader.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        run();
    }
    
    private void guessDefinition() {
        try {
            String word = slangWordList.randomWord();
            String[] difinition= {"","","",""};
            int randomIndex = (int) (Math.random() * 4);
            for(int i = 0; i < 4; i++){
                if(i == randomIndex) {
                    difinition[i] = slangWordList.getDefinition(word);
                }
                else {
                    difinition[i] = slangWordList.randomDefinition();
                    for(int j = 0; j < i; j++){
                        if(difinition[i].equals(difinition[j])|| 
                        difinition[i].equals(slangWordList.getDefinition(word))){
                            i--;
                            break;
                        }
                    }
                }

            }
            RandomGame game = new RandomGame(word, difinition, randomIndex);
            System.out.println("Guess the definition of: " + word);
            game.run();
            System.out.println("Press enter to continue...");
            BufferedReader bReader;

            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            bReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        run();
    }
    
    private void guessSlangWord() {
        try {
            String word = slangWordList.randomWord();
            String definition = slangWordList.getDefinition(word);
            String[] words = new String[4];
            int random = (int)(Math.random()*4);
            for(int i = 0; i < 4; i++){
                if(i==random){
                    words[i] = word;
                }
                else{
                    words[i] = slangWordList.randomWord();
                    for(int j = 0; j < i; j++){
                        if(words[i].equals(words[j])||words[i].equals(word)){
                            i--;
                            break;
                        }
                    }
                }
            }

            System.out.println("Guess the slang word of: " + definition);
            RandomGame game = new RandomGame (definition, words,random);
            game.run();
            System.out.println("Press enter to continue...");
            BufferedReader bReader;
        
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            bReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        run();

    }
    
    private boolean isNumeric(String readLine) {
        return readLine.matches("[0-9]+");
    }
    
    public void clearScreen() {  
       System.out.flush(); 
    }
}
