package lesson;
import java.awt.Color;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import ui.UIMain;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import reporter.Reporter;
import reporter.UserInfo;

/**
 *
 * @author Kevin Strileckis
 */
public class Projector {
    private UIMain ui;
    
    private String lessonTitle, lessonVersion;
    private String specialCommand;
    private ArrayList<Integer> starts;
    private ArrayList<Integer> stops;
    private ArrayList<Prompt> fullLesson;
    
    private int numOfSections;
    
    public Projector(UIMain ui){
        this.ui = ui;
    }
    
    public void setUpLesson(File lesson){
        readLessonScript(lesson);
    }
    
    //Returns false if there is not another lesson in file
        // Will also
        // save end time
        // write session information to file
    public boolean doNextLesson(JTextPane lessonArea, JTextArea outputArea, int segment, JTextPane copyOn, JTextArea workArea){
        if(segment >= numOfSections){
            try{
                UserInfo.setTimeEnd(System.currentTimeMillis());
                //Give the reporter the scores
                int[] scores = new int[fullLesson.size()];
                for(int i=0; i<scores.length; ++i){
                    scores[i] = fullLesson.get(i).getScore();
                }
                Reporter.scores = scores;
                //Create file
                Reporter.exportUniqueFile();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Trouble writing to file! Show this to your teacher or risk not having any credit on this assignment!", "Oh no!", JOptionPane.INFORMATION_MESSAGE);
            }
            return false;
        }
        if(segment == 0){
            UserInfo.setTimeStart(System.currentTimeMillis());
        }
        
        //Check for special code
        if(fullLesson.get(segment).getDesignation() == null){
            project(segment, lessonArea, outputArea, workArea);
        }
        else if(fullLesson.get(segment).getDesignation().equals("CopyOn")){
            project(segment, copyOn, outputArea, workArea);
            System.out.print(copyOn.getText());
        }
        else if(fullLesson.get(segment).getDesignation().equals("Review")){
            project(segment, lessonArea, outputArea, workArea);
        }
        else
            project(segment, lessonArea, outputArea, workArea);
        return true;
    }
    
