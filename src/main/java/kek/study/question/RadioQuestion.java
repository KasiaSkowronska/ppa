package kek.study.question;


import javafx.scene.control.Toggle;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;

public class RadioQuestion extends QuestionImp {
	
	protected ToggleGroup group;

	public RadioQuestion(Node renderedQuestion, String id, ToggleGroup group) {
		super(renderedQuestion, id);
		this.group = group;
	}


	@Override
	public Answer getAnswer() {
		Toggle data = group.getSelectedToggle();
		Answer answer = new Answer(data == null ? null : (String) data.getUserData());
		return answer;
	}


}
