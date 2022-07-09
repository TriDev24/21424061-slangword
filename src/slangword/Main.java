package slangword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author bin
 */
public class Main {
   
    /**
     * @param args the command line arguments
     */
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
                        clearScreen();
                        searchSlangWord();
                        break;
                    case 2:
                        clearScreen();
                        searchDefinition();
                        break;
                    case 3:
                        clearScreen();
                        viewSearchHistory();
                        break;
                    case 4:
                        clearScreen();
                        addNewSlangWord();
                        break;
                    case 5:
                        clearScreen();
                        editWord();
                        break;
                    
                    
                    case 0:
                        clearScreen();                        
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
        clearScreen();
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
        clearScreen();
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
            clearScreen();
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
    
    private boolean isNumeric(String readLine) {
        return readLine.matches("[0-9]+");
    }
    
    public static void clearScreen() {  
       //Clears Screen in java
        try {
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            Runtime.getRuntime().exec("clear");
     } catch (IOException | InterruptedException ex) {}
    }
}
