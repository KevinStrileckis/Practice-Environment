/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lesson;

import java.util.ArrayList;

/**
 *
 * @author Kevin Strileckis
 */
public class Prompt {
    private ArrayList<String> prompt;
    private String answer;
    private String output;
    private String designation;
    private int commentLocation;
    private int score;
    
    
    public Prompt(){
        score = 0;
        prompt = new ArrayList<>();
    }
    public Prompt(String p){
        prompt = new ArrayList<>();
        prompt.add(p);
        score = 0;
    }
    public Prompt(String p, String a, String o, String d){
        prompt = new ArrayList<>();
        prompt.add(p);
        answer = a;
        output = o;
        designation = d;
        score = 0;
    }
    
    public void incScore(){setScore(getScore() + 1);}
    public void decScore(){setScore(getScore() - 1);}
    
    public boolean isJavaComment(int i){
        int j = 0;
        String s = prompt.get(i);
        if(s.length() < 1)
            return false;
        char c = s.charAt(j);
        for(int size = prompt.get(i).length(); j< size; ++j)
        {
            c = s.charAt(j);
            if(c == '/' && j < (size-1) && s.charAt(j+1) == '/'){
                commentLocation = j;
                return true;
            }
        }
        return false;
    }
    public boolean isPythonComment(int i){
        int j = 0;
        String s = prompt.get(i);
        if(s.length() < 1)
            return false;
        char c = s.charAt(j);
        for(int size = prompt.get(i).length(); j< size; ++j)
        {
            c = s.charAt(j);
            if(c == '#' && j < (size-1)){
                commentLocation = j;
                return true;
            }
        }
        return false;
    }
    //Requirement: isJavaComment(int) has already been called
    boolean isTODO(int i) {
        if(prompt.get(i).substring(commentLocation+2).contains("TODO"))
            return true;
        return false;
    }
    public void replaceChanges(String substring) {
        answer = answer.replace("_X_X_", substring);
        System.out.println(answer);
    }
    public void concealDontFeel(int index) {
        int tmp;
        if((tmp = prompt.get(index).indexOf("*X*X*")) > -1)
            prompt.set(index, prompt.get(index).substring(0, tmp) + "...");
    }
    
    
    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @param designation the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return the prompt
     */
    public ArrayList<String> getPrompt() {
        return prompt;
    }

    public void setAnswerToPrompt() {
        answer = "";
        for(String s: prompt){
            if(!(s.contains("//") || s.contains("/*") || s.contains("*/")))
                answer += s;
        }
        System.out.println("\n\nFSD"+answer);
    }

    /**
     * @param prompt the prompt to set
     */
    public void addToPrompt(String prompt) {
        this.prompt.add(prompt);
    }

    /**
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }




}
