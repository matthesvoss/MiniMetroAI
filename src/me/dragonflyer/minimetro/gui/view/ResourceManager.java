package me.dragonflyer.minimetro.gui.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    private Map<String, BufferedImage> images = new HashMap<>();
    private final String[] imageNames = new String[] {"circle", "cross", "diamond", "drop",
            "leaf", "pentagon", "rhombus", "square", "star", "stationselection", "triangle" };

    public void loadImages() {
        for (String imageName : imageNames) {
            String imageLocation = "/" + imageName + ".png";
            images.put(imageName, loadImage(imageLocation));
        }
    }

    private BufferedImage loadImage(String imageLocation) {
        try {
            URL imageUrl = getClass().getResource(imageLocation);
            assert imageUrl != null : "Image not found: " + imageLocation;
            return ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage getImage(String imageName) {
        return images.get(imageName);
    }
}
