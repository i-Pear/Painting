package Layers;

import com.sun.javafx.geom.Ellipse2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Implement for circle shape layer
 */
public class Layer_Circle extends Layer {

    Point2D leftUpper, rightBottom;

    Layer_Circle(double x0, double y0, double x1, double y1) {
        layerType = LayerType.CIRCLE;

        double d = Math.min(x1 - x0, y1 - y0);
        leftUpper = new Point2D(x0, y0);
        rightBottom = new Point2D(x0 + d, y0 + d);
    }

    public Layer_Circle(PointGroup pointGroup) {
        this(pointGroup.p0.getX(),pointGroup.p0.getY(),pointGroup.p1.getX(),pointGroup.p1.getY());
    }

    @Override
    public void draw(GraphicsContext graphics) {
        setGraphicsMode(graphics);
        switch (fillType) {
            case NO:
                graphics.strokeOval(leftUpper.getX() + x_shifting, leftUpper.getY() + y_shifting, rightBottom.getX() - leftUpper.getX(), rightBottom.getY() - leftUpper.getY());
            case FILL:
                graphics.strokeOval(leftUpper.getX() + x_shifting, leftUpper.getY() + y_shifting, rightBottom.getX() - leftUpper.getX(), rightBottom.getY() - leftUpper.getY());
                graphics.fillOval(leftUpper.getX() + x_shifting, leftUpper.getY() + y_shifting, rightBottom.getX() - leftUpper.getX(), rightBottom.getY() - leftUpper.getY());
        }
    }

    @Override
    public boolean isInner(float x, float y) {
        Ellipse2D ellipse_outer = new Ellipse2D((float) leftUpper.getX() - width, (float) leftUpper.getY() - width,
                (float) rightBottom.getX() - (float) leftUpper.getX() + 2 * width, (float) rightBottom.getY() - (float) leftUpper.getY() + 2 * width);
        Ellipse2D ellipse_inner = new Ellipse2D((float) leftUpper.getX() + width, (float) leftUpper.getY() + width,
                (float) rightBottom.getX() - (float) leftUpper.getX() - 2 * width, (float) rightBottom.getY() - (float) leftUpper.getY() - 2 * width);
        if (fillType == FillType.FILL) {
            return ellipse_outer.contains(x, y);
        } else {
            return ellipse_outer.contains(x, y) && (!ellipse_inner.contains(x, y));
        }
    }

    @Override
    public void applyShifting() {
        leftUpper = leftUpper.add(x_shifting, y_shifting);
        rightBottom = rightBottom.add(x_shifting, y_shifting);
        x_shifting = y_shifting = 0;
    }

    @Override
    public Layer getClone() {
        Layer_Circle new_layer = new Layer_Circle(leftUpper.getX(), leftUpper.getY(), rightBottom.getX(), rightBottom.getY());
        super.setClone(new_layer);
        return new_layer;
    }

}
