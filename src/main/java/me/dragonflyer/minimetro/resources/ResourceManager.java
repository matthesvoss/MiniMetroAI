package me.dragonflyer.minimetro.resources;

import me.dragonflyer.minimetro.gui.model.entities.Station;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    private static ResourceManager resourceManager;
    private static final String IMAGE_DIR = "/images/";

    private Map<String, BufferedImage> images = new HashMap<>();

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }

    public void loadImages() {
        try {
            for (Station.Type stationType : Station.Type.values()) {
                String imageLocation = IMAGE_DIR + stationType.getName() + ".png";
                images.put(stationType.getName(), loadImage(imageLocation));
            }
            images.put("stationselection", loadImage(IMAGE_DIR + "stationselection.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private BufferedImage loadImage(String imageLocation) throws IOException {
        URL imageUrl = getClass().getResource(imageLocation);
        if (imageUrl == null) {
            throw new IOException("Image not found: " + imageLocation);
        }
        return ImageIO.read(imageUrl);
    }

    public Image getImage(String imageName) {
        return images.get(imageName);
    }

}
