/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctak;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import static zapoctak.Zapoctak.*;

/**
 *
 * @author Tereza Miklóšová
 */
public class Filters {
    
    private static int w;
    private static int h;
    
    public static void setW(int width)
    {
        w = width;
    }
    public static void setH(int height)
    {
        h = height;
    }
    
    private static int flatten(int i)
    {
        if(i > 255){return 255;}
        else if(i < 0){return 0;}
        else    {return i;}
    }
    
    public static void gaussianBlur()
    {
        BufferedImage before = (BufferedImage)getImage();
        BufferedImage after;
        float[] matrix = {1/16f,1/8f,1/16f,1/8f,1/4f,1/8f,1/16f,1/8f,1/16f};
        BufferedImageOp BIO = new ConvolveOp(new Kernel(3,3,matrix));
        after = BIO.filter(before, null);
        setImage(after);                
    }
    
    public static void ztmav(int HowMuch)
    {               
        BufferedImage bimg=(BufferedImage)getImage();
        
        Color c;
        for(int i = 0; i < w;i++)
        {
            for (int j = 0; j < h; j++) 
            {
                c = new Color(bimg.getRGB(i, j));              
                int p;
                int red     = c.getRed()    - HowMuch;
                int green   = c.getGreen()  - HowMuch;
                int blue    = c.getBlue()   - HowMuch;               
                p = (0<<24) | (flatten(red)<<16) | (flatten(green)<<8) | flatten(blue);
                
                bimg.setRGB(i,j,p);
            }
        }
        setImage(bimg);
        
    } 
    public static void toGrayscale()
    {       
        BufferedImage bimg=(BufferedImage)getImage();
        
        Color c;
        for(int i = 0; i < w;i++)
        {
            for (int j = 0; j < h; j++) 
            {
                c = new Color(bimg.getRGB(i, j));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();
                
                int p;
                int avgRGB = (red +green + blue)/3;
               
                p = (0<<24) | (flatten(avgRGB)<<16) | (flatten(avgRGB)<<8) | flatten(avgRGB);
                
                bimg.setRGB(i,j,p);
            }
        }
        setImage(bimg);        
    }
    
    public static void zesvetli()
    {
        BufferedImage bimg=(BufferedImage)getImage();
        
        Color c;
        for(int i = 0; i < w;i++)
        {
            for (int j = 0; j < h; j++) 
            {
                c = new Color(bimg.getRGB(i, j));
                int p;
                int red = c.getRed() + 10;
                int green = c.getGreen() + 10;
                int blue = c.getBlue() + 10;
                p = (0<<24) | (flatten(red)<<16) | (flatten(green)<<8) | flatten(blue);
                
                bimg.setRGB(i,j,p);
            }
        }
        setImage(bimg);                
    }
    
    public static void invert()
    {
        BufferedImage bimg=(BufferedImage)getImage();
        
        Color c;
        for(int i = 0; i < w;i++)
        {
            for (int j = 0; j < h; j++) 
            {
                c = new Color(bimg.getRGB(i, j));
                int red     = 255   - c.getRed();
                int green   = 255   - c.getGreen();
                int blue    = 255   - c.getBlue();

                int p;
               
                p = (0<<24) | (flatten(red)<<16) | (flatten(green)<<8) | flatten(blue);
                
                bimg.setRGB(i,j,p);
            }
        }
        setImage(bimg);       
    }
    
    public static void motionBlur()
    {
        BufferedImage before = (BufferedImage)getImage();
        BufferedImage after;
        
        Kernel kernel = new Kernel(9,9, new float[]{
            1/9f,0f,0f,0f,0f,0f,0f,0f,0f,
            0f,1/9f,0f,0f,0f,0f,0f,0f,0f,
            0f,0f,1/9f,0f,0f,0f,0f,0f,0f,
            0f,0f,0f,1/9f,0f,0f,0f,0f,0f,
            0f,0f,0f,0f,1/9f,0f,0f,0f,0f,
            0f,0f,0f,0f,0f,1/9f,0f,0f,0f,
            0f,0f,0f,0f,0f,0f,1/9f,0f,0f,
            0f,0f,0f,0f,0f,0f,0f,1/9f,0f,
            0f,0f,0f,0f,0f,0f,0f,0f,1/9f});
        
        BufferedImageOp BIO = new ConvolveOp(kernel);
        after = BIO.filter(before, null);
        setImage(after);
    }
}
