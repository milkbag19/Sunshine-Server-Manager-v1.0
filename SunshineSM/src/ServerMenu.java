 /*/
 * 
 * Created By  : Joshua Wells
 * Last Update : 2019/08/05
 * 
 /*/

import java.io.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.geom.*;
import javax.swing.text.*;
import java.net.*;
import javax.swing.plaf.*;
import javax.imageio.ImageIO;
import java.text.DecimalFormat;

public class ServerMenu {

  static JButton infoButton = new JButton();
  static JButton stop = new JButton("Shutdown");
  static JButton start = new JButton("Offline");
  static FileOutputStream out;
  static FileInputStream in;
  static Properties props;
  static String pvpEnabled, forceGamemodeEnabled, hardCoreEnabled;
  static Server server;
  static JScrollPane scrollPane = new JScrollPane();
  static Rcon rcon;
  static Border etch = BorderFactory.createEmptyBorder();
  static boolean running = false;
  static String ip; 
  static JTextField upTime;
  static String userHomePath = System.getProperty("user.dir");
  static FIO restartFile = new FIO(userHomePath+"\\restart.txt");
  static MineStat ms = new MineStat(ip, 25565);
  static JTextArea status = new JTextArea("Status : Offline");
  static Point clickCoords;
  private static Color background = new Color(35,39,42);
  private static Font logFont = new Font("Andale Mono", Font.PLAIN, 12);
  static FIO serverStatsFIO = new FIO(userHomePath+"//plugins//OLSMonitor//data.monitordata");
  static DecimalFormat f = new DecimalFormat("##");
  static JBox mainBox, logBox;
  static JPanel settingPanel = new JPanel();
  static JLabel difficultyLabel;
  static JPanel difficultyPanel, gamemodePanel, ramPanel;
  private static FIO settingsFIO = new FIO(userHomePath+"//settings.txt");
  static JComboBox ramComboBox;
  static JLabel versionLabel = new JLabel("v1.0");
  
