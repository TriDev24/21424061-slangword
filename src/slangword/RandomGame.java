
package slangword;

import java.io.BufferedReader;

public class RandomGame {
    public String question;
    public String[] answers;
    public int userAnswer;
    public int correctAnswer;

    public RandomGame(String question, String[] answers, int correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.userAnswer = 0;
        this.correctAnswer = correctAnswer;
    }


    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return this.answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getCorrectAnswer() {
        return this.correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }
    public int getUserAnswer() {
        return this.userAnswer;
    }

    public boolean printResult() {
        if (this.userAnswer == -1) {
            System.out.println("Bạn chưa trả lời!");
            return false;
        } else if (this.userAnswer == this.correctAnswer) {
            System.out.println("Đúng rồi!!!");
            return true;
        } else {
            System.out.println("Sai rồi!!!");
            return false;
        }
    }
    public void display() {
        for (int i = 1; i <= this.answers.length; i++) {
            System.out.println(i + ": " + this.answers[i-1]);
        }
    }

    public void run() {
        display();
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
        try {
            do {
                if(this.userAnswer < 0 || this.userAnswer >= this.answers.length) {
                    
                    System.out.println("Hãy nhập số thứ tự câu trả lời đúng!");
                }
                System.out.print("Nhập câu trả lời của bạn (1 đến " + this.answers.length + "): ");
                String line = reader.readLine();
                if(isNumeric(line)) {
                    this.userAnswer = Integer.parseInt(line)-1 ;
                }
                else {
                   this.userAnswer=-1;
                }

            } while (this.userAnswer < 0 || this.userAnswer >= this.answers.length);
            
        } catch (Exception e) {
           
        }
       
        if(! printResult()){System.out.println("*Đáp án đúng là: " + this.answers[this.correctAnswer]);
    }
}


    private boolean isNumeric(String readLine) {
        try {
            Integer.parseInt(readLine);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}