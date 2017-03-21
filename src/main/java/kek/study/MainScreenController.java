package kek.study;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import kek.study.question.*;
import kek.study.question.event.QuestionAnsweredEvent;
import kek.study.question.event.QuestionAnsweredEventListener;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


import javafx.scene.control.TitledPane;

public class MainScreenController implements QuestionAnsweredEventListener {

    protected PrintWriter out;
	private int whichQuestion;
	private String fileName; // file that contains study details

	public MainScreenController() throws FileNotFoundException {
        out = new PrintWriter("answers.answ");
		this.whichQuestion = -1; // we count from 0.
	}

	private static Map<String, IQuestionFactory> factoryMap;
	static {
		factoryMap = new HashMap<>();
		factoryMap.put("radio", new RadioQuestionFactory());
//		factoryMap.put("music", new MusicRadioQuestionFactory()); // we should not create another factories
//		factoryMap.put("image", new ImageRadioQuestionFactory()); // that's all radio questions
		// e.g. factoryMap.put("checkBox", new ...Factory()); // just marked for another types of question
	}

	@FXML AnchorPane mainStudy;
	@FXML Label fileNameLabel; 
	@FXML TitledPane titledPane;

    public IQuestion currentQuestion;

//	int trackNumber = 1; // it's weird place for this var, but it don't destroy anything
//  it was extremly ugly!


	@FXML public void startStudy() throws FileNotFoundException {
		if (fileName == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Jest error");
			alert.setHeaderText("Brak pliku");
			alert.setContentText("Proszę wybrać plik z pytaniami");
			alert.showAndWait();
		} else {
			displayQuestion();
	    }
	}
	
	@FXML public void displayQuestion() throws FileNotFoundException {
        whichQuestion++;
		mainStudy.getChildren().clear();

		// below historical code
//		InputStream is = getClass().getResourceAsStream(System.getProperty("user.dir")
//				+ File.separator + "target" + File.separator + "classes"
//				+ File.separator + "details" +  fileName); // it is null, so whole program doesn't work.
//		InputStream is = getClass().getResourceAsStream(fileName); // it is null, so whole program doesn't work.
		Node questionComponent = readQuestionFromFile(whichQuestion);
		// maybe better will be: questionComponent = currentQuestion.getRenderedQuestion();
		if (questionComponent != null){
			String questionTitle = "Pytanie " + String.valueOf(whichQuestion+1);
			titledPane.setText(questionTitle);
            mainStudy.getChildren().add(questionComponent);
        }
        else {
            endStudy();
        }

	}

    private void endStudy() {
        mainStudy.getChildren().clear();
        mainStudy.getChildren().add(new Label("Thank you!"));
    }

    private Node readQuestionFromFile(int i) throws FileNotFoundException {
        ResourcesLoader loader = new ResourcesLoader("details");
        File questionFile = loader.loadFile(fileName);
		BufferedReader br = new BufferedReader(new FileReader(questionFile)); // Files are easier than InputStream
        String currentLine;
		int which = 0;
		List<String> questionLines = new ArrayList<>();
		boolean readingQuestions = false;
		String questionType = null;
		String questionId = null;
		String questionExtrasType = null;
		String questionExtrasFile = null;
		try {
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.startsWith("StartQuestion")) { // begin reading questions
					if (readingQuestions) {
						throw new IllegalArgumentException("Invalid file format: StartQuestion without EndQuestion");
					}
					if (which == i) {
						readingQuestions = true;
						String[] elements = currentLine.split(" ");
						if (elements.length > 1) {
							String[] givenType = elements[1].split("=");
							if (givenType.length > 1) {
								questionType = givenType[1];
							}
							if (elements.length > 2){
								String[] givenID = elements[2].split("=");
								questionId = givenID[1];
							}
						}
						currentLine = br.readLine(); // second line of study details contains type and file of extras
						String[] elements2 = currentLine.split(" "); // for future image+music (mixed) questions.
						if (elements2.length > 0 && !elements2[0].equals("")) {
							// shold be 'for' here if we'll want to extend application to allow multiple images/music files or even mixed.
							String[] extraTypeAndFile = elements2[1].split("=");
							questionExtrasType = elements2[0];
							questionExtrasFile = elements2[1];
						}
						if (questionType == null) {
							throw new IllegalArgumentException("Invalid file format: StartQuestion type=<type>");
						}
						if (questionId == null) {
							throw new IllegalArgumentException("Invalid file format: StartQuestion ID=<ID>");
						}
					} else {
						which++;
					}
				} else {
					if (readingQuestions) {
						if (currentLine.startsWith("EndQuestion")) {
							// build question
							if (factoryMap.containsKey(questionType)) {
								currentQuestion = factoryMap.get(questionType).createQuestion(questionLines, questionId, questionType, questionExtrasType, questionExtrasFile);
								currentQuestion.addQuestionAnsweredListener(this);
								return currentQuestion.getRenderedQuestion();
							} else {
								throw new IllegalArgumentException("Do not have a factory for question type: " + questionType);
							}
						} else {						
							questionLines.add(currentLine.trim());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void handleEvent(QuestionAnsweredEvent event) throws FileNotFoundException {
		IQuestion question = event.getQuestion();
		Answer answer = event.getAnswer();
        event.saveToFile();
        question.terminate();
        try {
            displayQuestion();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

	public void chooseFile() {
		 FileChooser fileChooser = new FileChooser();
		 String currentDir = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes" +
                 File.separator + "details" + File.separator;
         File file = new File(currentDir); // Should open in our working directory + path to study details
         fileChooser.setInitialDirectory(file);
		 fileChooser.setTitle("Open Resource File");
		 fileChooser.getExtensionFilters().addAll(
				 new ExtensionFilter("Question Files", "*.sqf"),
		         new ExtensionFilter("Text Files", "*.txt"),
		         new ExtensionFilter("All Files", "*.*"));
		 File selectedFile = fileChooser.showOpenDialog(mainStudy.getScene().getWindow());
		 if (selectedFile != null){
			 fileName = selectedFile.getName();
			 fileNameLabel.setText(fileName);
		}
	}
	
}
