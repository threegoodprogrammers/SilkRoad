package elements;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * The type Transition manager
 */

public class TransitionManager {
    /**
     * Gets fade transition
     *
     * @param duration the duration
     * @param node     the node
     * @return the fade transition
     */

    public static FadeTransition getFadeTransition(Duration duration, Node node, Double toValue) {
        FadeTransition transition = new FadeTransition(duration, node);
        transition.setToValue(toValue);
        return transition;
    }
}
