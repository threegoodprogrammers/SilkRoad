package elements;

import com.jfoenix.controls.JFXButton;
import graph.App;

public class OperationOption extends JFXButton {
    private App.Operation operation;

//    public OperationOption(App.Operation operation) {
//        this.operation = operation;
//    }

    public App.Operation getOperation() {
        return operation;
    }

    public void setOperation(App.Operation operation) {
        this.operation = operation;
    }
}
