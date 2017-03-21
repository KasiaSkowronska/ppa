package kek.study.question.event;


import java.io.FileNotFoundException;

public interface QuestionAnsweredEventNotifier {
	
	public void addQuestionAnsweredListener(QuestionAnsweredEventListener listener);
	public void removeQuestionAnsweredListener(QuestionAnsweredEventListener listener);
	
	public void fireEvent() throws FileNotFoundException;
}
