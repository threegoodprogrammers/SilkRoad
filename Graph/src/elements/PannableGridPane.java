package elements;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class PannableGridPane extends GridPane {
    private DoubleProperty scale = new SimpleDoubleProperty(3);

    public PannableGridPane() {
//        setPrefSize(0, 0);

        // add scale transform
        scaleXProperty().bind(scale);
        scaleYProperty().bind(scale);
    }

    public double getScale() {
        return scale.get();
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }
}
