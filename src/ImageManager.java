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
    private Filter filter;

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

                //Escalona os valores HSB para o intervalo [0.0, 1.0]
                float h = (float) hsb[0] / 360f;
                float s = (float) hsb[1];
                float b = (float) hsb[2];
    
                //Cria uma nova cor usando os valores HSB escalonados

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
        System.out.println(hueDelta);
        convertRGBToHSB();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                hsbValues[x][y][0] += hueDelta;
    
                // Wrap the hue value within the range [0, 360]
                hsbValues[x][y][0] %= 360;
    
                // Ensure it's positive
                if (hsbValues[x][y][0] < 0) {
                    hsbValues[x][y][0] += 360;
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
        convertRGBToHSB();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if(1.0f - hsbValues[x][y][2] > 0)
                    hsbValues[x][y][2] = 1.0f - hsbValues[x][y][2];
                else
                    hsbValues[x][y][2] = 0;
            }
        }
        convertHSBToRGB();
    }

    public void loadFilterFromFile(String filterFilePath) throws IOException {
        filter = new Filter();
        filter.loadFilter(filterFilePath);
    }


    public void correlationFilter() {
        int m = filter.m;
        int n = filter.n;
        int stride = filter.stride;
    
        int resultWidth = (image.getWidth() - m + 1) / stride; // Adjusted to consider stride
        int resultHeight = (image.getHeight() - n + 1) / stride; // Adjusted to consider stride
        BufferedImage result = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);
    
        for (int i = 0; i < resultHeight; i++) {
            for (int j = 0; j < resultWidth; j++) {
                float sumRed = 0, sumGreen = 0, sumBlue = 0;
                for (int x = 0; x < m; x++) {
                    for (int y = 0; y < n; y++) {
                        int pixelX = j * stride + x; // Calculate the pixel position in the original image
                        int pixelY = i * stride + y; // Calculate the pixel position in the original image
                        
                        if (pixelX >= 0 && pixelX < image.getWidth() && pixelY >= 0 && pixelY < image.getHeight()) {
                            Color pixelColor = new Color(image.getRGB(pixelX, pixelY));
                            sumRed += pixelColor.getRed() * filter.filter[x][y];
                            sumGreen += pixelColor.getGreen() * filter.filter[x][y];
                            sumBlue += pixelColor.getBlue() * filter.filter[x][y];
                        }
                    }
                }
                int resultRed = (int) Math.min(255, Math.max(0, sumRed)); // Ensure result is within [0, 255]
                int resultGreen = (int) Math.min(255, Math.max(0, sumGreen)); // Ensure result is within [0, 255]
                int resultBlue = (int) Math.min(255, Math.max(0, sumBlue)); // Ensure result is within [0, 255]
                Color resultColor = new Color(resultRed, resultGreen, resultBlue);
                result.setRGB(j, i, resultColor.getRGB());
            }
        }
        image = result;
    }
    

        public void applyBoxFilter(int m, int n, int stride) {
    
        int resultWidth = (image.getWidth() - m + 1) / stride; // Adjusted to consider stride
        int resultHeight = (image.getHeight() - n + 1) / stride; // Adjusted to consider stride
        BufferedImage result = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);
    
        for (int i = 0; i < resultHeight; i++) {
            for (int j = 0; j < resultWidth; j++) {
                int sumRed = 0, sumGreen = 0, sumBlue = 0;
                for (int x = 0; x < m; x++) {
                    for (int y = 0; y < n; y++) {
                        int pixelX = j * stride + x; // Calculate the pixel position in the original image
                        int pixelY = i * stride + y; // Calculate the pixel position in the original image
                        
                        if (pixelX >= 0 && pixelX < image.getWidth() && pixelY >= 0 && pixelY < image.getHeight()) {
                            Color pixelColor = new Color(image.getRGB(pixelX, pixelY));
                            sumRed += pixelColor.getRed();
                            sumGreen += pixelColor.getGreen();
                            sumBlue += pixelColor.getBlue();
                        }
                    }
                }
                int averageRed = sumRed / (m * n);
                int averageGreen = sumGreen / (m * n);
                int averageBlue = sumBlue / (m * n);
                Color newColor = new Color(averageRed, averageGreen, averageBlue);
                result.setRGB(j, i, newColor.getRGB());
            }
        }
        image = result;
    }
    

    public void applySobelFilterWithHistogramExpansion() {
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // Sobel filter kernels
        int[][] sobelX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        };
        
        int[][] sobelY = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
        };
        
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int gxRed = 0;
                int gxGreen = 0;
                int gxBlue = 0;
                int gyRed = 0;
                int gyGreen = 0;
                int gyBlue = 0;
                
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color pixelColor = new Color(image.getRGB(x + i, y + j));
                        int pixelRed = pixelColor.getRed();
                        int pixelGreen = pixelColor.getGreen();
                        int pixelBlue = pixelColor.getBlue();
                        
                        gxRed += sobelX[i + 1][j + 1] * pixelRed;
                        gxGreen += sobelX[i + 1][j + 1] * pixelGreen;
                        gxBlue += sobelX[i + 1][j + 1] * pixelBlue;
                        
                        gyRed += sobelY[i + 1][j + 1] * pixelRed;
                        gyGreen += sobelY[i + 1][j + 1] * pixelGreen;
                        gyBlue += sobelY[i + 1][j + 1] * pixelBlue;
                    }
                }
                
                int gradientMagnitudeRed = (int) Math.sqrt(gxRed * gxRed + gyRed * gyRed);
                int gradientMagnitudeGreen = (int) Math.sqrt(gxGreen * gxGreen + gyGreen * gyGreen);
                int gradientMagnitudeBlue = (int) Math.sqrt(gxBlue * gxBlue + gyBlue * gyBlue);
                
                gradientMagnitudeRed = Math.min(255, Math.max(0, gradientMagnitudeRed));
                gradientMagnitudeGreen = Math.min(255, Math.max(0, gradientMagnitudeGreen));
                gradientMagnitudeBlue = Math.min(255, Math.max(0, gradientMagnitudeBlue));
                
                Color resultColor = new Color(gradientMagnitudeRed, gradientMagnitudeGreen, gradientMagnitudeBlue);
                resultImage.setRGB(x, y, resultColor.getRGB());
            }
        }
        
        // Histogram Expansion
        int[] histogram = new int[256];
        int totalPixels = width * height;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixelColor = new Color(resultImage.getRGB(x, y));
                int pixelValue = pixelColor.getRed(); // Use the red channel for histogram expansion
                histogram[pixelValue]++;
            }
        }
        
        int[] cumulativeHistogram = new int[256];
        cumulativeHistogram[0] = histogram[0];
        
        for (int i = 1; i < 256; i++) {
            cumulativeHistogram[i] = cumulativeHistogram[i - 1] + histogram[i];
        }
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixelColor = new Color(resultImage.getRGB(x, y));
                int pixelValue = pixelColor.getRed(); // Use the red channel for histogram expansion
                int expandedValue = (int) (255.0 * cumulativeHistogram[pixelValue] / totalPixels);
                Color expandedColor = new Color(expandedValue, expandedValue, expandedValue);
                resultImage.setRGB(x, y, expandedColor.getRGB());
            }
        }

        image = resultImage;
    }


    public void compareBoxFilters() {
   // Assuming you have 'image', 'Box15x1', and 'Box15x15' as BufferedImage objects

    // Measure the time taken by Box15x15
    long startTime15x15 = System.currentTimeMillis();
    applyBoxFilter(15, 15, 1);
    long endTime15x15 = System.currentTimeMillis();
    long elapsedTime15x15 = endTime15x15 - startTime15x15;
    
    // Now you have the execution times in 'elapsedTime15x15' and 'elapsedTime15x1Box1x15'
    System.out.println("Filtro Box15x15 levou : " + elapsedTime15x15/1000 + " Segundos");
    displayImage("Box15x15");
    // Reset the image to its original state
    revertToOriginalImage();

    // Measure the time taken by Box15x1(Box1x15(image))
    long startTime15x1Box1x15 = System.currentTimeMillis();
    applyBoxFilter(15,1,1);
    applyBoxFilter(15,1,1);
    long endTime15x1Box1x15 = System.currentTimeMillis();
    long elapsedTime15x1Box1x15 = endTime15x1Box1x15 - startTime15x1Box1x15;

    // You can print or use these times for comparison
    System.out.println("Filtro Box15x1(Box1x15(image)) levou : (Box1x15(image)): " + elapsedTime15x1Box1x15/1000 + " Segundos");
    displayImage("Box15x1(Box1x15(image))");

    }

    public void revertToOriginalImage() {
        image = deepCopy(originalImage);
    }


    
    public void displayImage(String title) {

        // Redimensiona a imagem para 800x600

        // Calcula as novas dimensões com base na proporção
        int maxWidth = 1000;
        int maxHeight = 800;
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (originalWidth > maxWidth || originalHeight > maxHeight) {
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;

            if (widthRatio < heightRatio) {
                newWidth = maxWidth;
                newHeight = (int) (originalHeight * widthRatio);
            } else {
                newWidth = (int) (originalWidth * heightRatio);
                newHeight = maxHeight;
            }
        }

        // Redimensiona a imagem
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(title);

        ImageIcon imageIcon = new ImageIcon(resizedImage);
        JLabel jLabel = new JLabel(imageIcon);
        frame.setAlwaysOnTop(true);

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
