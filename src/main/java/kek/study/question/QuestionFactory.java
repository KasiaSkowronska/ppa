package kek.study.question;

import java.io.IOException;
import java.util.List;



public interface QuestionFactory {
		
	public IQuestion createQuestion(List<String> lines, String id) throws IOException;
	
}
