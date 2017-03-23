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
import sun.security.util.Length;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainScreenController implements QuestionAnsweredEventListener {

    protected PrintWriter out;
	private String fileName; // file that contains study details
	
	@FXML AnchorPane mainStudy;
	@FXML Label fileNameLabel; 
	@FXML TitledPane titledPane;

    public IQuestion currentQuestion;
    private int whichQuestion;
    
    
	private static Map<String, IQuestionFactory> factoryMap;
	static {
		factoryMap = new HashMap<>();
		factoryMap.put("radio", new RadioQuestionFactory());
		// e.g. factoryMap.put("checkBox", new ...Factory()); // just marked for another types of question
	}

	
	
	public MainScreenController() throws FileNotFoundException {
        out = new PrintWriter("answers.answ");
		this.whichQuestion = -1; // we count from 0.
	}


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
		IQuestion questionComponent = readQuestionFromFile(whichQuestion);
		if (questionComponent != null){
			String questionTitle = "Pytanie " + String.valueOf(whichQuestion+1);
			titledPane.setText(questionTitle);
            mainStudy.getChildren().add(questionComponent.getRenderedQuestion());
            questionComponent.init();
        }
        else {
            endStudy();
        }
	}

    private void endStudy() {
        mainStudy.getChildren().clear();
        mainStudy.getChildren().add(new Label("Thank you!"));
    }


    
    private IQuestion readQuestionFromFile(int i) throws FileNotFoundException {
        ResourcesLoader loader = new ResourcesLoader("details");
        File questionFile = loader.loadFile(fileName);
		BufferedReader br = new BufferedReader(new FileReader(questionFile)); 
		
		boolean readingQuestions = false;
		
        String currentLine;
		int which = 0;
		List<String> questionLines = new ArrayList<>();
		String questionType = null;
		String questionId = null;
		String questionExtrasType = null;
		String questionExtrasFile = null;
		
		Map<String, String> questionParameters = new HashMap<String, String>();
		try {
			while ((currentLine = br.readLine()) != null) {
				if (currentLine.startsWith("StartQuestion")) { // begin reading questions
					
						if (readingQuestions) {
							throw new IllegalArgumentException("Invalid file format: StartQuestion without EndQuestion");
						}
						
						// LOADING QUESTION PARAMETERS
						if (which == i) {
							readingQuestions = true;
							String[] elements = currentLine.split(" ");
							System.out.println(currentLine);
							for (String elt : elements){
								String[] parameter = elt.split("=");
								if (parameter.length > 1) {
								questionParameters.put(parameter[0], parameter[1]);}
							}
						}
						else {
						which++;}
						
				} else {
					
						if (readingQuestions) {
						
							if (currentLine.startsWith("EndQuestion")) {
								
								// BUILDING QUESTION
								// GETTING TYPE AND ID
								if (questionParameters.containsKey("type")){
									questionType = questionParameters.get("type");
								} else {
									throw new IllegalArgumentException("Invalid file format: StartQuestion type=<type>");}
								
								if (questionParameters.containsKey("ID")){
									questionId = questionParameters.get("ID");
								} else {
									throw new IllegalArgumentException("Invalid file format: StartQuestion ID=<ID>");}
								
								// GETTING EXTRAS - to do: make a list of extras, make it more universal
								if (questionParameters.containsKey("music")){
									questionExtrasType = "music";
									questionExtrasFile = questionParameters.get("music");
								}
								if (questionParameters.containsKey("image")){
									questionExtrasType = "image";
									questionExtrasFile = questionParameters.get("image");
								}
								
								// USING FACTORY
								if (factoryMap.containsKey(questionType)) {
									currentQuestion = factoryMap.get(questionType).createQuestion(questionLines, questionId, questionType, questionExtrasType, questionExtrasFile);
									currentQuestion.addQuestionAnsweredListener(this);
									return currentQuestion;
								} else {
									throw new IllegalArgumentException("Do not have a factory for question type: " + questionType);
								}
							
						} else {						
							questionLines.add(currentLine.trim());
						}
					}
				}
			
		}} catch (IOException e) {
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
