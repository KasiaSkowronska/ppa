package kek.study.question;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


import kek.study.question.event.QuestionAnsweredEvent;
import kek.study.question.event.QuestionAnsweredEventListener;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public abstract class QuestionImp implements IQuestion {

	protected Node renderedQuestion;
	protected String id;
	protected List<QuestionAnsweredEventListener> listeners;
	protected MediaPlayer mediaPlayer = null; // optional, added by addExtras() in factory


	public QuestionImp(Node renderedQuestion, String id) {
		this.id = id;
		this.renderedQuestion = renderedQuestion;
		listeners = new ArrayList<>();
		this.renderedQuestion.autosize();
	}
	
	
// INITIALIZING & TERMINATING MEDIAS 
	@Override
	public void init(){
		if (mediaPlayer != null){
			System.out.println("tak");
	        mediaPlayer.setMute(false);
	        mediaPlayer.setAutoPlay(true);
	        MediaView mediaView = new MediaView(mediaPlayer);
		}
	}

	@Override
	public void terminate() {
		if (mediaPlayer != null){
			mediaPlayer.setMute(true);
		}
	}

	
// LISTENERS	
	@Override
	public void addQuestionAnsweredListener(QuestionAnsweredEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeQuestionAnsweredListener(QuestionAnsweredEventListener listener) {
		listeners.remove(listener);
	}
	
	public void fireEvent() throws FileNotFoundException, ClassNotFoundException {
		QuestionAnsweredEvent event = new QuestionAnsweredEvent(this, getAnswer());
		for (QuestionAnsweredEventListener listener : listeners) {
			listener.handleEvent(event);
		}
	}

	
// GETTERS & SETTERS
	@Override
	public Node getRenderedQuestion() {
		return renderedQuestion;
	}

	@Override
	public String getId() {
		return id;
	}

	public abstract Answer getAnswer();
	

    public void setRenderedQuestion(Node renderedQuestion) {
        this.renderedQuestion = renderedQuestion;
    }

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

    
}