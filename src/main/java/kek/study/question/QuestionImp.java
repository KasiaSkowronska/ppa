package kek.study.question;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.GridPane;
import kek.study.question.event.QuestionAnsweredEvent;
import kek.study.question.event.QuestionAnsweredEventListener;


import javafx.scene.Node;

public abstract class QuestionImp implements IQuestion {
	
	protected Node renderedQuestion;
	protected String id;
	protected List<QuestionAnsweredEventListener> listeners;

	public QuestionImp(Node renderedQuestion, String id) {
		this.id = id;
		this.renderedQuestion = renderedQuestion;
		listeners = new ArrayList<>();
		this.renderedQuestion.autosize();
	}

	public void terminate() {
		// by default: do nothing
	}

	@Override
	public void addQuestionAnsweredListener(QuestionAnsweredEventListener listener) {
		listeners.add(listener);

	}

	@Override
	public void removeQuestionAnsweredListener(QuestionAnsweredEventListener listener) {
		listeners.remove(listener);

	}

	@Override
	public Node getRenderedQuestion() {
		return renderedQuestion;
	}

	@Override
	public String getId() {
		return id;
	}

	public abstract Answer getAnswer();
	
	public void fireEvent() throws FileNotFoundException {
		QuestionAnsweredEvent event = new QuestionAnsweredEvent(this, getAnswer());
		for (QuestionAnsweredEventListener listener : listeners) {
			listener.handleEvent(event);
		}

	}


    public void setRenderedQuestion(Node renderedQuestion) {
        this.renderedQuestion = renderedQuestion;
    }
}