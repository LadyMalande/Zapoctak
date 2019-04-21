/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctak;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author Tereza Miklóšová
 */
public class Zapoctak {
    static JFrame frame;
    static JLabel label1;
    static JLabel imgLabel;
    static JPanel panel;
    static private Image img;
    static File file, tempFileBack, resetFile;
    static JMenuItem back, forward, saveFile, reset;
    
    public static Image getImage()
    {
    return img;
    }
    public static void setImage(Image i)
    {
        img = i;
    }
    
    private static void createTempFile(String what)
    {
        
        try
        {
            
            tempFileBack = File.createTempFile(what, ".jpg");
            tempFileBack.deleteOnExit();
            if(what.equals("reset")){
                resetFile = File.createTempFile(what, ".jpg");
                resetFile.deleteOnExit();
                }
            try
            {        
            ImageIO.write((BufferedImage)img, "jpg", tempFileBack);
            
            }
            catch(IOException e){JOptionPane.showMessageDialog(frame, "Soubor nemohl být vytvořen");}
            
        }
        catch(IOException e){JOptionPane.showMessageDialog(frame, "Soubor nemohl být vytvořen");}
        catch(SecurityException ex)
        {
            Path resourceDirectory = Paths.get("src","zapoctak","resources");
            tempFileBack = new File(resourceDirectory + what + ".jpg");
            try
            {        
            ImageIO.write((BufferedImage)img, "jpg", tempFileBack);
            tempFileBack.deleteOnExit();
            }
            catch(IOException e){JOptionPane.showMessageDialog(frame, "Soubor nemohl být vytvořen");}
        }
        
    }



    
 
       
    private static void useFilter(String filter)
    {
        createTempFile("back");
        back.setEnabled(true);
        switch(filter)
        {
            case "Zesvětli" :               { panel.setBackground(Color.red); Filters.zesvetli();panel.setBackground(Color.green);}
            break; 
            case "Ztmav"    :               {panel.setBackground(Color.red); Filters.ztmav(20); panel.setBackground(Color.green);} 
            break;
            case "Color -> Greyscale":      { panel.setBackground(Color.red);Filters.toGrayscale();panel.setBackground(Color.green);}
            break;
            case "Emboss":                  {panel.setBackground(Color.red); Filters.emboss();panel.setBackground(Color.green);}
            break;
            case "Gaussian blur":           {Filters.gaussianBlur();}
            break;
            case "Inverze":                 {Filters.invert();}
            break;
            case "Rozmazaný pohybem zleva": {Filters.motionBlur();}
            break;
            case "Rozmazaný pohybem zprava": {Filters.motionBlurRight();}
            break;
            case "Detekce hran světlá":     {Filters.edgeDetection(true);}
            break;
            case "Detekce hran tmavá":      {Filters.edgeDetection(false);}
            break;
            case "Zaostři":                 {Filters.sharpen();}
            break;
            case "Sobel":                   {Filters.sobel();}
            break;
        }
        showImage();
    }
    
    private static void showImage()
    {       
        int h = img.getHeight(frame);
        int w = img.getWidth(frame);
        float scale;
        Image imgSmaller;
        if (h >= w)
        {
            scale = (float)h/(float)w;
            int scaledw = (int)Math.round(800/scale);
            imgSmaller = img.getScaledInstance(scaledw, 800, Image.SCALE_SMOOTH);            
        }
        else
        {
            scale = (float)w/(float)h;
            int scaledh = (int)Math.round(800/scale);
            imgSmaller = img.getScaledInstance(800, scaledh, Image.SCALE_SMOOTH);           
        }
        
        ImageIcon icon = new ImageIcon(imgSmaller);
                      
        imgLabel.setIcon(icon);
        imgLabel.setText("");        
        label1.setText(System.getProperty("java.io.tmpdir"));
    }
    
    private static void back(String what)
    {
        File tmp = tempFileBack;
        switch(what)
        {
            case "back": 
            {
                forward.setEnabled(true); 
                back.setEnabled(false); 
                
                createTempFile("forward");
                try
                {
                    img = ImageIO.read(tmp);
                    showImage();
                }
                catch(IOException e){JOptionPane.showMessageDialog(frame, "Soubor nemohl být načten");}
                
            }           
                break;
            case "forward": 
            {   
                forward.setEnabled(false); 
                back.setEnabled(true); 
                createTempFile("back");
                try
                {
                    img = ImageIO.read(tmp);
                    showImage();
                }
                catch(IOException e){JOptionPane.showMessageDialog(frame, "Soubor nemohl být načten");}            
            }
                break;
            default:
                break;
        }
        
    }
       
