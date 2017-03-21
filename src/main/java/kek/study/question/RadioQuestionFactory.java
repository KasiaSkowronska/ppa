package kek.study.question;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class RadioQuestionFactory extends QuestionFactory {

    @Override
    public QuestionImp createQuestion(List<String> lines, String questionId, String questionType,
                                      String questionExtrasType, String questionExtrasFile) throws IOException {
        VBox questionBox = new VBox();
        String questionContent = lines.get(1);
        questionBox.getChildren().add(new Label(questionContent));
        ToggleGroup group = new ToggleGroup();
        for (int i = 2; i < lines.size(); i+=2) {
            String answer = lines.get(i);
            String answerCode = lines.get(i+1);
            RadioButton button = new RadioButton(answer);
            button.setUserData(answerCode);
            button.setToggleGroup(group);
            questionBox.getChildren().add(button);
        }

        RadioQuestion question = createSpecificQuestion(questionExtrasType, questionExtrasFile); //
        question = addExtras(question, ... , ...);

        Button finishButton = new Button("Submit");
        finishButton.setOnAction((event) -> {
            try {
                question.fireEvent();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        questionBox.getChildren().add(finishButton);
        questionBox.onContextMenuRequestedProperty();
        return question;
    }

    private RadioQuestion createSpecificQuestion(String questionExtrasType, String questionExtrasFile) {
        QuestionImp question = null;
        if (questionExtrasType == null) {
            question = new SimplyRadioQuestion();
        }
        if (questionExtrasType.equals("music")) {
//            question = new MusicRadioQuestion(mediaPlayer);
        }
        if (questionExtrasType.equals("image")) {
            question = new ImageRadioQuestion();
        }
        question = addExtras(question, questionExtrasType, questionExtrasFile);
        return question;
    }

}