    private void project(int segment, JTextPane lessonArea, JTextArea outputArea, JTextArea workArea){
        
        lessonArea.setMargin(new Insets(5, 5, 5, 5));
        
        for(int i=0, size = fullLesson.get(segment).getPrompt().size();
                i<size; ++ i)   
        {
            //Check if it is a Java comment
            if(getFullLesson().get(segment).isJavaComment(i))
            {
                //Check if it is a TODO statement
                if(getFullLesson().get(segment).isTODO(i)){
                    appendToPane(lessonArea, getFullLesson().get(segment).getPrompt().get(i) + "\n", Color.darkGray);
                }
                else {//Comment
                    appendToPane(lessonArea, getFullLesson().get(segment).getPrompt().get(i) + "\n", new Color(0, 120, 0));
                }
            }
            //Check if it is a Python comment
            else if(getFullLesson().get(segment).isPythonComment(i))
            {
                //Check if it is a TODO statement
                if(getFullLesson().get(segment).isTODO(i)){
                    appendToPane(lessonArea, getFullLesson().get(segment).getPrompt().get(i) + "\n", new Color(120, 110, 110));
                }
                else{
                    //Add red Python comment
                    appendToPane(lessonArea, getFullLesson().get(segment).getPrompt().get(i) + "\n", new Color(120, 0, 0));
                }
            }
            else
                appendToPane(lessonArea, getFullLesson().get(segment).getPrompt().get(i) + "\n", Color.BLUE);
        }
        //Fill the output pane. Some problems rely on showing the previous problem
        if(segment > 0 && getFullLesson().get(segment).getOutput().equals("**showprev**"))
        {
            outputArea.setText("//Previously on SPE\n");
            outputArea.append(getFullLesson().get(segment-1).getOutput() + "\n");
        }
        else if(segment > 0 && getFullLesson().get(segment).getOutput().equals("**saveprev**"))
        {
            workArea.setText(toStringWithNewLines(getFullLesson().get(segment-1).getAnswer()));
        }
        else
        {
            outputArea.setText(getFullLesson().get(segment).getOutput());
        }
    }
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        System.out.print(msg);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        try{
            tp.getStyledDocument().insertString(len, msg, aset);
        }
        catch(BadLocationException  e)
        {
            System.out.println("Broke at appendToPane(,,) in Projector class. Show to your teacher");
        }
    }
    
        private String specialCode = "";
    private void readLessonScript(File lesson) {
        boolean makeNew = true;
        on_line = 0;
        fullLesson = new ArrayList<>();
        starts = new ArrayList<>();
        stops = new ArrayList<>();
        
        //Reset lesson codes
        Reporter.resetTotals();
        
        try{
        boolean readPromptFlag = false, upToDate = false;
        boolean threepointO = false;
        FileReader fr = new FileReader(lesson);
        BufferedReader br = new BufferedReader(fr);
        String buffer;
        
        //First, read version number
        lessonVersion = br.readLine();
        
        if(lessonVersion.contains("100.000")){//New version started on 11-17-2018 for encrypted lesson files
            lessonTitle = decryptString(br.readLine());
            upToDate = true;
        }
        else if(lessonVersion.contains("**110100.110001**")){
            lessonTitle = br.readLine();
            upToDate = true; //Compatibility for previous lesson
        }
        else if(lessonVersion.contains("**3.0**"))
        {
            //Then, read title
            lessonTitle = br.readLine();
            upToDate = false; // New version started on 9-30 for better encryption
            threepointO = true;
        }
        else if(!lessonVersion.contains("**\"\"**"))
        {
            lessonTitle = lessonVersion;
            lessonVersion = "Old (the classics!)";
        }
        else
        {
            lessonTitle = br.readLine();
            lessonVersion = "One of the 2s";
        }
        //Then check for special commands. If none, then read rest of lesson
        buffer = br.readLine();
        
        
        for(int i=0, tmp, j=-1; buffer != null;)
        {
            if(upToDate){
                buffer = decryptString(buffer);
                System.out.println(buffer);
            }
            //Check if this line is a special command
            if(buffer.indexOf("$PCC0M") == 0)
            {
                if(buffer.indexOf("(R)") > 0)
                {
                    setSpecialCode("Review");
                getFullLesson().get( getFullLesson().size()-1 ).setDesignation(specialCode);
                }
                else if(buffer.indexOf("(CO)") > 0)
                {
                    setSpecialCode("CopyOn");
                getFullLesson().get( getFullLesson().size()-1 ).setDesignation(specialCode);
                }
                else if(buffer.indexOf("(CF)") > 0)
                {
                    setSpecialCode("CopyFrom");
                getFullLesson().get( getFullLesson().size()-1 ).setDesignation(specialCode);
                }
                else if(buffer.indexOf("(Chance)") > 0)
                {
                    setSpecialCode("Change");
                getFullLesson().get( getFullLesson().size()-1 ).setDesignation(specialCode);
                }
                else if(buffer.indexOf("(CFM)") > 0)
                {
                    setSpecialCode("ChangeFromMemory");
                getFullLesson().get( getFullLesson().size()-1 ).setDesignation(specialCode);
                }
                else if(buffer.indexOf("Solve") > 0)
                {
                    setSpecialCode("Solve");
                getFullLesson().get( getFullLesson().size()-1 ).setDesignation(specialCode);
                }
            buffer = br.readLine();
                continue;
            }
            System.out.print(specialCode);
            
            //check if we've started a new problem
            if(makeNew){
                getFullLesson().add(new Prompt());
                makeNew = false;
                ++j;
            }
            
            tmp = buffer.indexOf("%&");
            
            if(tmp > -1)
            {
                readPromptFlag = !readPromptFlag;
                getFullLesson().get(j).addToPrompt(buffer.substring(tmp+2));
                if(specialCode != null && specialCode.equals("ChangeFromMemory"))
                    getFullLesson().get(j).concealDontFeel(getFullLesson().get(j).getPrompt().size()-1);
                i++;
            }
            else if(readPromptFlag && upToDate){
                getFullLesson().get(j).addToPrompt(buffer);
                if(specialCode != null && specialCode.equals("ChangeFromMemory"))
                    getFullLesson().get(j).concealDontFeel(getFullLesson().get(j).getPrompt().size()-1);
                i++;
            }
            else if(readPromptFlag && threepointO)
            {
                getFullLesson().get(j).addToPrompt(buffer);
                if(specialCode != null && specialCode.equals("ChangeFromMemory"))
                    getFullLesson().get(j).concealDontFeel(getFullLesson().get(j).getPrompt().size()-1);
                i++;
            }
            else if ((tmp = buffer.indexOf("$TOP")) > -1)
            {
                //System.out.println(buffer);
                if(tmp != -1)
                {
                    //In either case, read in the answer
                    if((tmp = buffer.indexOf("$#")) > -1){
                        getFullLesson().get(j).setAnswer(buffer.substring(buffer.indexOf("$TOP") + 4, buffer.indexOf("$#")));
                        getFullLesson().get(j).setOutput(buffer.substring(tmp + 2));
                        if((tmp = buffer.indexOf("_X__X_")) > -1){
                            getFullLesson().get(j).replaceChanges(buffer.substring(tmp + 6));
                        }
                    }
                    else if((tmp = buffer.indexOf("_X__X_")) > -1){
                        getFullLesson().get(j).setAnswer(buffer.substring(buffer.indexOf("$TOP") + 4, tmp));
                        getFullLesson().get(j).replaceChanges(buffer.substring(tmp + 6));
                        getFullLesson().get(j).setOutput("No output");
                    }
                    else{
                        getFullLesson().get(j).setAnswer(buffer.substring(buffer.indexOf("$TOP") + 4));
                        getFullLesson().get(j).setOutput("No output");
                    }
                    if((buffer.contains("[["))){
                        if((buffer.contains("SIC"))){
                            getFullLesson().get(j).setAnswerToPrompt();
                        }
                        else if((buffer.contains("SAP"))){
                            getFullLesson().get(j).setAnswer(getFullLesson().get(j-1).getAnswer());
                        }
                    }
                    
                    //Then reset make new
                    makeNew = true;
                }
            }
            
            buffer = br.readLine();
        }
        
        
        //Close readers
        br.close();
        fr.close();
        //Update number of sections
            setNumOfSections(getFullLesson().size());
        System.out.println(numOfSections);
        //Give the Reporter the proper code
        ArrayList<String> outputvalue = new ArrayList<>();
        for(int i=0; i<getNumOfSections();++i)
            outputvalue.add(getFullLesson().get(0).getAnswer());
        Reporter.getSum(outputvalue);
        for(int i=0, size = getFullLesson().get(0).getPrompt().size(); i<size;++i)
            outputvalue.add(getFullLesson().get(0).getPrompt().get(i));
        Reporter.getSumTotalTotal(outputvalue);
                
        }
        
        catch (FileNotFoundException ex){
            System.out.println("Error in reading lesson file.");
        }
        catch (IOException ex){
            System.out.println("Error in reading lesson file.");
        }
    }
    
    /*private void runLoadedLessonScript(){
        /*  Code format
            duration
            text
            
            if there are any non-constants to print, they will be preceded by %&
            "name"
            "scor"
            Currently, only these two are used. So the ternary operator on line 85 works fine
        */
        
      /*  Timer t = new Timer(0);
        
        //Show lesson
        for(int i=0, tmp; i<durations.size();)
        {
            if(t.isExpired())
            {
                ui.addLessonText(texts.get(i) + "\n");
                //ui.getLessonDisplay().append(texts.get(i) + "\n");
                System.out.println("tick");
                t.reset(durations.get(i++));
            }
        }
    }
    
    public void showVideo(String file)
    {
       /* new Thread(new Runnable() {
        @Override
        public void run() {
            VideoCodec videoCodec =
                    new VideoCodec(videoPanel, videoName + TextsDao.getText("videoFilesExtension"));
        }
    }).start();*/
    //}


    /**
     * @return the lessonTitle
     */
    public String getLessonTitle() {
        return lessonTitle;
    }

    /**
     * @return the numOfSections
     */
    public int getNumOfSections() {
        return numOfSections;
    }

    /**
     * @param numOfSections the numOfSections to set
     */
    public void setNumOfSections(int numOfSections) {
        this.numOfSections = numOfSections;
    }

    /**
     * @return the fullLesson
     */
    public ArrayList<Prompt> getFullLesson() {
        return fullLesson;
    }

    /**
     * @param fullLesson the fullLesson to set
     */
    public void setFullLesson(ArrayList<Prompt> fullLesson) {
        this.fullLesson = fullLesson;
    }

    /**
     * @return the specialCode
     */
    public String getSpecialCode() {
        return specialCode;
    }

    /**
     * @param specialCode the specialCode to set
     */
    public void setSpecialCode(String specialCode) {
        this.specialCode = specialCode;
    }

    private String toStringWithNewLines(String answer) {
        String ret = "";
        for(int i=0, length = answer.length(); i<length; ++i)
        {
            ret += answer.charAt(i);
            if(answer.charAt(i) == ';')
                ret+='\n';
        }
        return ret;
    }
    
    private String decryptLine1(String s){
        String s2 = "";
        for(int i=0, len =s.length(); i<len; i++){
            s2 += (char)(s.charAt(i) - 16);
        }
        return s2;
    }
    private String decryptLine2(String s){
        String s2 = "";
        for(int i=s.length()-1; i>=0; i--){
            if(i == 7)
                s2 += (char)(s.charAt(i) - 5);
            else
                s2 += (char)(s.charAt(i) - 6);
        }
        return s2;
    }
    private String decryptLine3(String s){
        String s2 = "";
        for(int i=s.length()-1; i>=0; i--){
            s2 += (char)(s.charAt(i) - 10);
        }
        return s2;
    }
    //Will start on the second line because the first line is the unencrypted header
    private int on_line;
    private String decryptString(String buffer) {
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
}
