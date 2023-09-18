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

    public ImageManager() {
        image = null;
        originalImage = null;
        selectedImagePath = null;
    }

    public BufferedImage getImage() {
        return image;
    }


    public void convertRGBToHSB() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                float[] hsb = Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), null);
                int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                image.setRGB(x, y, newRGB);
            }
        }
    }

    public void convertHSBToRGB() {
        image = deepCopy(originalImage);
    }

    public void changeHue(float hueDelta) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                float[] hsb = Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), null);
                hsb[0] += hueDelta;
                int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                image.setRGB(x, y, newRGB);
            }
        }
    }

    public void changeSaturation(float saturationDelta) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                float[] hsb = Color.RGBtoHSB(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), null);
                hsb[1] += saturationDelta;
                int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                image.setRGB(x, y, newRGB);
            }
        }
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
