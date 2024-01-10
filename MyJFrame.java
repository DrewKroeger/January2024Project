/* DREW KROEGER- JANUARY 2024 PROJECT
 * THIS IS A POMODORO CLOCK AND GOES WITH POMODORO DRIVER
 * SEE DRIVER FOR HOW IT WORKS
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;//for audio file
import java.io.IOException;//for audio file
public class MyJFrame implements ActionListener                 //action listener is used for the time
{
        JFrame frame = new JFrame();                            //this create the popup window/gui that we will need
        JButton start = new JButton("START");              //this creates the start button
        JButton reset = new JButton("RESET");              //this creates the stop button
        JLabel timeLabel = new JLabel();                        //this is used to display the time
        JLabel otherLabel = new JLabel();                       //this is used to display the iterations, on break, total elapsed seconds, and iteration seconds 
        int elapsedTime = 0;                                    //this is used only for the elapsed time display
        int elapsedTime2 = elapsedTime;                                   //this is used for making the audio que go off and resetting the minutes and seconds when appropriate
        int thisIterationSeconds =  elapsedTime/1000;                     //this is used for the iterations and getting the 30 minute break
        int seconds = 0;                                                  //simple as it sounds
        int minutes = 0;                                        
        int iterations = 0;                                               //when this reaches 4 we get thre thirty minute break, and 5 means we just reset back to 0
        boolean started = false;                                          //used for start/stop button
        boolean onBreak = false;                                          //used to know if we should be working or not
        String seconds_string = String.format("%02d", seconds);    //this is so we can see 2 zeroes always in minutes and seconds
        String minutes_string = String.format("%02d", minutes);






        Timer timer = new Timer(1000,new ActionListener()                           //this is used for the actual timer aspect of the clock, it updates once per 1000 milliseconds(1 second)
        {
            public void actionPerformed(ActionEvent e)          
            {
                elapsedTime += 1000;                                                     //elapsed time goes up
                elapsedTime2 += 1000;                                           
                thisIterationSeconds = thisIterationSeconds + 1;                
                minutes = (elapsedTime2/60000) % 60;
                seconds = (elapsedTime2/1000)%60;
                String seconds_string = String.format("%02d", seconds);
                String minutes_string = String.format("%02d", minutes);
                timeLabel.setText("Current Time: "+ minutes_string + ":" + seconds_string);//have to reset/update the time label text every second, and update other label
                otherLabel.setText("Iterations:" + iterations + "   On A Break?: " + onBreak + " Total Elapsed Time: " + elapsedTime/1000 + " This Iteration Seconds: " + thisIterationSeconds);
                
                if(thisIterationSeconds == 1500 || thisIterationSeconds == 1800)           //this plays every 25 minutes, then 5 minutes after everytime
                {
                    try 
                    {
                        audio();
                    } catch (UnsupportedAudioFileException e1) 
                    
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e1) 

                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (LineUnavailableException e1) 

                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }



                if(thisIterationSeconds >= 1500 && thisIterationSeconds < 1800)             //on break for last 5 minutes
                {
                    onBreak = true;
                }
                if(thisIterationSeconds == 1500 && (iterations < 4))                        //reset timer for last 5 minutes, unless iterations is 4 then we keep going
                {
                    elapsedTime2 = 0;
                    minutes = 0;
                    seconds = 0;
                }

                if(iterations == 4 && thisIterationSeconds < 1800)                          //on break for full 30 minutes
                {
                    onBreak = true;        
                }

                
                if(iterations == 4 && thisIterationSeconds >= 1800)                        //just reset the clock fully when we reach the end of the 30 minute break
                {
                    reset();
                    start();
                }
                else if(thisIterationSeconds >= 1800)                                      //if it isnt the fourth iteration we just reset everything except update iterations
                {
                    thisIterationSeconds = 0;
                    elapsedTime2 = 0;
                    minutes = 0;
                    seconds = 0;
                    onBreak = false;
                    iterations++;
                    
                }

            }//end of action performed
        });
    //*************************************************************

    // this is the jframe constructor

    MyJFrame()
    {
        timeLabel.setText( " Current Time: " + minutes_string + ":" + seconds_string); //sets the timelabel text
        timeLabel.setBounds(100,150,200,20);                          //wehre time label will be in the jframe, and its size
        timeLabel.setFont(new Font("Arial",Font.PLAIN,10));                  //sets the font for the time labe                                           
        timeLabel.setHorizontalAlignment(JTextField.CENTER);                           //where the text will be in its bounds

        otherLabel.setText("Iterations:" + iterations + "   On A Break?: " + onBreak); //see time label comments, but applies to otherLabel
        otherLabel.setBounds(0,50,400,50);
        otherLabel.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        otherLabel.setHorizontalAlignment(JTextField.CENTER);

        start.setBounds(50, 250, 70, 50);                             //se time labe;        
        start.setFont(new Font("Verdana",Font.BOLD,10));
        start.setFocusable(false);
        start.addActionListener(this);                                                 //this makes the button able to do something

        reset.setBounds(300, 250, 70, 50);
        reset.setFont(new Font("Verdana",Font.BOLD,10));
        reset.setFocusable(false);
        reset.addActionListener(this);

        frame.add(start);                                                               //adds the components to the jframe
        frame.add(reset);
        frame.add(otherLabel);
        frame.add(timeLabel);
        frame.setTitle("Pomodoro Clock");                                         //sets the top title for the clock
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                           //makes it so we can click the red x
        frame.setResizable(false);                                            //cannot change the size of the jframe
        frame.setSize(400, 400);                                           //size of the jframe
        frame.setVisible(true);                                                       //can actually see the jframe
           
    }
//*****************************************************************
    
    //this methods deals with button clicks

    public void actionPerformed(ActionEvent e)                                          //what happens when we click a button
    {
        
        if( e.getSource() == start)                                                     //what happens when we actually click start
        {
            
            if(started == false)                                                        //if we havent started we start the timer and change text to stop on button
            {
                started = true;
                start.setText("STOP");
                start();
            }
            else                                                                        //if we have started we change the text to start and stop the timer
            {
                started = false;
                start.setText("START");
                stop();
            }
        }   
        if(e.getSource() == reset)                                                      //if the button we click is reset we stop teh timer, reset all the variables,and make start button say start(instead of stop)
        {
            started = false;
            start.setText("START");
            reset();
        }
    }
    //*****************************************************************

    //this method starts the timer

    void start()
    {
        timer.start();
    }

    //*****************************************************************

    //this methods stops the timer

    void stop()
    {
        timer.stop();
    }

    //*****************************************************************
    
    //this methods resets the variables and timer, also called when we reach the end of our thirty minute break

    void reset()
    {
        timer.stop();
        elapsedTime = 0;
        elapsedTime2 = 0;
        seconds = 0;
        minutes = 0;
        iterations = 0;
        thisIterationSeconds = 0;
        onBreak = false;
        String seconds_string = String.format("%02d", seconds);
        String minutes_string = String.format("%02d", minutes);
        timeLabel.setText("Current Time: "+ minutes_string + ":" + seconds_string);
    }

    //*****************************************************************

    //this adds audio at the 25 and 30 minute mark everytime

    void audio() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        File file = new File("looperman-l-0405457-0044575-smithman888-pest (1).wav");   //add a new file
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);                   //get the audio from this file
        Clip clip = AudioSystem.getClip();  
        clip.open(audioStream);
        clip.start();
    }
    
}//end of class

