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

    private Map<String, BufferedImage> images = new HashMap<>();

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }

    public void loadImages() {
        for (Station.Type stationType : Station.Type.values()) {
            String imageLocation = "/" + stationType.getName() + ".png";
            images.put(stationType.getName(), loadImage(imageLocation));
        }
        images.put("stationselection", loadImage("/stationselection.png"));
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

    public Image getImage(String imageName) {
        return images.get(imageName);
    }

}
