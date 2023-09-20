package src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;

public class ImageManager {

    private BufferedImage image;
    private BufferedImage originalImage;
    private String selectedImagePath; // Variável para armazenar o caminho do arquivo selecionado
    private double[][][] hsbValues; 

    public ImageManager() {
        image = null;
        originalImage = null;
        selectedImagePath = null;
    }

    public BufferedImage getImage() {
        return image;
    }


    public void convertRGBToHSB() {
        hsbValues = new double[image.getWidth()][image.getHeight()][3];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                double[] hsb = RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue());
                hsbValues[x][y][0] = hsb[0];
                hsbValues[x][y][1] = hsb[1];
                hsbValues[x][y][2] = hsb[2];

                // Scale HSB values to the [0.0, 1.0] range
                float h = (float) hsb[0] / 360.0f; // H component is scaled by 360
                float s = (float) hsb[1];
                float b = (float) hsb[2];
    
                // Create a new Color using the scaled HSB values
                image.setRGB( x, y, new Color(h, s, b).getRGB());
            }
        }
        System.out.println("Conversão RGB para HSB realizada com sucesso!");
    }
    

    public static double[] RGBtoHSB(int red, int green, int blue) {
        // Normalize red, green, and blue values
        double r = ((double) red / 255.0);
        double g = ((double) green / 255.0);
        double b = ((double) blue / 255.0);
    
        // Conversion start
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
    
        double h = 0.0;
        if (max == r && g >= b) {
            h = 60 * (g - b) / (max - min);
        } else if (max == r && g < b) {
            h = 60 * (g - b) / (max - min) + 360;
        } else if (max == g) {
            h = 60 * (b - r) / (max - min) + 120;
        } else if (max == b) {
            h = 60 * (r - g) / (max - min) + 240;
        }
    
        double s = (max == 0) ? 0.0 : (1.0 - (min / max));
    
        return new double[] { (double) h, (double) s, (double) (max) };
    }
    

    public void convertHSBToRGB() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                int Pixel [] = HSBtoRGB(hsbValues[x][y][0] , hsbValues[x][y][1],hsbValues[x][y][2]);
                Color pixelColor = new Color(Pixel[0],Pixel[1],Pixel[2]);
                image.setRGB( x, y, pixelColor.getRGB());
            }
        }
    }

    public static int [] HSBtoRGB(double h, double s, double b) {
        double r = 0;
        double g = 0;
        double blue = 0;
    
        if (s == 0) {
            r = g = blue = b;
        } else {
            // The color wheel consists of 6 sectors. Figure out which sector you're in.
            double sectorPos = h / 60.0;
            int sectorNumber = (int) Math.floor(sectorPos);
            // Get the fractional part of the sector
            double fractionalSector = sectorPos - sectorNumber;
    
            // Calculate values for the three axes of the color.
            double p = b * (1.0 - s);
            double q = b * (1.0 - (s * fractionalSector));
            double t = b * (1.0 - (s * (1 - fractionalSector)));
    
            // Assign the fractional colors to r, g, and blue based on the sector
            // the angle is in.
            switch (sectorNumber) {
                case 0:
                    r = b;
                    g = t;
                    blue = p;
                    break;
                case 1:
                    r = q;
                    g = b;
                    blue = p;
                    break;
                case 2:
                    r = p;
                    g = b;
                    blue = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    blue = b;
                    break;
                case 4:
                    r = t;
                    g = p;
                    blue = b;
                    break;
                case 5:
                    r = b;
                    g = p;
                    blue = q;
                    break;
            }
        }

        return new int[] { (int) Math.round(r * 255.0), (int) Math.round(g * 255.0), (int) Math.round(blue * 255.0) };
    }
    

    public void changeHue(double hueDelta) {
        convertRGBToHSB();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                hsbValues[x][y][0] += hueDelta;
                
                // Verifique se o valor de matiz está fora do intervalo 0-360 e ajuste, se necessário
                if (hsbValues[x][y][0] > 360) {
                    hsbValues[x][y][0] = 360;
                } else if (hsbValues[x][y][0] < 0) {
                    hsbValues[x][y][0] = 0;
                }
            }
        }
        convertHSBToRGB();
    }
    
    public void changeSaturation(double saturationDelta) {
        convertRGBToHSB();
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // Obtenha a saturação original
                double originalSaturation = hsbValues[x][y][1];
                
                // Verifique se o pixel não está em tons de cinza (saturação > 0)
                if (originalSaturation > 0) {
                    // Ajuste a saturação proporcionalmente, limitando-a entre 0 e 1
                    double newSaturation = Math.max(0, Math.min(1, originalSaturation + saturationDelta));
                    
                    // Atribua o novo valor de saturação
                    hsbValues[x][y][1] = newSaturation;
                }
            }
        }
        convertHSBToRGB();
    }
    

    public void applyNegativeRGB() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                int newRed = 255 - pixelColor.getRed();
                int newGreen = 255 - pixelColor.getGreen();
                int newBlue = 255 - pixelColor.getBlue();
                Color newColor = new Color(newRed, newGreen, newBlue);
                image.setRGB(x, y, newColor.getRGB());
            }
        }
    }

    public void applyNegativeHSVBandV() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                float[] hsb = Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), null);
                hsb[2] = 1.0f - hsb[2]; // Invert the brightness (V) component
                int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                image.setRGB(x, y, newRGB);
            }
        }
    }

    public void applyBoxFilter(int m, int n, int stride) {
        BufferedImage resultImage = deepCopy(originalImage);
        for (int x = 0; x < image.getWidth() - m + 1; x += stride) {
            for (int y = 0; y < image.getHeight() - n + 1; y += stride) {
                int sumRed = 0, sumGreen = 0, sumBlue = 0;
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        Color pixelColor = new Color(image.getRGB(x + i, y + j));
                        sumRed += pixelColor.getRed();
                        sumGreen += pixelColor.getGreen();
                        sumBlue += pixelColor.getBlue();
                    }
                }
                int averageRed = sumRed / (m * n);
                int averageGreen = sumGreen / (m * n);
                int averageBlue = sumBlue / (m * n);
                Color newColor = new Color(averageRed, averageGreen, averageBlue);
                resultImage.setRGB(x + m / 2, y + n / 2, newColor.getRGB());
            }
        }
        image = resultImage;
    }

    public void applySobelFilter() {
        // Implement Sobel filter here
    }

    public void loadFilterFromFile(String filterFilePath) {
        // Load filter from file and apply it
    }

    public void compareBoxFilters() {
        // Compare Box15x1(Box1x15(image)) with Box15x15(image)
    }

    public void revertToOriginalImage() {
        image = deepCopy(originalImage);
    }


    
    public void displayImage() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Image Viewer");

        ImageIcon imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel(imageIcon);

        frame.setLayout(new FlowLayout());
        frame.add(jLabel);

        frame.pack();
        frame.setVisible(true);
    }

    // Deep copy a BufferedImage
    private BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        copy.getGraphics().drawImage(bi, 0, 0, null);
        return copy;
    }


        class MyFileChooser extends JFileChooser {
        protected JDialog createDialog(Component parent) throws HeadlessException {
            JDialog dialog = super.createDialog(parent);
            dialog.setAlwaysOnTop(true);
            return dialog;
        }
    }

    public void OpenImage() throws IOException {
        // Create a new file chooser
        MyFileChooser fileChooser = new MyFileChooser();

        // Set the file filter to only allow image files
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(fileChooser);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Store the selected file path
            try {
                image = ImageIO.read(selectedFile);
                originalImage = deepCopy(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void saveImage() throws IOException {
        if (selectedImagePath != null) {
            MyFileChooser fileChooser = new MyFileChooser();
            fileChooser.setSelectedFile(new File(selectedImagePath));
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();
                try {
                    ImageIO.write(image, "png", outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
