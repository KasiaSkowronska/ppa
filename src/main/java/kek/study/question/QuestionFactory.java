package kek.study.question;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import kek.study.ResourcesLoader;

import javax.xml.soap.Node;
import java.io.File;

/**
 * Created by kosss on 21.03.2017.
 */
public abstract class QuestionFactory implements IQuestionFactory {

    protected QuestionImp addExtras(QuestionImp question, String extrasType, String extrasFile) {
        // this function will modify question and extras to them according to our old methods.
        if (extrasType.equals("music")){

        }
        if (extrasType.equals("image")){
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            final ImageView imv = new ImageView();
            imv.setPreserveRatio(true);
            imv.setFitHeight(150);
            imv.setFitWidth(150);

            ResourcesLoader loader;
            loader = new ResourcesLoader("image");

            File f = loader.loadFile(extrasFile);
            System.out.println(f.toURI().toString());
            Image image = new Image(f.toURI().toString());
            imv.setImage(image);

            final HBox pictureRegion = new HBox();
            pictureRegion.getChildren().add(imv);
            gridPane.add(pictureRegion, 1, 1);

            gridPane.add(question.getRenderedQuestion(), 3, 1);
            question.setRenderedQuestion(gridPane); // it's weird, there is a need to change it.

        }

        return question;
    }
}
