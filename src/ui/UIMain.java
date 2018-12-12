package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import lesson.Projector;
import lesson.Typewriter;
import reporter.Reporter;
import reporter.UserInfo;


/**
 *
 * @author Kevin Strileckis
 */
public class UIMain implements ActionListener {
    //Member variables
    
    //Sizes
    private final int XSIZE, YSIZE;
    
    //Containers
    private final JFrame top;
    private JPanel mainPanel;
    private JTextArea lcArea;
    private JScrollPane lcScrollPane;
    private JTextPane lessonDisplay;
    private JTextArea outputDisplay;
    
    private ImageIcon speIcon;
    
    private JLayeredPane layeredPane;
    private JTextArea workArea;
    private JTextPane copyOnArea;
    
    //File browser
    private JButton loadLesson;
    private JFileChooser browse;
    
    //Saving reports -- happens automatically at finishing a lesson, but user may wish to exit early
    private JButton saveReport;
    
    //Layout
    private final GridBagLayout layout;
    private final GridBagConstraints cons;
    
    //Projector
    private JButton submit;
    private Projector prof;
    private int onSection;
    private JLabel lessonInfoLabel;
    private JLabel sectionInfoLabel;
    
    //Typewriter
    private Typewriter typewriter;
    private JButton doTypewriter;
    private ImageIcon createImageIcon(String path, String description) throws MalformedURLException {
        path = path.replace("\\", "/");
       //System.out.println("file:///" + path);
        java.net.URL imgURL = new java.net.URL("file:///" + path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
        }
        return null;
    }
    public UIMain() {
        top = new JFrame("Strileckis Practice Environment (4.0) -- Session with " + UserInfo.getName());
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        top.setCursor(new Cursor(1));
        try {
            String path = new java.io.File( "." ).getCanonicalPath();
            speIcon = createImageIcon(path + "\\src\\images\\s.gif", "S");
        } catch (IOException ex) {
            Logger.getLogger(UIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(speIcon != null)
            top.setIconImage(speIcon.getImage());
        top.setLocation(270, 100);
        top.setLayout(layout);
        top.setPreferredSize(new Dimension(XSIZE=875, YSIZE=775));
        
        //Make the window's content resize on window resize
        top.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                System.out.println("Resized");
                resetWindow();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        
        mainPanel = new JPanel();
        
        //Get the classroom ready
        mainPanel.setPreferredSize(new Dimension(XSIZE-10, YSIZE-10));
        mainPanel.setLayout(layout);
        addLessonContentComponents();
        setUpMainWindow(0,1);
        setUpProjector();
        
        cons.gridx = 0;
        cons.gridy = 0;
        top.add(mainPanel, cons);
        
        top.pack();
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.setVisible(true);
    }
    private void addLessonContentComponents(){
        lcArea = new JTextArea("Lesson-related content will go in here");
        lcArea.setPreferredSize(new Dimension(XSIZE-20, 95));
        lcScrollPane = new JScrollPane(lcArea);
        cons.gridx=0;
        cons.gridy=0;
        mainPanel.add(lcScrollPane, cons);
    }
    
    private void setUpProjector()
    {
        prof = new Projector(this);
    }
    
    private void resetWindow()
    {
        int newWidth = top.getWidth();
        int newHeight = top.getHeight();
        mainPanel.setPreferredSize(new Dimension(newWidth-10, newHeight-10));
        lessonDisplay.setPreferredSize(new Dimension(newWidth, (newHeight-30)/2));
        lessonDisplay.setMinimumSize(new Dimension(newWidth/2 - 10, (newHeight-30)/2));
        workArea.setPreferredSize(new Dimension(newWidth - 2, newHeight-30 - 2));
        workArea.setMinimumSize(new Dimension(newWidth/2 - 10- 2, newHeight-30- 2));
        outputDisplay.setPreferredSize(new Dimension(newWidth, (newHeight-30)/2));
        outputDisplay.setMinimumSize(new Dimension(newWidth/2 - 10, (newHeight-30)/2));
        layeredPane.setPreferredSize(new Dimension(newWidth, (newHeight-30)/2));
        layeredPane.setMinimumSize(new Dimension(newWidth/2 - 10, (newHeight-30)/2));
        //Update scene
        mainPanel.repaint();
        //top.pack();
    }
    
    private void setUpMainWindow(int gx, int gy)
    {
        
        lessonInfoLabel = new JLabel("This is where lesson information will go");
        lessonInfoLabel.setMinimumSize(new Dimension(XSIZE-20, 50));
        cons.gridx = gx;
        cons.gridy = gy;
        cons.gridwidth = 2;
        mainPanel.add(lessonInfoLabel, cons);
        
        
        //Reset width and y padding values
        cons.gridwidth = 1;
        cons.ipady = 0;
        
        //Add the space for the section of the lesson we are on
        sectionInfoLabel = new JLabel("");
        cons.gridx = gx+0;
        cons.gridy = gy+1;
        cons.anchor = GridBagConstraints.NORTH;
        mainPanel.add(sectionInfoLabel, cons);
        
        //Add the "typewriter" button
        doTypewriter = new JButton("Do Typewriter Lesson");
        doTypewriter.addActionListener(this);
        cons.gridx = gx+1;
        cons.gridy = gy+1;
        cons.anchor = GridBagConstraints.NORTH;
        mainPanel.add(doTypewriter, cons);
        
        //Add the "load lesson" button
        loadLesson = new JButton("Load new lesson");
        loadLesson.addActionListener(this);
        cons.gridx = gx;
        cons.gridy = gy+2;
        cons.anchor = GridBagConstraints.NORTH;
        mainPanel.add(loadLesson, cons);
        
        //Add the "save report" button
        saveReport = new JButton("Save Report");
        saveReport.addActionListener(this);
        cons.gridx = gx;
        cons.gridy = 4+gy;
        cons.anchor = GridBagConstraints.NORTH;
        mainPanel.add(saveReport, cons);
        
        //Add the "submit" button
        submit = new JButton("Check work");
        submit.addActionListener(this);
        cons.gridx = 1+gx;
        cons.gridy = 2+gy;
        mainPanel.add(submit, cons);
        submit.setEnabled(false);
        
        //Set up JFileChooser and set default folder
        browse = new JFileChooser();
        try {
            //Set the default location to the parent directory. If this fails somehow, it will be Documents.
            browse.setCurrentDirectory(new File(UIMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
            //Try to open the lessons folder. If this fails, then the selector will fall back on the parent directory
            browse.setCurrentDirectory(new File(UIMain.class.getProtectionDomain().getCodeSource().getLocation().toURI() + "\\Lessons"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(UIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Add the lesson display area
        lessonDisplay = new JTextPane();
        Font font = new Font("Lucida Console", Font.BOLD, 14);
        lessonDisplay.setFont(font);
        lessonDisplay.setEditable(false);
        lessonDisplay.setText("Hello " + UserInfo.getName() + ", I wish you luck on your practice environment session(s) today."
                + "\nRemember, perfect practice makes perfect.");
        //lessonDisplay.setLineWrap(true);
        //lessonDisplay.setWrapStyleWord(true);
        lessonDisplay.setPreferredSize(new Dimension(XSIZE, (YSIZE-30)/2));
        lessonDisplay.setMinimumSize(new Dimension(XSIZE/2 - 10, (YSIZE-30)/2));
        lessonDisplay.setBorder(BorderFactory.createTitledBorder("Lesson Display Area"));
        lessonDisplay.setSelectionColor(Color.GREEN);
        cons.gridx = gx;
        cons.gridy = 3+gy;
        cons.gridwidth = 1;
        mainPanel.add(lessonDisplay, cons);
        
        //Add the output display area
        outputDisplay = new JTextArea();
        font = new Font("Lucida Console", Font.BOLD, 12);
        outputDisplay.setFont(font);
        outputDisplay.setForeground(Color.red);
        outputDisplay.setEditable(false);
        outputDisplay.setLineWrap(true);
        outputDisplay.setWrapStyleWord(true);
        outputDisplay.setPreferredSize(new Dimension(XSIZE, (YSIZE-30)/2));
        outputDisplay.setMinimumSize(new Dimension(XSIZE/2 - 10, (YSIZE-30)/2));
        outputDisplay.setBorder(BorderFactory.createTitledBorder("Output"));
        cons.gridx = gx;
        cons.gridy = 4+gy;
        cons.gridwidth = 1;
        mainPanel.add(outputDisplay, cons);
        
        //Add the work area
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(XSIZE, YSIZE-30));
        workArea = new JTextArea();
        copyOnArea = new JTextPane();
        
        font = new Font("Lucida Console", Font.BOLD, 13);
        //WorkArea
        workArea.setTransferHandler(null);//Disable copy/paste
        workArea.setFont(font);
        workArea.setForeground(Color.blue);
        workArea.setPreferredSize(new Dimension(XSIZE-2, YSIZE-32));
        workArea.setMinimumSize(new Dimension(XSIZE/2 - 10, YSIZE-32));
        workArea.setOpaque(false);
        //workArea.setBackground(new Color(0, 0, 0, 128));
        workArea.setBorder(BorderFactory.createTitledBorder("User's Work Area"));
        //CopyOnArea
        copyOnArea.setForeground(Color.red);
        copyOnArea.setPreferredSize(new Dimension(XSIZE-2, YSIZE-32));
        copyOnArea.setMinimumSize(new Dimension(XSIZE/2 - 10, YSIZE-32));
        copyOnArea.setOpaque(false);
        copyOnArea.setBorder(null);
        copyOnArea.setBackground(new Color(0, 0, 0, 128));
        //Set contraints
        cons.gridx = 1+gx;
        cons.gridy = 3+gy;
        cons.gridheight = 2;
        //Add our two areas to the layered pane
        layeredPane.setLayout(new BoxLayout(layeredPane, BoxLayout.PAGE_AXIS));
        //layeredPane.add(workArea, new Integer(1), 0);
        layeredPane.add(copyOnArea, new Integer(0), 0 );
        //Add the Layered Pane to mainpanel
        mainPanel.add(workArea, cons);
        
        //Update scene
        mainPanel.repaint();
        
        //Start at lesson segment code -1
        onSection = -1;
    }
    
    public void cleanSlate(){
        lessonDisplay.setText("");
        workArea.setText("");
        outputDisplay.setText("");
        lessonInfoLabel.setText("This is where lesson information will go");
        sectionInfoLabel.setText("");
        copyOnArea.setText("");
    }
    public void addLessonText(String text){
        lessonDisplay.setText(lessonDisplay.getText() + (text) );
    }
    public JTextPane getLessonDisplay(){
        return lessonDisplay;
    }
    
    
    
    //Implemented methods

    @Override
    public void actionPerformed(ActionEvent e) {
        int temp;
        
        if(e.getActionCommand().equals("Load new lesson"))
        {
            //If user is about to clear data, ask that this is really want is desired. Return, otherwise
            if(!checkSafeLoad()){
                if(
                        JOptionPane.showConfirmDialog(null, "Are you sure that you would like to open a new lesson and save over progress?")
                        !=
                        JOptionPane.YES_OPTION)
                    return;
            }
            cleanSlate();
            temp = browse.showOpenDialog(top);
            
            //handle if user selects a file
            if(temp == JFileChooser.APPROVE_OPTION){
                //Get lesson file
                File lesson = browse.getSelectedFile();
                prof.setUpLesson(lesson);
                onSection = 0;
                prof.doNextLesson(lessonDisplay, outputDisplay, onSection, copyOnArea, workArea);
                updateLessonInfo();
                submit.setEnabled(true);
            }
            //Handle if user does not select a file
            else{
                onSection = -1;
            }
        }
        else if(e.getActionCommand().equals("Do Typewriter Lesson")){
            typewriter = new Typewriter();
        }
        else if(e.getActionCommand().equals("Check work"))
        {
            if(checkWork()){
                cleanSlate();
                if(!(prof.doNextLesson(lessonDisplay, outputDisplay, ++onSection, copyOnArea, workArea))){
                    submit.setEnabled(false);
                    
                    //doNextLesson() creates file. So now reset onSection
                    onSection = -1;
                    //Increase score and notify user that the lesson has ended
                    congratulate();
                }
                else
                    updateLessonInfo();
            }
        }
        else if(e.getActionCommand().equals("Save Report"))
        {
            try {
                Reporter.exportUniqueFile();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UIMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(UIMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void updateLessonInfo(){
        lessonInfoLabel.setText(
                prof.getLessonTitle() + 
                        " ~ " + (onSection+1) + 
                        "/" + prof.getNumOfSections()
                );
        sectionInfoLabel.setText(prof.getSpecialCode());
    }
    
    private void congratulate(){
        System.out.println("FSDFDS");
        //Increase score
        UserInfo.incScore();
        //Congratulate user
        JOptionPane.showMessageDialog(null, "Hey! Who made it? You made it.");
    }
    
    //Make sure to prevent user from accidentally erasing data
        //Return true if lesson loading is fine
    private boolean checkSafeLoad(){
        if(onSection == -1)
            return true;
        return false;
    }
    
    private boolean checkWork(){
        boolean result = true;
        int temp;
        if(onSection > prof.getNumOfSections()){
            congratulate();
            return false;
        }
        String keyAnswer = prof.getFullLesson().get(onSection).getAnswer();
        //Remove all comments from answer key
        keyAnswer += "\n";
        
        while(keyAnswer.contains("//"))
        {
            for(temp=keyAnswer.indexOf("//"); keyAnswer.charAt(temp) != '\n' && temp < keyAnswer.length(); temp++ )
                ;
            if(temp >= keyAnswer.length())
                temp --;
            keyAnswer = keyAnswer.substring(0, keyAnswer.indexOf("//")) + keyAnswer.substring(temp);
        }
        
        if(keyAnswer.contains("Output"))
            keyAnswer = keyAnswer.substring(0, keyAnswer.indexOf("Output")).trim();
        else
            keyAnswer = keyAnswer.trim();
        //Change "i++" to "++i"
        if(keyAnswer.contains("i++"))
            keyAnswer = keyAnswer.replace("i++", "++i");
        
        String userAnswer = workArea.getText();
        //Remove all comments from user's code
        userAnswer += "\n";
        while(userAnswer.contains("//"))
        {
            for(temp=userAnswer.indexOf("//"); userAnswer.charAt(temp) != '\n' && temp < userAnswer.length()-1; temp++ )
                ;
            if(temp >= userAnswer.length())
                temp --;
            userAnswer = userAnswer.substring(0, userAnswer.indexOf("//")) + userAnswer.substring(temp);
        }
        //Remove "Output"
        if(userAnswer.contains("Output"))
            userAnswer = userAnswer.substring(0, userAnswer.indexOf("Output")).trim();
        else
            userAnswer = userAnswer.trim();
        //Change "i++" to "++i"
        if(userAnswer.contains("i++"))
            userAnswer = userAnswer.replace("i++", "++i");
        
        
        String newLeft = "";
        String newRight = "";
        
        for(int i=0, tmp=keyAnswer.length(); i<tmp; ++i)
            if(keyAnswer.charAt(i) != ' ' && keyAnswer.charAt(i) != '\t' && keyAnswer.charAt(i) != '\n')
                newLeft+=keyAnswer.charAt(i);
        
        for(int i=0, tmp=userAnswer.length(); i<tmp; ++i)
            if(userAnswer.charAt(i) != ' ' && userAnswer.charAt(i) != '\t' && userAnswer.charAt(i) != '\n')
                newRight+=userAnswer.charAt(i);
        
        if(newLeft.equals(newRight)){
            prof.getFullLesson().get(onSection).incScore();
            return true;
        }
        
        //If answer is incorrect, alert the user and decrease their score
        JOptionPane.showMessageDialog(null, "Your answer would not produce that output");
        prof.getFullLesson().get(onSection).decScore();
        return false;
    }
    
}
