package kek.study;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Node;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Created by kosss on 15.03.2017.
 */
public class ResourcesLoader {
    // he will load resources like images or music files specified in second line in particular study details
    // he also load study details.

    String folderName;

    public ResourcesLoader(String folderName) {
        this.folderName = folderName;
    }

    public File loadFile(String fileName){
        String fileDir = getFileDir(fileName);
        File file = new File(fileDir);
        return file;
    }

    public Image loadImage(String fileName) {
        String fileDir = getFileDir(fileName);
        final Image image = new Image(fileDir);
        return image;
    }

    public String getFileDir(String fileName){
        String fileDir = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes"
                + File.separator + this.folderName + File.separator + fileName;
        return fileDir;
    }

}
