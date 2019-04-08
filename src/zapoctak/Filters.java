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
        int w = getImage().getWidth(frame);
        int h = getImage().getHeight(frame);
        
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
                int a = 0;
                int p;
                red = red - HowMuch;
                green = green - HowMuch;
                blue = blue - HowMuch;
                if(red < 0){red = 0;}
                if(green < 0){green = 0;}
                if(blue < 0){blue = 0;}
                
                p = (a<<24) | (red<<16) | (green<<8) | blue;
                
                bimg.setRGB(i,j,p);
            }
        }
        setImage(bimg);
        
    } 
    
}
