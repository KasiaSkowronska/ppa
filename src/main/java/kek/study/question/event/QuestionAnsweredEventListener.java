package kek.study.question.event;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public interface QuestionAnsweredEventListener {

	public void handleEvent(QuestionAnsweredEvent event) throws FileNotFoundException;

}
