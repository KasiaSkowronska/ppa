package kek.study.question;

import java.io.IOException;
import java.util.List;



public interface IQuestionFactory {
		
	public IQuestion createQuestion(List<String> lines, String questionId, String questionType,
									String questionExtrasType, String questionExtrasFile) throws IOException;

}
