package angryflappybird;

import java.util.HashMap;
import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This is the class that gets almost all the constants for the AngryFlappyBird class
 */
public class Defines {
    
	// dimension of the GUI application 
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;
    
    // coefficients of easy level 
    final int EASY_REVERSED_PIPE_Y_MAX = -40;
    final int EASY_REVERSED_PIPE_Y_MIN = -150;
    final int EASY_GAP_HEIGHT_MAX = 235;
    final int EASY_GAP_HEIGHT_MIN = 220;
    
    // coefficients of medium level
    final int MEDIUM_REVERSED_PIPE_Y_MAX = -30;
    final int MEDIUM_REVERSED_PIPE_Y_MIN = -140;
    final int MEDIUM_GAP_HEIGHT_MAX = 225;
    final int MEDIUM_GAP_HEIGHT_MIN = 210;
    
    // coefficients of hard level 
    final int HARD_REVERSED_PIPE_Y_MAX = -20;
    final int HARD_REVERSED_PIPE_Y_MIN = -130;
    final int HARD_GAP_HEIGHT_MAX = 215;
    final int HARD_GAP_HEIGHT_MIN = 200;

    // coefficients related to the blob 
    final int BLOB_WIDTH = 70;
    final int BLOB_HEIGHT = 70;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -40;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floor
    final int FLOOR_WIDTH = 400;
    final int FLOOR_HEIGHT = 100;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to the pipes
    final int PIPE_WIDTH = 100;
    int PIPE_HEIGHT = 230;
    final int PIPE_COUNT = 6;
    
    // coefficients related to the time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;
    final double PARACHUTE_DURATION = 6;

    // coefficients related to the eggs 
    final int EGG_WIDTH = 60;
    final int EGG_HEIGHT = 80;
    
    // coefficients related to the pigs 
    final int PIG_WIDTH = 60;
    final int PIG_HEIGHT = 40;

    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Bird";

	private final String IMAGE_DIR = "../resources/images/";
//    private final String IMAGE_DIR = "/";
	
	// image file names storage 
    final String[] IMAGE_FILES = {"background","blob0", "blob1", "blob2", "blob3", "floor", "pipes", "reversedPipes", 
            "egg", "golden_egg", "pigHead", "night"};

    private final String AUDIO_DIR = "../resources/sounds/";
    
    // audio file names storage 
    final String[] AUDIO_FILES = {"punch-140236"};

    // hashmaps for image view, images, and audio 
    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    final HashMap<String, Media> AUDIO = new HashMap<String, Media>();

    // nodes on the scene graph 
    Button startButton;
    Button difficulty;
    Text score;
    Text life;
    
    //game rules
    ImageView goldenEggRulePic = new ImageView();
    Text goldenEggRule = new Text();
    ImageView eggRulePic = new ImageView();
    Text eggRule = new Text();
    ImageView pigRulePic = new ImageView();
    Text pigRule = new Text();
    
    // constructor
    /**
     * Here is a constructor.
     * it initializes images, audio, ImageView, and scene nodes
     * no parameter
     */
	 Defines() {

		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
//            if (i == 8) {
//                img = new Image(pathImage(IMAGE_FILES[i]), EGG_WIDTH, EGG_HEIGHT, false, false);
//            }
//			else
            if (i == 5) {
				img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			}
			else if (i == 1 || i == 2 || i == 3 || i == 4){
				img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			}
			else if (i == 0 || i == 11) {
				img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			} else if (i == 10){
			    img = new Image(pathImage(IMAGE_FILES[i]), PIG_WIDTH, PIG_HEIGHT, false, false);
			} else if (i == 8 || i == 9){
			    img = new Image(pathImage(IMAGE_FILES[i]), EGG_WIDTH, EGG_HEIGHT, false, false);
			} else {
			    img = new Image(pathImage(IMAGE_FILES[i]), PIPE_WIDTH, PIPE_HEIGHT, false, false);
			}
    		IMAGE.put(IMAGE_FILES[i],img);
    	}
		
		//initialize audio
        for(int i=0; i<AUDIO_FILES.length; i++) {
            Media sound;
            sound = new Media(pathSound(AUDIO_FILES[i]));
            AUDIO.put(AUDIO_FILES[i],sound);
        }
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize scene nodes
		startButton = new Button("Go!");
		score = new Text();
		life = new Text();
	}
	/**
     * This is pathImage method.  
     * It is used to produce the full path of an image
     * @param String filepath for the exact name of the file
     * @return the full path of the image
     */
	
	public String pathImage(String filepath) {
//        String fullpath;
//        if (Objects.equals(filepath, "egg") || Objects.equals(filepath, "golden_egg")) {
//            fullpath = getClass().getResource("/images/" + filepath + ".png").toExternalForm();
//        }
//        else {
//            fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
//        }
//        String fullpath = getClass().getResource("/"+filepath+".png").toExternalForm();
        String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	/**
     * This is pathSound method.  
     * It is used to produce the full path of a sound
     * @param filepath for the exact name of the file
     * @return the full path of the sound
     */
	public String pathSound(String filepath) {
	    //Sound soundEffect = new Sound(AUDIO_DIR+"");
        String fullpath = getClass().getResource(AUDIO_DIR+filepath+".mp3").toExternalForm();
        return fullpath;
    }
	
	/**
     * This is pathImage method.  
     * It is used to produce the full path of an image
     * @param filepath The exact name of the file
     * @param width The width you want to resize this image to
     * @param height The height you want to resize this image to
     * @return the image after resizing
     */
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
}
