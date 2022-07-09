package slangword;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SlangWordApplication app = new SlangWordApplication();
        
        boolean isLoaded = app.loadData("slang.txt");
        if(isLoaded) {
            app.run();
        }
    }
}

class SlangWordApplication{
    SlangWordList slangWordList = new SlangWordList();
    LinkedHashMap history = new LinkedHashMap<String, String>();
    private String fileName = "slang.txt";
    private String restoreFileName = "slang-restore.txt";

    
    public boolean loadData(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
    
    public void saveData(String fileName, HashMap<String, String> writeData) {
        try {
            File file = createFileIfNotExist(fileName);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            
            for(String word : writeData.keySet()) {
                var data =  word + "`" + writeData.get(word) + "\n";
                out.writeBytes(data);
            }
            out.close();
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
  
    public File createFileIfNotExist(String fileName) {
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
            System.out.println("\t---Slang Word---");
            System.out.println("1. Tìm kiếm Slang word\t\t\t6. Xoá slang word");
            System.out.println("2. Tìm kiếm theo định nghĩa\t\t\t7. Reset lại danh sách ban đầu");
            System.out.println("3. Xem lịch sử\t\t\t\t\t8. On this day slang word - Chọn slang word ngẫu nhiên");
            System.out.println("4. Thêm slang word\t\t\t9. Random game: Đoán định nghĩa dựa trên slang word");
            System.out.println("5. Chỉnh sửa slang word\t\t\t10. Random game: Đoán slang word dựa trên định nghĩa");
            System.out.println("0. Exit");
            System.out.println("\t---------------------------------------------");
            if(id < 0 || id > 10) {
                System.out.print("Lựa chọn không phù hợp! Xin hãy chọn lại: ");
            }
            else
            {
                System.out.print("Nhập lựa chọn của bạn: ");
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
                        restoreBackup();
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
                        System.out.println("Cảm ơn vì đã sử dụng ứng dụng Slang Word!");
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
        System.out.println("Tìm kiếm slang word: ");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String word = scanner.nextLine();
        
        if(slangWordList.slangWords.get(word) != null) {
            String value =  slangWordList.slangWords.get(word);
            System.out.println("Định nghĩa: " + value);
            history.put(word, value);
        }
        else {
            System.out.println("Không tìm thấy!");
        }
        System.out.println("Nhấn enter để tiếp tục...");
        scanner.nextLine();
        run();
    }

    private void searchDefinition() {
        System.out.print("Nhập định nghĩa bạn muốn tìm kiếm: ");
            Scanner scanner = new Scanner(System.in);
            String definition = scanner.nextLine();
            SlangWordList result = slangWordList.search(definition);
            if(result.size() == 0) {
                System.out.println("Không có kết quả!");
            }
            else {
                System.out.println("Tìm thấy " + result.size() + " kết :" + result.slangWords);
                for(String word : result.slangWords.keySet()) {
                   history.put(word , result.slangWords.get(word));
                }
            }
            System.out.println("Nhấn enter để tiếp tục...");
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
        System.out.println("Nhấn enter để tiếp tục...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        run();
    }
    
    private void addNewSlangWord() {
        System.out.print("Nhập slang word mới: ");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String slangWord = bReader.readLine();
            System.out.print("Nhập định nghĩa: ");
            String definition = bReader.readLine();
            String writeData = slangWord + "`" + definition + "\n";
            
            OutputStream os = new FileOutputStream(new File(fileName), true);
            os.write(writeData.getBytes(), 0, writeData.length());
            

            if(slangWordList.add(slangWord, definition)) {
                System.out.println("Thêm từ mới thành công!");
            }
            else {
                System.out.println("Thêm từ mới thất bại!");
            }
            
            System.out.println("Nhấn enter để tiếp tục...");
            bReader.readLine();
            os.close();
            
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void editWord() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Nhập từ bạn muốn chỉnh sửa: ");
            String word = sc.nextLine();
            if(slangWordList.slangWords.containsKey(word)){
                System.out.print("Định nghĩa cũ: " + slangWordList.slangWords.get(word) + "\n");
                System.out.print("Nhập định nghĩa mới: ");
                String definition = sc.nextLine();
                slangWordList.slangWords.put(word, definition);
                
                
                saveData(fileName, slangWordList.slangWords);
                System.out.println("Chỉnh sửa thành công!");
            }
            else{
                System.out.println("Từ không tìm !");
            }
            System.out.println("Nhấn enter để tiếp tục...");
            sc.nextLine();
            run();
        }
    }
    
    private void deleteWord() {
        System.out.print("Nhập từ bạn muốn xoá: ");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String word = bReader.readLine();
            if(slangWordList.slangWords.containsKey(word)){
                
                System.out.println("Slang word: "+ word + " , định nghĩa: " + slangWordList.slangWords.get(word) );
                System.out.println("Bạn có chắc muốn xóa từ này không? (Y/N)");
                String choice = bReader.readLine();
                if(choice.equalsIgnoreCase("Y")){
                    slangWordList.slangWords.remove(word);
                    saveData(fileName, slangWordList.slangWords);
                    System.out.println("Xoá thành công!");
                }
                else{
                    System.out.println("Xoá thất bại!");
                }
            }
            else{
                System.out.println("Không tìm thấy từ này!");
            }
            System.out.println("Nhấn enter để tiếp tục...");
            bReader.readLine();
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void restoreBackup() {
        System.out.println("Bạn có chắc muốn reset lại danh sách ban đầu không? (Y/N)");
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            System.out.print("Nhập lựa chọn của bạn: ");
            String choice = bReader.readLine();
            if(choice.equals("Y")||choice.equals("y")) {
                slangWordList.slangWords.clear();
                loadData(restoreFileName);
                saveData(fileName, slangWordList.slangWords);
                System.out.println("Đã reset lại danh sách!");

            }
            else {
                System.out.println("Reset không thành công!");
            }
            System.out.println("Nhấn enter để tiếp tục...");
            bReader.readLine();
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void pickRandomWord() {
        String word = slangWordList.randomWord();
        System.out.println("Slang word: " + word +", định nghĩa: " + slangWordList.getDefinition(word));
        
        System.out.println("Nhấn enter để tiếp tục...");
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
            System.out.println("Đoán định nghĩa của từ: " + word);
            game.run();
            System.out.println("Nhấn enter để tiếp tục...");
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

            System.out.println("Đoán slang word của: " + definition);
            RandomGame game = new RandomGame (definition, words,random);
            game.run();
            System.out.println("Nhấn enter để tiếp tục...");
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
