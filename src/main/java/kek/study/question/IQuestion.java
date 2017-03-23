package kek.study.question;

import kek.study.question.event.QuestionAnsweredEventNotifier;

import javafx.scene.Node;

public interface IQuestion extends QuestionAnsweredEventNotifier {

	public Node getRenderedQuestion();
	public String getId();
	public Answer getAnswer();
	public void init();
	public void terminate(); // by default do nothing. Will be overwritten in subclasses

}