  public static void main(String[] args) { 
    //Any variable declararition that recquires exception handling
    try{
      ip = InetAddress.getLocalHost().getHostAddress().toString();
      in = new FileInputStream(userHomePath+"//server.properties");
      props = new Properties();
      props.load(in);
      in.close();
      System.out.println(userHomePath);
    }catch(Exception e){}
    ////////////////////////////////////////////////////////////////
    
    
    JFrame mainFrame = new JFrame("Server Menu");  //Main JFrame of the program
    JPanel mainPanel = new JPanel();               //JPanel used to place JComponents on the program
    mainFrame.setBackground(background);           //setting the background color of the main JFrame to dark gray
    JPanel titleBar = new JPanel();                //Declaring the title bar so we have space to place a close button and any other buttons/labels
    //Setting up titlebar drag functionality 
    titleBar.addMouseListener(new MouseAdapter() { 
      public void mouseReleased(MouseEvent e){
        clickCoords = null;
      }
      public void mousePressed(MouseEvent e){
        clickCoords = e.getPoint();
      }
      public void mouseEntered(MouseEvent e){
      }
      public void mouseExited(MouseEvent e){
      }
    });
    titleBar.addMouseMotionListener(new MouseAdapter() {
      public void mouseMoved(MouseEvent e){
      }
      public void mouseDragged(MouseEvent e){
        Point currCoords = e.getLocationOnScreen();
        mainFrame.setLocation(currCoords.x - clickCoords.x, currCoords.y - clickCoords.y);
      }
    });
    //////////////////////////////////////////
    
    //Setting several properties of the titlebar to make it look pretty
    titleBar.setPreferredSize(new Dimension(1100,43));
    titleBar.setBackground(background);
    titleBar.setBorder(null);
    
    //Program logo to be painted onto title bar
    JLabel logoLabel = new JLabel();
    try{
    logoLabel.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//sunshineIcon.png")).getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
    }catch(Exception lawliet){}
    JLabel nameLabel = new JLabel("Sunshine SM");
    nameLabel.setForeground(Color.LIGHT_GRAY);
    titleBar.add(logoLabel);
    titleBar.add(nameLabel);
    titleBar.add(versionLabel);
    titleBar.add(Box.createRigidArea(new Dimension(885,1)));
    ///////////////////////////////////////////

    //setting up the exit button for the titlebar and setting its properties
    JButton exitButton = new JButton("\u274C");
    exitButton.setPreferredSize(new Dimension(50,29));
    exitButton.setBackground(background);
    exitButton.setForeground(Color.white);
    exitButton.setBorder(null);
    //adding closing functionality for the exit button
    exitButton.addMouseListener(new MouseAdapter(){
      public void mouseEntered(MouseEvent e){
        exitButton.setBackground(Color.red);
      }
      public void mouseExited(MouseEvent e){
        exitButton.setBackground(background);
      }
    });
    exitButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) { 
        try{
          settingsFIO.save(ramComboBox.getSelectedItem().toString());
          rcon = new Rcon(ip, 25575, "1234".getBytes());
          rcon.command("stop");
        }catch(Exception f){}
        System.exit(0);
      }
    });
    /////////////////////////////////////////////////////
    titleBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
    titleBar.add(exitButton);
    //////////////////////////////////////////////////////////////////////////

    //setting up the tabs panel where we have the dashboard tab, settings tab, etc.
    JPanel tabsPanel = new JPanel();
    tabsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    tabsPanel.setBackground(new Color(44,47,51));
    tabsPanel.setPreferredSize(new Dimension(150,700));
    tabsPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.white));
    ///////////////////////////////////////////////////////////////////////////////
    
    //dashboard page
    JButton dashboard = new JButton();
    try{
      dashboard.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//dashboard.png")).getScaledInstance(70, 65, Image.SCALE_DEFAULT)));
    }catch(Exception e){e.printStackTrace();}
    dashboard.setPreferredSize(new Dimension(140,75));
    dashboard.setBackground(new Color(204, 102, 0));
    dashboard.setBorder(null);
    //adding hover color change functionality
    dashboard.addMouseListener(new MouseAdapter(){
      public void mouseEntered(MouseEvent e){
        if(!dashboard.getBackground().equals(new Color(204, 102, 0))) {
          dashboard.setContentAreaFilled(true);
          dashboard.setBackground(new Color(255, 153, 51));
        }
      }
      public void mouseExited(MouseEvent e){
        if(!dashboard.getBackground().equals(new Color(204, 102, 0))){
          dashboard.setContentAreaFilled(false);
        }
      }
    });
    ///////////////////////////////////////////

    ////////////////
    
    //settings page
    JButton settingButton = new JButton();
    try{
      settingButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//settings.png")).getScaledInstance(70, 65, Image.SCALE_DEFAULT)));
    }catch(Exception e){e.printStackTrace();}
    settingButton.setPreferredSize(new Dimension(140,75));
    settingButton.setContentAreaFilled(false);
    settingButton.setBorder(null);
    //Adding hover color changing functionality 
    settingButton.addMouseListener(new MouseAdapter(){
      public void mouseEntered(MouseEvent e){
        if(!settingButton.getBackground().equals(new Color(204, 102, 0))) {
          settingButton.setContentAreaFilled(true);
          settingButton.setBackground(new Color(255, 153, 51));
        }
      }
      public void mouseExited(MouseEvent e){
        if(!settingButton.getBackground().equals(new Color(204, 102, 0))){
          settingButton.setContentAreaFilled(false);
        }
      }
    });
    /////////////////////////////////////////////
    settingPanel.setPreferredSize(new Dimension(950,500));
    settingPanel.setBackground(new Color(44,47,51));
    settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    //adding settings tab switching functionality
    settingButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        try{
          settingButton.setContentAreaFilled(true);
          settingButton.setBackground(new Color(204, 102, 0));
          dashboard.setBackground(null);
          infoButton.setBackground(null);
          mainFrame.remove(mainBox);
          mainBox.removeAll();
          mainBox = JBox.vbox(
                              JBox.vbox(titleBar), JBox.hbox(tabsPanel,settingPanel)
                             );
          mainFrame.add(mainBox);
          mainFrame.setVisible(true);
        }catch(Exception lmao){lmao.printStackTrace();}
      }
    });
    //////////////////////////////////////////////

    ///////////////

    //adding functionality to switch to dashboard page
    dashboard.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        try{
          dashboard.setContentAreaFilled(true);
          dashboard.setBackground(new Color(204, 102, 0));
          settingButton.setBackground(null);
          infoButton.setBackground(null);
          mainFrame.remove(mainBox);

          mainBox.removeAll();
          mainBox = JBox.vbox(
                  JBox.vbox(titleBar), JBox.hbox(tabsPanel,logBox)
          );
          mainFrame.add(mainBox);
          mainFrame.pack();
          mainFrame.setVisible(true);
        }catch(Exception lmao){lmao.printStackTrace();}
      }
    });
    ////////////////////////////////////////////////////

    //setting up the look and feel of the software.  although not much is changed from this due to the fact that ive customized mostly everything, it might become useful at some point.  
    try { 
      UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch(Exception ignored){}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Border emptyBorder = BorderFactory.createEmptyBorder();

    //setting up the difficulty setting box
    String[] diff = {"Peaceful","Easy","Normal","Hard"};
    JComboBox difficultyComboBox = new JComboBox(diff);
    difficultyComboBox.setBackground(new Color(44,47,51));
    difficultyComboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    difficultyComboBox.setPreferredSize(new Dimension(140,30));
    difficultyComboBox.setFont(new Font("", Font.PLAIN,16));
    ///////////////////////////////////////
    
    //creating a panel for the setting box to be painted onto
    difficultyPanel = new JPanel();
    difficultyPanel.add(difficultyComboBox);
    difficultyPanel.setBackground(new Color(44,47,51));

    /////////////////////////////////////////////////////////
    
    //setting up the gamemode setting box
    String[] gmode = {"Creative","Survival","Adventure","Spectator"};
    JComboBox gamemodeComboBox = new JComboBox(gmode);
    gamemodeComboBox.setBackground(new Color(44,47,51));
    gamemodeComboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    gamemodeComboBox.setPreferredSize(new Dimension(140,30));
    gamemodeComboBox.setFont(new Font("", Font.PLAIN,16));
    //////////////////////////////////////
    
    //creating a panel for the gamemode setting box to be painted onto
    gamemodePanel = new JPanel();
    gamemodePanel.add(gamemodeComboBox);
    gamemodePanel.setBackground(new Color(44,47,51));
    //////////////////////////////////////////////////////////////////
    
    //setting up the ram/heap size setting box
    String[] ram = {"1G","2G","4G","6G","8G","10G","12G"};
    ramComboBox = new JComboBox(ram);
    ramComboBox.setBackground(new Color(44,47,51));
    ramComboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    ramComboBox.setPreferredSize(new Dimension(140,30));
    ramComboBox.setFont(new Font("", Font.PLAIN,16));

    ramComboBox.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                      if(ramComboBox.getSelectedIndex()>0){
                                        JOptionPane.showMessageDialog(mainPanel, "Make sure you have JRE 64 Bit. \nIf you attempt to run server with 32 Bit JRE,\nThe server WILL NOT run.", "Check Java Bit Version", JOptionPane.WARNING_MESSAGE);
                                      }
                                    }
                                  });

    ///////////////////////////////////////////
    
    //creating a panel for the ram/heap size setting box to be painted onto
    ramPanel = new JPanel();
    ramPanel.add(ramComboBox);
    ramPanel.setBackground(new Color(44,47,51));
    ///////////////////////////////////////////////////////////////////////
    
    //setting up the true/false box for the nether travel setting box
    JCheckBox allowNetherTravelBox = new JCheckBox("Nether");
    allowNetherTravelBox.setBackground(new Color(44,47,51));
    try{
      //creating a custom checkbox icon because the default one looks ass
      allowNetherTravelBox.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//checkEnabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
    }catch(Exception bruh){}
    ///////////////////////////////////////////////////////////////////
    
    //setting up the shutdown button for the server

    
    /*
     * Adding the functionality to the shutdown button.
     * The Shutdown button requests a connection to the servers remote console, then sends the "stop" command to the server,
     * which then saves all data and shuts down the server.
     */

    stop.setPreferredSize(new Dimension(85,35));
    stop.setForeground(Color.white);
    stop.setUI(new StyledButtonUI());
    stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {

        String[] statusString = status.getText().split(" ");
        if(statusString[2].equals("Online")){
          try{

            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("stop");
            rcon.disconnect();
            status.setText("Status : Offline");
            start.setText("Offline");
            start.setForeground(new Color(255, 80, 80));
          }catch(Exception e){e.printStackTrace();}

        }
        else{
          JOptionPane.showMessageDialog(mainPanel, "The server must be running to shutdown.", "Server Not Running", JOptionPane.WARNING_MESSAGE);
        }
      }});
    /////////////////////////////////////////////////////
    
    //setting up the pvp checkbox and all the properties
    JCheckBox pvp = new JCheckBox();
    try {
      pvp.setIcon( new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//checkDisabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)) );
      pvp.setSelectedIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//checkEnabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)) );
    }catch(Exception g){g.printStackTrace();}
    pvp.setBackground(new Color(44,47,51));
    pvp.setForeground(new Color(255,255,255));

    JLabel pvpLabel = new JLabel("PVP");
    pvpLabel.setFont(new Font("Professional font", Font.PLAIN,20));
    pvpLabel.setForeground(Color.white);

    JBox pvpBox = JBox.hbox(pvp,JBox.vbox(pvpLabel,Box.createRigidArea(new Dimension(0,3))));
    pvpBox.setPreferredSize(new Dimension(100,30));
    pvpBox.setBackground(new Color(44,47,51));

    JCheckBox hardCore = new JCheckBox();
    try {
      hardCore.setIcon( new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//checkDisabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)) );
      hardCore.setSelectedIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//checkEnabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)) );
    }catch(Exception g){g.printStackTrace();}
    hardCore.setBackground(new Color(44,47,51));
    hardCore.setForeground(new Color(255,255,255));

    JLabel hardcoreLabel = new JLabel("Hardcore");
    hardcoreLabel.setFont(new Font("Professional font", Font.PLAIN,20));
    hardcoreLabel.setForeground(Color.white);

    JBox hardcoreBox = JBox.hbox(hardCore,JBox.vbox(hardcoreLabel,Box.createRigidArea(new Dimension(0,3))));
    hardcoreBox.setPreferredSize(new Dimension(100,30));
    hardcoreBox.setBackground(new Color(44,47,51));

    JCheckBox forceGamemode = new JCheckBox();
    try {
      forceGamemode.setIcon( new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//checkDisabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)) );
      forceGamemode.setSelectedIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//checkEnabled.png")).getScaledInstance(25, 25, Image.SCALE_DEFAULT)) );
    }catch(Exception g){g.printStackTrace();}
    forceGamemode.setBackground(new Color(44,47,51));
    forceGamemode.setForeground(new Color(255,255,255));

    JLabel forceGamemodeLabel = new JLabel("Force Gamemode");
    forceGamemodeLabel.setFont(new Font("Professional font", Font.PLAIN,20));
    forceGamemodeLabel.setForeground(Color.white);

    JBox forceGamemodeBox = JBox.hbox(forceGamemode,JBox.vbox(forceGamemodeLabel,Box.createRigidArea(new Dimension(0,3))));
    forceGamemodeBox.setPreferredSize(new Dimension(100,30));
    forceGamemodeBox.setBackground(new Color(44,47,51));

    //pvp.setPreferredSize(new Dimension(100,100));
    //////////////////////////////
    
    /* 
     * setting up the log box where server output is displayed.  
     * The program grabs the text from the log file "latest.log".
     * It recursively scans the file if there have been changes in the text, and if so, it displayed the text onto the log display.
     */
    JTextArea logField = new JTextArea();
    logField.setEditable(false);
    logField.setForeground(new Color(255,255,255));
    logField.setFont(logFont);
    logField.setBackground(new Color(52,55,59));
    logField.setBorder(null);
    ///////////////////////////////////////////////////////////
    
    //creating a panel that the log and command field will the painted onto
    JPanel logPanel = new JPanel();
    logPanel.add(logField);
    logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.PAGE_AXIS));
    logPanel.setBorder(null);
    ////////////////////////////////////////////////////////////////////////

    stop.setBackground(logField.getBackground());

    //creating a scroll window so that the user can scroll through the log on the display.
    scrollPane = new JScrollPane(logPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setWheelScrollingEnabled(true);
    scrollPane.setPreferredSize(new Dimension(550,500));
    scrollPane.setBackground(new Color(35,150,42));
    scrollPane.setForeground(new Color(255,255,255));
    scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.white));
    scrollPane.getVerticalScrollBar().setBackground(logField.getBackground());
    //////////////////////////////////////////////////////////////////////////////////////
    
    //setting up a command bar so the user can send commands to the sever via remote console.
    JTextField commandField = new JTextField(" Enter command");
    commandField.setLayout(null);
    commandField.setBorder(BorderFactory.createLineBorder(Color.white));
    commandField.setBackground(Color.white);
    commandField.setForeground(new Color(255,255,255));
    commandField.setPreferredSize(new Dimension(550,30));
    /////////////////////////////////////////////////////////////////////////////////////////
    
    //adding a focus listener so that when the user clicks onto the command bar, it makes it empty for the user to input commands.  
    commandField.addFocusListener(new FocusListener(){
      @Override
      public void focusGained(FocusEvent e){
        if(commandField.getText().equals(" Enter command")){
          commandField.setText("");
        }
        else{
        }
      }
      public void focusLost(FocusEvent e){
        if(commandField.getText().equals(""))
          commandField.setText(" Enter command");
      }
    });
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //setting up an action listener for the command bar that will send the commands through to the server
    commandField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try{
          
          rcon = new Rcon(ip, 25575, "1234".getBytes());
          System.out.println(rcon.command(commandField.getText()));

        }catch(Exception e){}
      }});
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //These switch statements are getting the current property data of the server and setting the stting boxes to match the data.
    switch(props.getProperty("difficulty")){
      case "peaceful":
        difficultyComboBox.setSelectedItem("Peaceful");
        break;
      case "easy":
        difficultyComboBox.setSelectedItem("Easy");
        break;
      case "normal":
        difficultyComboBox.setSelectedItem("Normal");
        break;
      case "hard":
        difficultyComboBox.setSelectedItem("Hard");
        break;
    }
    switch(props.getProperty("gamemode")){
      case "creative":
        gamemodeComboBox.setSelectedItem("Creative");
        break;
      case "survival":
        gamemodeComboBox.setSelectedItem("Survival");
        break;
      case "adventure":
        gamemodeComboBox.setSelectedItem("adventure");
        break;
      case "spectator":
        gamemodeComboBox.setSelectedItem("Spectator");
        break;
    }
    switch(props.getProperty("pvp")){
      case "true":
        pvp.setSelected(true);
        break;
      case "false":
        pvp.setSelected(false);
        break;
    }
    switch(props.getProperty("force-gamemode")){
      case "true":
        forceGamemode.setSelected(true);
        break;
      case "false":
        forceGamemode.setSelected(false);
        break;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //setting up the launch button for the sever

    //adding startup functionality to the launch button.
    start.setBackground(logField.getBackground());
    start.setForeground(new Color(255, 80, 80));
    start.setPreferredSize(new Dimension(85,35));
    start.setUI(new StyledButtonUI());
    start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        String[] statusString = status.getText().split(" ");
        if(statusString[2].equals("Offline")&&!start.getText().equals("Launching")){
          running = true;
          settingsFIO.save(ramComboBox.getSelectedItem().toString());
          ms = new MineStat(ip, 25565);
          start.setText("Launching");
          start.setForeground(new Color(51, 204, 255));
          if(pvp.isSelected()==true)
            pvpEnabled = "true";
          else
            pvpEnabled = "false";

          if(forceGamemode.isSelected())
            forceGamemodeEnabled = "true";
          else
            forceGamemodeEnabled = "false";

          if(hardCore.isSelected())
            hardCoreEnabled = "true";
          else
            hardCoreEnabled = "false";
          System.out.print(pvpEnabled);
          server = new Server(difficultyComboBox.getSelectedItem().toString().toLowerCase(),gamemodeComboBox.getSelectedItem().toString().toLowerCase(),pvpEnabled,forceGamemodeEnabled,hardCoreEnabled,ramComboBox.getSelectedItem().toString(),ip);
          server.start();
          
        }
        else{
          JOptionPane.showMessageDialog(mainPanel, "The server is already running.", "Server Already Running", JOptionPane.WARNING_MESSAGE);
        }
      }});
    ////////////////////////////////////////////
    
    /*
     * below are the closing and opening of the program listeners.  
     * When the program is opened, it checks the the "restart.txt" file is set to true, and if so
     * it will automatically start up the server.  this enabled the ability to send the restart command to the server and it
     * restart the program and the server at the same time.
     * If the file is set to false, nothing will occur.
    */
    mainFrame.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        try{

          rcon = new Rcon(ip, 25575, "1234".getBytes());
          rcon.command("stop");
        }catch(Exception f){f.printStackTrace();}
        System.exit(0);
      }});
    mainFrame.addWindowListener(new WindowAdapter(){
      public void windowOpened(WindowEvent e){
        ramComboBox.setSelectedItem(settingsFIO.load(1));


        if(restartFile.load(1).toString().equals("1")){
          running = true;
          start.setText("Launching");
          if(pvp.isSelected()==true)
            pvpEnabled = "true";
          else
            pvpEnabled = "false";

          if(forceGamemode.isSelected())
            forceGamemodeEnabled = "true";
          else
            forceGamemodeEnabled = "false";

          if(hardCore.isSelected())
            hardCoreEnabled = "true";
          else
            hardCoreEnabled = "false";
          System.out.print(pvpEnabled);
          server = new Server(difficultyComboBox.getSelectedItem().toString().toLowerCase(),gamemodeComboBox.getSelectedItem().toString().toLowerCase(),pvpEnabled,forceGamemodeEnabled,hardCoreEnabled,ramComboBox.getSelectedItem().toString(),ip);

          server.start();
          restartFile.save("0");
        }
      }});
    
    //setting the program logo to the minecraft grass block.
    ImageIcon img = new ImageIcon(userHomePath+"//Images//sunshineIcon.png");
    mainFrame.setIconImage(img.getImage());
    ///////////////////////////////////////////////////////
    
    //setting up the cpu usage meter.  
    JProgressBar cpuUsageGauge = new JProgressBar();
    Font cpuFont = new Font("Andale Mono", Font.BOLD, 45);
    cpuUsageGauge.setUI(new ProgressCircleUI());
    cpuUsageGauge.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    cpuUsageGauge.setStringPainted(true);
    cpuUsageGauge.setFont(cpuUsageGauge.getFont().deriveFont(24f));
    cpuUsageGauge.setForeground(new Color(204, 102, 0));
    cpuUsageGauge.setPreferredSize(new Dimension(125,125));
    JBox cpuBox = JBox.vbox(Box.createRigidArea(new Dimension(10,10)),cpuUsageGauge);
    cpuBox.setBackground(new Color(44,47,51));
    /////////////////////////////////

    //setting up the ram usage meter
    JProgressBar ramUsageGauge = new JProgressBar();
    ramUsageGauge.setUI(new ProgressCircleUI());
    ramUsageGauge.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    ramUsageGauge.setStringPainted(true);
    ramUsageGauge.setFont(cpuUsageGauge.getFont().deriveFont(24f));
    ramUsageGauge.setForeground(new Color(204, 102, 0));
    ramUsageGauge.setPreferredSize(new Dimension(125,125));
    JBox ramBox = JBox.vbox(Box.createRigidArea(new Dimension(10,10)),ramUsageGauge);
    ////////////////////////////////
    
    //setting up the player count display
    JTextArea playerCountLabel = new JTextArea("0/20");
    playerCountLabel.setFont(cpuFont);
    playerCountLabel.setForeground(Color.white);
    playerCountLabel.setEditable(false);
    JBox playersBox = JBox.vbox(Box.createRigidArea(new Dimension(10,48)),playerCountLabel);
    /////////////////////////////////////

    try{
      //log box is a JBox that is set up to display the entire dashboard/server log.
      //having it allocated to its own JBox allows me to easily have each tab modularized.
      //FIO cpuImage = new FIO(new File());
      logBox = JBox.vbox(Box.createRigidArea(new Dimension(0,10)),
               JBox.hbox(Box.createRigidArea(new Dimension(10,0)),
               scrollPane,Box.createRigidArea(new Dimension(10,0))),
               Box.createRigidArea(new Dimension(1,10)),
               JBox.hbox(Box.createRigidArea(new Dimension(10,1)),commandField,Box.createRigidArea(new Dimension(27,1))),
               Box.createRigidArea(new Dimension(10,10)),
               JBox.hbox(Box.createRigidArea(new Dimension(75,10)),Box.createRigidArea(new Dimension(15,1)),JBox.vbox(new JLabel(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//cpu.png")).getScaledInstance(100, 100, Image.SCALE_DEFAULT))),Box.createRigidArea(new Dimension(15,15))),cpuBox,Box.createRigidArea(new Dimension(25,0)),JBox.vbox(new JLabel(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//ram.PNG")).getScaledInstance(135, 70, Image.SCALE_DEFAULT))),Box.createRigidArea(new Dimension(1,25))),ramBox,Box.createRigidArea(new Dimension(25,25)),JBox.vbox(new JLabel(new ImageIcon(ImageIO.read(new File(userHomePath+"//Images//players.png")).getScaledInstance(75, 75, Image.SCALE_DEFAULT))),Box.createRigidArea(new Dimension(1,25))),Box.createRigidArea(new Dimension(25,0)),playersBox),
               Box.createRigidArea(new Dimension(10,10))
               );
      logBox.setPreferredSize(new Dimension(titleBar.getWidth(),700));
      logBox.setBackground(new Color(44,47,51));
      ////////////////////////////////////////////////////////////////////////////////////
      
      //setting up the setting panel so that it can be easily inserted into the display when switching tabs.

      JLabel gameplaySettingsLabel = new JLabel("Gameplay");
      gameplaySettingsLabel.setFont(new Font("", Font.PLAIN,26));
      gameplaySettingsLabel.setForeground(Color.white);

      JBox comboBoxesBox = JBox.hbox(difficultyPanel,ramPanel,gamemodePanel);
      comboBoxesBox.setBackground(new Color(44,47,51));

      JBox checkBoxesBox = JBox.vbox(pvpBox,Box.createRigidArea(new Dimension(10, 0)),hardcoreBox,Box.createRigidArea(new Dimension(10, 0)),forceGamemodeBox);
      checkBoxesBox.setBackground(new Color(44,47,51));

      JBox settingBox = JBox.vbox(gameplaySettingsLabel,Box.createRigidArea(new Dimension(0,10)),checkBoxesBox,comboBoxesBox);
      settingBox.setBackground(new Color(44,47,51));

      settingPanel.add(settingBox);

      ///////////////////////////////////////////////////////////////////////////////////////////////////////
      
      //the main JBox is where everything fits together.  
      //here we are displaying the dashboard page by default when the program starts.
      mainBox = JBox.vbox(
                          JBox.vbox(titleBar), JBox.hbox(tabsPanel,logBox)
                         );
      //////////////////////////////////////////////////////////////////////////////

      //Software Version Label Setup
      versionLabel.setForeground(Color.lightGray);
      tabsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      tabsPanel.add(dashboard);
      tabsPanel.add(settingButton);


      //Info page setup
      JPanel infoPanel = new JPanel();
      infoPanel.setPreferredSize(new Dimension(950,500));
      infoPanel.setBackground(new Color(44,47,51));
      JLabel largeSunshineLogo = new JLabel();

      try {
        largeSunshineLogo.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//sunshine.png")).getScaledInstance(200, 250, Image.SCALE_DEFAULT)));
        infoButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//info.png")).getScaledInstance(75, 75, Image.SCALE_DEFAULT)));
      }catch(java.io.IOException f){}



      infoButton.setPreferredSize(new Dimension(140,75));
      infoButton.setContentAreaFilled(false);
      infoButton.setBorder(null);

      //Adding hover color changing functionality
      infoButton.addMouseListener(new MouseAdapter(){
        public void mouseEntered(MouseEvent e){
          if(!infoButton.getBackground().equals(new Color(204, 102, 0))) {
            infoButton.setContentAreaFilled(true);
            infoButton.setBackground(new Color(255, 153, 51));
          }
        }
        public void mouseExited(MouseEvent e){
          if(!infoButton.getBackground().equals(new Color(204, 102, 0))){
            infoButton.setContentAreaFilled(false);
          }
        }
      });

      /////////////////////////////////////////////
      //adding Info page switching functionality
      infoButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
          try{
            infoButton.setContentAreaFilled(true);
            infoButton.setBackground(new Color(204, 102, 0));
            dashboard.setBackground(null);
            settingButton.setBackground(null);
            mainFrame.remove(mainBox);
            mainBox.removeAll();
            mainBox = JBox.vbox(
                    JBox.vbox(titleBar), JBox.hbox(tabsPanel,infoPanel)
            );
            mainFrame.add(mainBox);
            mainFrame.setVisible(true);
          }catch(Exception lmao){lmao.printStackTrace();}
        }
      });

      //setting up the visuals for the info page
      JLabel sunshineLabel = new JLabel("Sunshine SM");
      sunshineLabel.setForeground(Color.white);
      sunshineLabel.setFont(new Font("Professional font", Font.PLAIN,38));

      JLabel creatorInfo = new JLabel("Programmer/Visual Designer : Josh Wells");
      creatorInfo.setFont(new Font("Professional font", Font.PLAIN,18));
      creatorInfo.setForeground(Color.white);

      JLabel infoVersionLabel = new JLabel("v1.0");
      infoVersionLabel.setFont(new Font("Professional font", Font.PLAIN,14));
      infoVersionLabel.setForeground(Color.white);

      JLabel lastUpdateLabel = new JLabel("Last Updated : 2019 - 08 - 12");
      lastUpdateLabel.setFont(new Font("Professional font", Font.PLAIN,14));
      lastUpdateLabel.setForeground(Color.white);

      JBox infoAreaBox = JBox.hbox(largeSunshineLogo,JBox.vbox(sunshineLabel,infoVersionLabel,Box.createRigidArea(new Dimension(0,10)),creatorInfo,Box.createRigidArea(new Dimension(0,10)),lastUpdateLabel));
      infoAreaBox.setLayout(new FlowLayout(FlowLayout.CENTER));

      JBox infoBox = JBox.vbox(Box.createRigidArea(new Dimension(0,200)),infoAreaBox);
      infoBox.setBackground(new Color(44,47,51));

      //Quick command buttons setup
      JButton nightTimeButton = new JButton();
      nightTimeButton.setUI(new StyledButtonUI());
      nightTimeButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//nightTime.png")).getScaledInstance(28, 28, Image.SCALE_DEFAULT)));
      nightTimeButton.setPreferredSize(new Dimension(36,36));
      nightTimeButton.setBackground(start.getBackground());

      JButton morningTimeButton = new JButton();
      morningTimeButton.setUI(new StyledButtonUI());
      morningTimeButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//morningTime.png")).getScaledInstance(28, 28, Image.SCALE_DEFAULT)));
      morningTimeButton.setPreferredSize(new Dimension(36,36));
      morningTimeButton.setBackground(start.getBackground());

      JButton dayTimeButton = new JButton();
      dayTimeButton.setUI(new StyledButtonUI());
      dayTimeButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//dayTime.png")).getScaledInstance(28, 28, Image.SCALE_DEFAULT)));
      dayTimeButton.setPreferredSize(new Dimension(36,36));
      dayTimeButton.setBackground(start.getBackground());

      nightTimeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("time set 13000");
          }catch(Exception h){}
        }
      });
      morningTimeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("time set 0");
          }catch(Exception h){}
        }
      });
      dayTimeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("time set 6000");
          }catch(Exception h){}
        }
      });

      JButton rainButton = new JButton();
      rainButton.setUI(new StyledButtonUI());
      rainButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//rain.png")).getScaledInstance(28, 28, Image.SCALE_DEFAULT)));
      rainButton.setPreferredSize(new Dimension(36,36));
      rainButton.setBackground(start.getBackground());

      JButton clearButton = new JButton();
      clearButton.setUI(new StyledButtonUI());
      clearButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//clear.png")).getScaledInstance(28, 28, Image.SCALE_DEFAULT)));
      clearButton.setPreferredSize(new Dimension(36,36));
      clearButton.setBackground(start.getBackground());

      JButton thunderButton = new JButton();
      thunderButton.setUI(new StyledButtonUI());
      thunderButton.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//thunder.png")).getScaledInstance(28, 28, Image.SCALE_DEFAULT)));
      thunderButton.setPreferredSize(new Dimension(36,36));
      thunderButton.setBackground(start.getBackground());

      rainButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("weather rain");
          }catch(Exception h){}
        }
      });
      clearButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("weather clear");
          }catch(Exception h){}
        }
      });
      thunderButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try{
            rcon = new Rcon(ip, 25575, "1234".getBytes());
            rcon.command("weather thunder");
          }catch(Exception h){}
        }
      });

      JLabel commandTitle = new JLabel();
      commandTitle.setIcon(new ImageIcon(ImageIO.read(new File(userHomePath + "//Images//commandTitle.png")).getScaledInstance(135, 28, Image.SCALE_DEFAULT)));
      commandTitle.setPreferredSize(new Dimension(135,28));
      commandTitle.setBackground(thunderButton.getBackground());
      /////////////////////////////

      infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
      infoPanel.add(infoBox);
      //////////////////////////////////////////

      tabsPanel.add(infoButton);
      tabsPanel.add(Box.createRigidArea(new Dimension(500,230)));
      tabsPanel.add(commandTitle);
      tabsPanel.add(rainButton);
      tabsPanel.add(clearButton);
      tabsPanel.add(thunderButton);
      tabsPanel.add(nightTimeButton);
      tabsPanel.add(morningTimeButton);
      tabsPanel.add(dayTimeButton);
      tabsPanel.add(Box.createRigidArea(new Dimension(100,5)));
      tabsPanel.add(start);
      tabsPanel.add(stop);
      //////////////////////////////

      //setting up last properties of the main JFrame.
      mainFrame.setUndecorated(true);
      mainFrame.add(mainBox);
      mainFrame.setSize(800,600);
      mainFrame.setVisible(true);
      mainFrame.pack();
      ///////////////////////////////////////////////


    }catch(Exception e){e.printStackTrace();}

    //Recursive Actions Below
    for(;;){
      try{
        server.getLog();
      }catch(Exception g){}
      try{
        String logOld = server.getLog();
        for(;;){
          try{
            //The program grabs the ram usage info from one of the plugins on the server and displays it.
            //it also uses this data to set the value of the circle progress bar. 
            String[] tempRam = serverStatsFIO.load(3).split("W");
            String[] usedRamString = tempRam[1].split("m");
            ramUsageGauge.setString(usedRamString[0]+"mb");
            Double usedRamInt = Double.parseDouble(usedRamString[0]);
            String[] selectedRamString = settingsFIO.load(1).toString().split("G");
            int totalRamInt = Integer.parseInt(selectedRamString[0]);
            Double usedRamPercent = (usedRamInt/(1024*totalRamInt)*100);
            try{
              ramUsageGauge.setValue((int)Math.round(usedRamPercent));
            }catch(Exception h){h.printStackTrace();}
            /////////////////////////////////////////////////////////////////////////////////////////////
            
            try{
              //same thing as ram, but for the cpu usage.
              //note: I used so many variables I gave up on the names.
              String[] cpuUsageString =  serverStatsFIO.load(4).split("W");
              cpuUsageGauge.setString(cpuUsageString[1].substring(0, 4)+"%");
              String cpuValueString = cpuUsageGauge.getString().substring(0,cpuUsageGauge.getString().length()-1);
              try{
                String[] whatEvenAreTheseVarNamesAnymore = cpuUsageGauge.getString().split("\\.");
                cpuUsageGauge.setValue(Integer.parseInt(whatEvenAreTheseVarNamesAnymore[0]));
              }catch(Exception h){}
              String[] temp = serverStatsFIO.load(4).split("W");
              String[] playerCountString = temp[2].split(" ");
              playerCountLabel.setText(playerCountString[0]+"/20");
            }catch(NullPointerException e){}
            //////////////////////////////////////////////
            
            //this action is checking if the server is online of offline and setting the text of a JLabel accordingly.
            status.setText("Status : "+server.getStatus());
            String[] statusString = status.getText().split(" ");
            if(statusString[2].equals("Online")){
              start.setText("Online");
              start.setForeground(new Color(0, 204, 0));
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////

            //this action is scanning the latest.log file and displays the changes if there are any.
            String logNew = server.getLog();
            if(!logOld.equals(logNew)){
              logField.setText(logNew);
              logField.setCaretPosition(logField.getDocument().getLength());
              logOld = logNew;
            }
            else{
            }
            ////////////////////////////////////////////////////////////////////////////////////////
          }catch(Exception f){}
        }
      }catch(Exception h){}
    }
  }
}