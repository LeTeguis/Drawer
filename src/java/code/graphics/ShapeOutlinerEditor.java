package graphics;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ShapeOutlinerEditor {
    BooleanProperty visible;
    BooleanProperty locked;
    StringProperty name;
    AShape shape;
    int index;

    public ShapeOutlinerEditor(AShape shape, int index){
        this.shape = shape;
        this.index = index;

        visible = new SimpleBooleanProperty(this, "Visible", shape.IsEnable());
        visible.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            }
        });

        locked = new SimpleBooleanProperty(this, "Locked", false);
        locked.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            }
        });

        name = new SimpleStringProperty(this, "Name", shape.GetName());
        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                shape.SetName(newValue);
            }
        });
    }

    public BooleanProperty IsVisible(){
        return visible;
    }
    public BooleanProperty IsLocked(){
        return locked;
    }
    public StringProperty GetName(){
        return name;
    }

    public int GetIndex(){
        return index;
    }

    public AShape GetShape() {
        return shape;
    }

    public void DecrementIndex() {
        index--;
    }
}
