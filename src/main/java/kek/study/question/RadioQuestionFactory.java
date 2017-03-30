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
        String questionContent = lines.get(0);
        questionBox.getChildren().add(new Label(questionContent));
        ToggleGroup group = new ToggleGroup();
        for (int i = 1; i < lines.size(); i+=2) {
        	// TO DO: handle wrong number of lines - if line(i+1) is empty
            String answer = lines.get(i);
            String answerCode = lines.get(i+1);
            RadioButton button = new RadioButton(answer);
            button.setUserData(answerCode);
            button.setToggleGroup(group);
            questionBox.getChildren().add(button);
        }

        RadioQuestion question = new RadioQuestion(questionBox, questionId, group);
        
        if (questionExtrasType != null){
        addExtras(question, questionExtrasType, questionExtrasFile);}

        Button finishButton = new Button("Submit");
        finishButton.setOnAction((event) -> {
//        	System.out.println(question.listeners.size());
            try {
                question.fireEvent();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        questionBox.getChildren().add(finishButton);
        questionBox.onContextMenuRequestedProperty();
        return question;
    }


}