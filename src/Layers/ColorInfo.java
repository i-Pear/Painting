package Layers;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * A self-made color class, because the native Color class is not Serializable
 */
public class ColorInfo implements Serializable {

    double R, G, B;

    public ColorInfo(ColorInfo colorInfo){
        R=colorInfo.R;
        G=colorInfo.G;
        B=colorInfo.B;
    }

    public ColorInfo(Color color){
        R=color.getRed();
        G=color.getGreen();
        B=color.getBlue();
    }

    public Color getColor() {
        return new Color(R, G, B,1);
    }

}
