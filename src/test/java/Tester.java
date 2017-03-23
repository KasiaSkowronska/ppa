import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import kek.study.question.Answer;
import kek.study.question.RadioQuestion;
import kek.study.question.RadioQuestionFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pwilkin on 01-Mar-17.
 */
public class Tester {

    public static class MockApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // noop
        }
    }

    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(MockApp.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
    }
//
//    private RadioQuestion createMockQuestion() throws IOException {
//        RadioQuestionFactory rqf = new RadioQuestionFactory();
//        List<String> lines = new ArrayList<>();
//        lines.add("The Question");
//        lines.add("First answer");
//        lines.add("FA");
//        lines.add("Second answer");
//        lines.add("SA");
//        return rqf.createQuestion(lines, "LINES");
//        return (RadioQuestion) rqf.createQuestion(lines, questionId, questionType, questionExtrasType, "LINES");
//    }
//
//    @Test
//    public void testCreateQuestion() throws IOException {
//        RadioQuestion q = createMockQuestion();
//        Assert.assertNotNull(q);
//    }

////    @Test
////    public void testRadioNoDefaultAnswer() throws IOException {
////        RadioQuestion q = createMockQuestion();
////        Assert.assertNull(q.getAnswer());
////        Assert.assertNotNull(q.getAnswer());
////        Assert.assertNull(q.getAnswer().getAnswer());
////    }
////
////    @Test
////    public void testAnswerIsAnswer() throws IOException {
////        RadioQuestion q = createMockQuestion();
////        Parent render = (Parent) q.getRenderedQuestion();
////        RadioButton firstButton = (RadioButton) render.getChildrenUnmodifiable().stream().filter(x -> x instanceof RadioButton).findFirst().orElse(null);
////        Assert.assertNotNull(firstButton);
////        firstButton.setSelected(true);
////        Answer a = q.getAnswer();
////        Assert.assertEquals("FA", a.getAnswer());
////    }
//
//    @Test
//    public void testDatabase(){
//
//    }

}