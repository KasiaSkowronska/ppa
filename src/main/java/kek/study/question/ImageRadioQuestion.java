package kek.study.question;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kek.study.ResourcesLoader;

import java.io.File;

public class ImageRadioQuestion extends RadioQuestion {

    public ImageRadioQuestion(Node renderedQuestion, String id, ToggleGroup group) {
        super(renderedQuestion, id, group);
    }

}