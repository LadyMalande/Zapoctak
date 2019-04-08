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
    static File file, tempFileBack;
    static JMenuItem back, forward;
    
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
    private static void gaussianBlur()
    {
        BufferedImage before = (BufferedImage)img;
        BufferedImage after;
        float[] matrix = {1/16f,1/8f,1/16f,1/8f,1/4f,1/8f,1/16f,1/8f,1/16f};
        BufferedImageOp BIO = new ConvolveOp(new Kernel(3,3,matrix));
        after = BIO.filter(before, null);
        img = after;
        showImage();
        
    }
    private static void emboss()
    {
        
        int w = img.getWidth(frame);
        int h = img.getHeight(frame);
        
        BufferedImage bimg=(BufferedImage)img;
        BufferedImage simg= (BufferedImage)img;

        float[][] matrix =
{
    {0,  0, -1,  0,  0},
   {0,  0, -1,  0,  0},
   {0,  0,  2,  0,  0},
   {0,  0,  0,  0,  0},
   {0,  0,  0,  0,  0},
};
label1.setText(Integer.toString(matrix.length));
 /*       float[][] matrix = {
    {1/9f,0f,0f,0f,0f,0f,0f,0f,0f},
    {0f,1/9f,0f,0f,0f,0f,0f,0f,0f},
            {0f,0f,1/9f,0f,0f,0f,0f,0f,0f},
            {0f,0f,0f,1/9f,0f,0f,0f,0f,0f},
            {0f,0f,0f,0f,1/9f,0f,0f,0f,0f},
            {0f,0f,0f,0f,0f,1/9f,0f,0f,0f},
            {0f,0f,0f,0f,0f,0f,1/9f,0f,0f},
            {0f,0f,0f,0f,0f,0f,0f,1/9f,0f},
            {0f,0f,0f,0f,0f,0f,0f,0f,1/9f}};   
*/ 
/*
float[][] matrix =
{
  {1,  4,  6,  4,  1},
  {4, 16, 24, 16,  4},
  {6, 24, 36, 24,  6},
  {4, 16, 24, 16,  4},
  {1,  4,  6,  4,  1},
};
 */
//float[][] matrix = {{0,0.2f,0},{0.2f,0.2f,0.2f},{0,0.2f,0}};
        //float[][] matrix = {{-1,-1,0},{-1,0,1},{0,1,1}};
        float multiplier = 1.0f;
        double colorShift = 0.0;
        
        Color c0,c1;
        for(int i = 0; i < w;i++)
        {
            for (int j = 0; j < h; j++) 
            {
                double red = 0.0, green = 0.0, blue = 0.0;
                for (int fx = 0; fx < matrix.length; fx++) {
                    
                    for (int fy = 0; fy < matrix.length; fy++) {
                        int X = abs(i - matrix.length / 2 + fx + w) % w;
                        int Y = abs(j - matrix.length / 2 + fy + h) % h;
                        c0      = new Color(bimg.getRGB(X, Y));
                        
                        red     += c0.getRed() * matrix[fx][fy];
                        green   += c0.getGreen() * matrix[fx][fy];
                        blue    += c0.getBlue() * matrix[fx][fy];
                        

                        
                    }
                }
                int a = 0;
                        int p;
                        
                        int resred = Integer.min(Integer.max((int)(multiplier * red + colorShift), 0),255);
                        int resgreen = Integer.min(Integer.max((int)(multiplier * green + colorShift), 0),255);
                        int resblue = Integer.min(Integer.max((int)(multiplier * blue + colorShift), 0),255);
                        p = (a<<24) | (resred<<16) | (resgreen<<8) | resblue;
                simg.setRGB(i,j,p);
            }
        }
        
        BufferedImage before = (BufferedImage)img;
        BufferedImage after;
        
        
        
        
        img = simg;
        
        showImage();
        
    }
    /*
    Kernel kernel = new Kernel(3,3, new float[]{
            0,-1,0,
            -1,4,-1,
            0,-1,0}
        );
    */

    
 
       
    private static void useFilter(String filter)
    {
        createTempFile("back");
        back.setEnabled(true);
        switch(filter)
        {
            case "Zesvětli" : { panel.setBackground(Color.red); Filters.zesvetli();panel.setBackground(Color.green);}
            break; 
            case "Ztmav"    : {panel.setBackground(Color.red);Filters.ztmav(20); panel.setBackground(Color.green);} 
            break;
            case "Color -> Greyscale": { panel.setBackground(Color.red);Filters.toGrayscale();panel.setBackground(Color.green);}
            break;
            case "Emboss": {panel.setBackground(Color.red); emboss();panel.setBackground(Color.green);}
            break;
            case "Gaussian blur": { Filters.gaussianBlur();}
            break;
            case "Inverze": { Filters.invert();}
            break;
            case "Rozmazaný pohybem": { Filters.motionBlur();}
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
                try{
                img = ImageIO.read(tmp);
                showImage();
                }
                catch(IOException e){JOptionPane.showMessageDialog(frame, "Soubor nemohl být načten");}
                
            }
            
                break;
            case "forward": {forward.setEnabled(false); 
                back.setEnabled(true); 
                createTempFile("back");
                try{
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
        
        JFileChooser chooser = new JFileChooser();
        if (b == true)
        {
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);       
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Obrázky","jpg","jpeg", "png","gif");
            chooser.addChoosableFileFilter(extensionFilter);
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
        }
        
        int returnVal = chooser.showOpenDialog(c);
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
        frame.setSize(1200,850);
        
        JMenuBar menubar = new JMenuBar();
        
        JMenu menuUpravy = new JMenu("Úpravy");
        
        forward = new JMenuItem("Znovu");
        forward.addActionListener(e -> back("forward"));   
        forward.setEnabled(false);
        back = new JMenuItem("Zpět");
        back.addActionListener(e -> {back("back"); forward.setEnabled(true);});   
        back.setEnabled(false);
        
        
        
        JMenu menuSoubor = new JMenu("Soubor");
        menubar.add(menuSoubor);
        menubar.add(menuUpravy);
        JMenuItem saveFile = new JMenuItem("Uložit...");
        saveFile.addActionListener(e -> chooseAFile(frame, false));   
        saveFile.setEnabled(false);
        JMenuItem openFile = new JMenuItem("Otevřít...");
        openFile.addActionListener(e -> {chooseAFile(frame,true); showImage(); saveFile.setEnabled(true); back.setEnabled(true);});
        menuSoubor.add(openFile);
        menuSoubor.add(saveFile);
        menuUpravy.add(back);
        menuUpravy.add(forward);
        
        
        
        
        frame.setJMenuBar(menubar);
        
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setVerticalAlignment(JLabel.CENTER);
        JButton uploadButton = new JButton("Načti obrázek");              
        uploadButton.addActionListener(e -> {chooseAFile(frame,true); showImage();});
        JButton saveButton = new JButton("Ulož obrázek");               
        saveButton.addActionListener(e -> {chooseAFile(frame, false);});
        JPanel panel2 = new JPanel(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(800,800);
            };
            };
        panel2.setLayout(new BorderLayout());
        panel2.setBackground(Color.red);
        
        
        panel = new JPanel(new GridLayout(5,0));
        panel.setBounds(50, 100, 300, 700);
        panel.setBackground(Color.blue);
        pane.add(panel);
        pane.add(panel2);
        panel.setSize(400, 800);
        panel2.add(imgLabel, BorderLayout.CENTER); 
       
        
        panel.add(label1);
        panel.add(uploadButton);
        panel.add(saveButton);
          
        String[] filters = {"Zesvětli", "Ztmav", "Color -> Greyscale", "Emboss", "Gaussian blur", "Inverze", "Rozmazaný pohybem"};      
        JList filterList = new JList(filters);
        panel.add(filterList);
        
        JButton filterButton = new JButton("Použij vybraný filtr!");
        panel.add(filterButton);
        filterButton.addActionListener(e -> useFilter(filterList.getSelectedValue().toString()));
        
        
        //frame.pack();
        frame.setVisible(true);
        
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       javax.swing.SwingUtilities.invokeLater(() ->  createAndShowGUI()); 
    }    
}
