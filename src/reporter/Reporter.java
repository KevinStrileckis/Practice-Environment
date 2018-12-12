/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author Kevin Strileckis
 */
public class Reporter {
    
    //TODO reverse unique files so to check for honesty
    
    private static int totalAnswers;
    private static int totalTotal;
    
    //fuck the rules
    public static int[] scores;
    
    public static void resetTotals()
    {
        totalAnswers = 0;
        totalTotal = 0;
    }
    
    public static void exportUniqueFile() throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter(UserInfo.getName()+UserInfo.getTimeStart()+".lsns", "UTF-8");
        int i = 0;
        //Make 12 garbage lines
        while(i < 12)
        {
            writer.println(makeTrash(12));//garbage line;
            i++;
        }
        //Make 1 legit line + some garbage
        writer.println((scramble(UserInfo.getName()) + makeTrash(3)));
        //Make 20 garbage lines
        i=0;
        while(i < 20)
        {
            writer.println(makeTrash(12));//garbage line;
            i++;
        }
        //ID section
        writer.println((UserInfo.getName() + makeTrash(1,5)));
        writer.println(makeTrash(4, 16));//garbage line;
        writer.println(scramble(UserInfo.getName()) + makeTrash(2));
        writer.println(scramble(UserInfo.getScore()));
        writer.println(makeTrash(3, 10));//garbage line;
        writer.println(totalTotal / 2);
        writer.println(totalAnswers);
        writer.println(UserInfo.getTimeStart());
        writer.println(UserInfo.getTimeEnd());
        //Make 25 garbage lines
        i=0;
        while(i < 25)
        {
            writer.println(makeTrash(12));//garbage line;
            i++;
        }
        //Output the score (encrypted)
        for(int s:scores)
            writer.println(scoreScramble(s));
        writer.println(scramble("whathaveIbecome"));
        //Make 30 garbage lines
        i=0;
        while(i < (29- scores.length * 2) % 20)
        {
            writer.println(makeTrash(4, 14));//garbage line
            i++;
        }
        //We have 99 lines not in the ID section already comepleted
        writer.close();
    }

    public static void saveProgress() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(UserInfo.getName()+UserInfo.getTimeStart()+".progress", "UTF-8");
        int i = 0;
        //Make 12 garbage lines
        while(i < 12)
        {
            writer.println(makeTrash(12));//garbage line;
            i++;
        }
        //Make 1 legit line + some garbage
        writer.println((scramble(UserInfo.getName()) + makeTrash(3)));
        //Make 20 garbage lines
        i=0;
        while(i < 20)
        {
            writer.println(makeTrash(12));//garbage line;
            i++;
        }
        //ID section
        writer.println((UserInfo.getName() + makeTrash(1,5)));
        writer.println(makeTrash(4, 16));//garbage line;
        writer.println(scramble(UserInfo.getName()) + makeTrash(2));
        writer.println(scramble(UserInfo.getScore()));
        writer.println(makeTrash(3, 10));//garbage line;
        writer.println(totalTotal / 2);
        writer.println(totalAnswers);
        writer.println(UserInfo.getTimeStart());
        writer.println(UserInfo.getTimeEnd());
        //Make 25 garbage lines
        i=0;
        while(i < 25)
        {
            writer.println(makeTrash(12));//garbage line;
            i++;
        }
        //Output the score (encrypted)
        for(int s:scores)
            writer.println(scoreScramble(s));
        writer.println(scramble("whathaveIbecome"));
        //Make 30 garbage lines
        i=0;
        while(i < (29- scores.length * 2) % 20)
        {
            writer.println(makeTrash(4, 14));//garbage line
            i++;
        }
        //We have 99 lines not in the ID section already comepleted
        writer.close();
    }
    public void pickUpProgress()
    {
    }
    
    
    private static String decryptLine1(String s){
        String s2 = "";
        for(int i=0, len =s.length(); i<len; i++){
            s2 += (char)(s.charAt(i) - 16);
        }
        return s2;
    }
    private static String decryptLine2(String s){
        String s2 = "";
        for(int i=s.length()-1; i>=0; i--){
            if(i == 7)
                s2 += (char)(s.charAt(i) - 5);
            else
                s2 += (char)(s.charAt(i) - 6);
        }
        return s2;
    }
    private static String decryptLine3(String s){
        String s2 = "";
        for(int i=s.length()-1; i>=0; i--){
            s2 += (char)(s.charAt(i) - 10);
        }
        return s2;
    }
    private static int on_line;
    private static String decryptString(String buffer) {
        if(buffer == null){
            return "  ";
        }
        if(buffer.length() == 0)
            return "  ";
        switch(on_line++ % 3){
            case 0:
                return decryptLine1(buffer);
            case 1:
                return decryptLine2(buffer);
            case 2:
                return decryptLine3(buffer);
        }
        return "";
    }
    
    public static int getSum(ArrayList<String> strs)
    {   
        on_line = 0;
        for(String s: strs)
        {
            String s2 = decryptString(s);
            for(int i=s2.length()-1; i>=0; --i)
            {
                totalAnswers += s2.charAt(i);
            }
        }
        
        return totalAnswers;
    }
    
    public static int getSumTotalTotal(ArrayList<String> strs)
    {   
        for(String s: strs)
        {
            String s2 = decryptString(s);
            for(int i=s2.length()-1; i>=0; --i)
            {
                totalTotal += s2.charAt(i);
            }
        }
        
        return totalTotal;
    }
    
    private static String makeTrash(int e){
        String s2 = "";
        for(int i=0; i<e; i++){
            s2 += (char)(Math.random()*26 + 'a');
        }
        return s2;
    }
    
    private static String makeTrash(int b, int e){
        String s2 = "";
        e = (int)(Math.random()*e);
        e = (int)(Math.random()*(e-b) + b);
        for(int i=e; i>=b; i--){
            s2 += (char)(Math.random()*26 + 'a');
        }
        return s2;
    }
    
    private static String scramble(String s){
        String s2 = "";
        for(int i=s.length()-1; i>=0; i--){
            s2 += (char)(s.charAt(i) + 3);
        }
        return s2;
    }
    
    private static String scramble(int s){
        String s2 = "";
        s2 += (char)(s+'a');
        return s2;
    }
    
    private static int scoreScramble(int s){
        s += 7 * 10 + 3;
        return s;
    }
}