    private static void chooseAFile(Component c, boolean b)
    {
        int returnVal;
        JFileChooser chooser = new JFileChooser();
        if (b == true)
        {
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);       
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Obrázky","jpg","jpeg", "png","gif");
            chooser.addChoosableFileFilter(extensionFilter);
            returnVal = chooser.showOpenDialog(c);
        }
        else
        {
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            FileNameExtensionFilter jpg = new FileNameExtensionFilter("jpg","jpg");
            FileNameExtensionFilter png = new FileNameExtensionFilter("png","png");
            FileNameExtensionFilter gif = new FileNameExtensionFilter("gif","gif");
            chooser.addChoosableFileFilter(jpg);
            chooser.addChoosableFileFilter(png);
            chooser.addChoosableFileFilter(gif);
            returnVal = chooser.showSaveDialog(c);
        }
        
        
        if (b == true)
        {
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {               
                label1.setText(chooser.getSelectedFile().getName());
                file = chooser.getSelectedFile();
                try
                {
                    img = ImageIO.read(file);
                    Filters.setW(img.getWidth(frame));
                    Filters.setH(img.getHeight(frame));
                }
                catch(IOException e){label1.setText(e.getMessage());}    
                showImage(); 
                saveFile.setEnabled(true); 
                back.setEnabled(true);
                createTempFile("reset");
            }
        }
        else
        {
            String name;        
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {                      
                name = chooser.getSelectedFile().getPath();            
            }
            else name = "image";        
            try
            {
                label1.setText(chooser.getSelectedFile().getName() + "." + chooser.getFileFilter().getDescription() );
                BufferedImage simg = (BufferedImage)img;
                File outputFile = new File(name + "." + chooser.getFileFilter().getDescription());
                ImageIO.write(simg, chooser.getFileFilter().getDescription(), outputFile);
            }
            catch(IOException e){label1.setText(e.getMessage());}
        }
        
    }
    
    private static void createAndShowGUI()
    {
        
        frame = new JFrame("Obrazový filtr TM 2019");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgLabel = new JLabel("Obrazová plocha");
        label1 = new JLabel("Potřebný text");
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setVerticalAlignment(JLabel.CENTER);
        imgLabel.setSize(800,800);
        frame.setSize(1200,900);
        
        JMenuBar menubar = new JMenuBar();
        
        JMenu menuUpravy = new JMenu("Úpravy");
        
        forward = new JMenuItem("Znovu");
        forward.addActionListener(e -> back("forward"));   
        forward.setEnabled(false);
        back = new JMenuItem("Zpět");
        back.addActionListener(e -> {back("back"); forward.setEnabled(true);});   
        back.setEnabled(false);
        reset = new JMenuItem("Reset");
        reset.addActionListener(e -> {back("reset"); forward.setEnabled(true);});   
        reset.setEnabled(false);
        
        
        
        JMenu menuSoubor = new JMenu("Soubor");
        menubar.add(menuSoubor);
        menubar.add(menuUpravy);
        saveFile = new JMenuItem("Uložit...");
        saveFile.addActionListener(e -> chooseAFile(frame, false));   
        saveFile.setEnabled(false);
        JMenuItem openFile = new JMenuItem("Otevřít...");
        openFile.addActionListener(e -> {chooseAFile(frame,true); });
        menuSoubor.add(openFile);
        menuSoubor.add(saveFile);
        menuUpravy.add(back);
        menuUpravy.add(forward);
        
        
        
        
        frame.setJMenuBar(menubar);
        
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);
        JPanel panel2 = new JPanel(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(800,800);
            };
            };
        panel2.setLayout(new BorderLayout());
        panel2.setBackground(Color.red);
        
        
        panel = new JPanel(new GridLayout(3,0));
        panel.setBounds(50, 100, 300, 700);
        panel.setBackground(Color.blue);
        pane.add(panel);
        pane.add(panel2);
        panel.setSize(400, 800);
        panel2.add(imgLabel, BorderLayout.CENTER); 
       
        
        panel.add(label1);

          
        String[] filters = {
            "Zesvětli",
            "Ztmav",
            "Color -> Greyscale",
            "Emboss",
            "Gaussian blur",
            "Inverze",
            "Rozmazaný pohybem zleva",
            "Rozmazaný pohybem zprava",
            "Detekce hran světlá",
            "Detekce hran tmavá",
            "Zaostři",
            "Sobel"};      
        JList filterList = new JList(filters);
        panel.add(filterList);
        
        JButton filterButton = new JButton("Použij vybraný filtr!");
        panel.add(filterButton);
        filterButton.addActionListener(e -> useFilter(filterList.getSelectedValue().toString()));
        
        
        
        frame.setVisible(true);
        
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       javax.swing.SwingUtilities.invokeLater(() ->  createAndShowGUI()); 
    }    
}
