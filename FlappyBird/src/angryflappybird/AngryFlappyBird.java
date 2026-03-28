package angryflappybird;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This is the application layer of angry flappy bird game
 */
//The Application layer
public class AngryFlappyBird extends Application {
    
    /**
     * Here we define all the class parameters
     */
    private Defines DEF = new Defines();

    // time related attributes
    private long clickTime, startTime, elapsedTime;
    private AnimationTimer timer;

    // game components
    private Sprite blob;
    private ArrayList<Sprite> floors;
    private ArrayList<Sprite> pipes;
    private ArrayList<Sprite> reversedPipes;
    private int lifeCount;
    private int reversedPipeMaxY;
    private int reversedPipeMinY;
    private int gapHeightMax;
    private int gapHeightMin;

    private ArrayList<Sprite> eggs;
    private ArrayList<Sprite> pigs;

    private ChoiceBox<String> choices;   
    private String difficulty;
    
    private boolean parachuteMode;
    private long snoozeTime;
    private long startSnooze;
    MediaPlayer mediaPlayer = new MediaPlayer(DEF.AUDIO.get("punch-140236"));
    
    // game score
    private int score;

    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER;

    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private GraphicsContext gc;
    private ImageView nightBackground = new ImageView();
    private long eachRoundStartTime = 0;

    // game scene nodes/text
    private Label titleFloor = new Label();
    private Rectangle rect = new Rectangle();
    private Text title = new Text();
    private Text description = new Text();
    private Text gameover = new Text();
    private Text parachute = new Text();

    // the mandatory main method
    /**
     * This is the main method that launch the whold application
     * @param String[] args, automatically
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This is the start method that sets the Stage layer
     * @param primaryStage
     * It overrides the start method in application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
        resetGameScene(true);  // resets the gameScene
        

        HBox root = new HBox();
        HBox.setMargin(gameScene, new Insets(0,0,0,15));
        root.getChildren().add(gameScene);
        root.getChildren().add(gameControl);



        // add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);

        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // the getContent method sets the Scene layer
    private void resetGameControl() {
        
        lifeCount = 3;
        score = 0;
        reversedPipeMaxY = 0;
        reversedPipeMinY = 0;
        gapHeightMax = 0;
        gapHeightMin = 0;
        difficulty = "";
        parachuteMode = false;
        snoozeTime = 0;
        startSnooze = 0;
        eachRoundStartTime = System.nanoTime();
        

        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        DEF.score.setText("score: 0");
        DEF.life.setText("life count: 3");
        DEF.goldenEggRulePic.setImage(DEF.IMAGE.get("golden_egg"));
        DEF.goldenEggRule.setText("Parachute Mode");
        DEF.eggRulePic.setImage(DEF.IMAGE.get("egg"));
        DEF.eggRule.setText("Extra Points");
        DEF.pigRulePic.setImage(DEF.IMAGE.get("pigHead"));
        DEF.pigRule.setText("Avoid Pig");
        
        choices = new ChoiceBox<>();
        choices.getItems().add("Easy");
        choices.getItems().add("Medium");
        choices.getItems().add("Difficult");
        choices.setValue("Easy");
               
        gameControl = new VBox();
        gameControl.getChildren().addAll(DEF.startButton);
        gameControl.getChildren().add(DEF.score);
        gameControl.getChildren().add(DEF.life);
        gameControl.getChildren().add(choices);
        gameControl.getChildren().add(DEF.goldenEggRulePic);
        gameControl.getChildren().add(DEF.goldenEggRule);
        gameControl.getChildren().add(DEF.eggRulePic);
        gameControl.getChildren().add(DEF.eggRule);
        gameControl.getChildren().add(DEF.pigRulePic);
        gameControl.getChildren().add(DEF.pigRule);

        

        DEF.score.setText("score = " + score);

    }
    
    //get the difficulty choice
    private void getChoice() {
        difficulty = choices.getValue();
        if(difficulty.equals("Hard")) {
            reversedPipeMaxY = DEF.HARD_REVERSED_PIPE_Y_MAX;
            reversedPipeMinY = DEF.HARD_REVERSED_PIPE_Y_MIN;
            gapHeightMax = DEF.HARD_GAP_HEIGHT_MAX;
            gapHeightMin = DEF.HARD_GAP_HEIGHT_MIN;
        } else if (difficulty.equals("Medium")) {
            reversedPipeMaxY = DEF.MEDIUM_REVERSED_PIPE_Y_MAX;
            reversedPipeMinY = DEF.MEDIUM_REVERSED_PIPE_Y_MIN;
            gapHeightMax = DEF.MEDIUM_GAP_HEIGHT_MAX;
            gapHeightMin = DEF.MEDIUM_GAP_HEIGHT_MIN;
        } else {
            reversedPipeMaxY = DEF.EASY_REVERSED_PIPE_Y_MAX;
            reversedPipeMinY = DEF.EASY_REVERSED_PIPE_Y_MIN;
            gapHeightMax = DEF.EASY_GAP_HEIGHT_MAX;
            gapHeightMin = DEF.EASY_GAP_HEIGHT_MIN;
        }
    }
    

    private void mouseClickHandler(MouseEvent e) {
        if (GAME_OVER) {
            resetGameScene(false);
        }
        else if (GAME_START){
            clickTime = System.nanoTime();
        }
        GAME_START = true;
        CLICKED = true;
    }

    private void resetGameScene(boolean firstEntry) {

        // reset variables
        lifeCount = 3;
        CLICKED = false;
        GAME_OVER = false;
        GAME_START = false;
        floors = new ArrayList<>();
        pipes = new ArrayList<>();
        reversedPipes = new ArrayList<>();
        eggs = new ArrayList<>();
        pigs = new ArrayList<>();
        getChoice();
        
    	if(firstEntry) {
    		// create two canvases
        score = 0;
    	}

        if(firstEntry) {
            // create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();

            // create a background
            ImageView background = DEF.IMVIEW.get("background");
            nightBackground = DEF.IMVIEW.get("night");
            nightBackground.setVisible(false);

            // set the title screen text and other objects
            rect.setX(25);
            rect.setY(190);
            rect.setWidth(350);
            rect.setHeight(155);
            rect.setFill(Color.rgb(205, 225, 255));
            rect.setStrokeWidth(2);
            rect.setStroke(Color.BLACK);

            title.setX(45);
            title.setY(250);
            title.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 30));
            title.setFill(Color.RED);
            title.setStrokeWidth(2);
            title.setStroke(Color.BLACK);
            title.setText("Angry Flappy Bird");

            gameover.setX(120);
            gameover.setY(250);
            gameover.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 30));
            gameover.setFill(Color.RED);
            gameover.setStrokeWidth(2);
            gameover.setStroke(Color.BLACK);
            gameover.setText("GAME OVER");
            gameover.setOpacity(0);

            description.setX(70);
            description.setY(300);
            description.setFont(Font.font("Courier New", FontWeight.NORMAL, FontPosture.REGULAR, 22));
            description.setText("Click 'Go' to Play!");
            
            parachute.setX(100);
            parachute.setY(200);
            parachute.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 22));
            parachute.setStrokeWidth(1);
            parachute.setStroke(Color.WHITE);
            parachute.setText("Parachute Mode");
            parachute.setOpacity(0);


            // initialize floor

            Image floorImage = DEF.IMAGE.get("floor");
            titleFloor.setTranslateY(DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT);
            titleFloor.setGraphic(new ImageView(floorImage));

            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, nightBackground, canvas, rect, title, description, titleFloor, gameover, parachute);
    	}
    	
    	// initialize floor

    	for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    		
    		int posX = i * DEF.FLOOR_WIDTH;
    		int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    		
    		Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor"));
    		floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		floor.render(gc);
    		
    		floors.add(floor);
    	}
    	
         
        // initialize pipes
        for(int i=0; i<DEF.PIPE_COUNT; i++) {
            
            double randomYReversedPipe = (Math.floor(Math.random() *(reversedPipeMaxY - reversedPipeMinY + 1))) + reversedPipeMinY;
            double randomGapHeight = (Math.floor(Math.random() *(gapHeightMax - gapHeightMin + 1)))+ gapHeightMin;
            double randomYPipe = DEF.SCENE_HEIGHT - randomYReversedPipe - randomGapHeight;
           
            double posX = 350 + i * DEF.PIPE_WIDTH;

            Sprite pipe = new Sprite(posX, randomYPipe, DEF.IMAGE.get("pipes"));
            Sprite reversedPipe = new Sprite(posX, randomYReversedPipe, DEF.IMAGE.get("reversedPipes"));
           
            pipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            reversedPipe.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            pipe.render(gc);
            reversedPipe.render(gc);

            pipes.add(pipe);
            reversedPipes.add(reversedPipe);
            
//            if (Math.random() < 0.25 * i && eggsIndexes.size() < 3) {
//                
//                double eggPosY = randomYPipe - DEF.EGG_HEIGHT;
//                Sprite egg;
//                // white eggs
//                if (eggs.size() < 2) {
//                    egg = new Sprite(posX + 25, eggPosY, DEF.resizeImage("egg", DEF.EGG_WIDTH, DEF.EGG_HEIGHT));
//                }
//                // golden egg
//                else {
//                    egg = new Sprite(posX + 25, eggPosY, DEF.resizeImage("golden_egg", DEF.EGG_WIDTH, DEF.EGG_HEIGHT));
//                    egg.setGoldenEgg(true);
//                }
//                egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
//                egg.render(gc);
//                eggs.add(egg);
//                eggsIndexes.add(i);
//            }  
            
            //initialize egg
            double eggPosY = randomYPipe - DEF.EGG_HEIGHT;
            Sprite egg = new Sprite(posX + 25, eggPosY, DEF.IMAGE.get("egg"));            
            double rNumEgg = Math.random();
            if(rNumEgg < 0.6) {
                if(rNumEgg < 0.4) {
                    egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                } else {
                    egg.setPositionXY(posX, -900);
                    egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                }
            } else if (rNumEgg >= 0.6) {
                egg.setImage(DEF.IMAGE.get("golden_egg"));
                egg.setGoldenEgg(true);
                if(rNumEgg > 0.8) {
                    egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                } else {
                    egg.setPositionXY(posX, -900);
                    egg.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                }
            } 
            egg.render(gc);
            eggs.add(egg);
            
            //initialize pig
            Sprite pig = new Sprite(posX + 25, 0, DEF.IMAGE.get("pigHead"));
            
            double rNum = Math.random();
            if(rNum < 0.1) {
                pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0.25);
            } else {
                pig.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                pig.setPositionXY(posX, -80);
            }
            pig.render(gc);
            pigs.add(pig);

        }

        // initialize blob
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y,DEF.IMAGE.get("blob0"));
        blob.collided = false;
        blob.render(gc);

        // initialize timer
        startTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
    }
    
    /**
     * This is the timer class inside the application class.
     * It is responsible for running the the application once it is initialized and starts
     * It extends the AnimationTimer
     */
    class MyTimer extends AnimationTimer {

        int counter = 0;

         @Override
         public void handle(long now) {          
             // time keeping
             elapsedTime = now - startTime;
             startTime = now;

             // clear current scene
             gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);

             if (GAME_START) {
                 // step1: update floor
                 moveFloor();

                 // step2: update blob
                 moveBlob();
                 
               //step3: update pipe
                 movePipes();
                 
                 //step4: check collision
                 checkCollision();
                 
                 //background update
                 backgroundChange();
                              
             }
         }
         
         // background update
         
         private void backgroundChange() {
             long currentTime =  (long) ((System.nanoTime()-eachRoundStartTime) * DEF.NANOSEC_TO_SEC);
             if (currentTime % 20 > 15) {
                 nightBackground.setVisible(true);
             } else {
                 nightBackground.setVisible(false);
             }
         }
         

         // step1: update floor
         private void moveFloor() {

            for(int i=0; i<DEF.FLOOR_COUNT; i++) {
                if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
                    double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
                    double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
                    floors.get(i).setPositionXY(nextX, nextY);
                }
                floors.get(i).render(gc);
                floors.get(i).update(DEF.SCENE_SHIFT_TIME);
            }
         }
         
         

         // step2: update blob
         private void moveBlob() {
            
         // hide title/game over screen
            rect.setOpacity(0);
            title.setOpacity(0);
            description.setOpacity(0);
            titleFloor.setOpacity(0);
            gameover.setOpacity(0);

            long diffTime = System.nanoTime() - clickTime;

            // blob flies upward with animation
            if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {

                int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
                imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
                blob.setImage(DEF.IMAGE.get("blob"+String.valueOf(imageIndex)));
                blob.setVelocity(0, DEF.BLOB_FLY_VEL);
            }
            // blob drops after a period of time without button click
            else {
                blob.setVelocity(0, DEF.BLOB_DROP_VEL);
                CLICKED = false;
            }

            // render blob on GUI
            blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
            blob.render(gc);
         }
         
         /**
          * It checks if one sprite collides with another one, and play collision sound when two sprites collide.
          * no parameter.
          * no return.
          * It changes the score if the sprite collides with eggs or pigs, accordingly.
          * It changes the lifeCount if the sprite collides with floors, pipes, or pigs, accordingly.
          * It ends the game when GAME_OVER == true and lifeCount == 0.
          */
         public void checkCollision() {
//check collision boolean
             
             // check collision
             for (Sprite floor: floors) {
                 if (blob.intersectsSprite(floor) && !blob.collided) {
                     blob.setCollided(true);
                     lifeCount = 0;
                 }
             }

             // check collision
             for (Sprite pipe: pipes) {
                 //GAME_OVER = (GAME_OVER || blob.intersectsSprite(pipe)) && lifeCount == 0;
                 if (blob.intersectsSprite(pipe) && !blob.collided) {
                     blob.setCollided(true);
                     lifeCount = lifeCount - 1;
                 }
             }

             for (Sprite reversedPipe: reversedPipes) {
                 //GAME_OVER = (GAME_OVER || blob.intersectsSprite(reversedPipe)) && lifeCount == 0;
                 if (blob.intersectsSprite(reversedPipe) && !blob.collided) {
                     blob.setCollided(true);
                     lifeCount = lifeCount - 1;
                 }
             }
             
             for (Sprite pig: pigs) {
                 //GAME_OVER = (GAME_OVER || blob.intersectsSprite(reversedPipe)) && lifeCount == 0;
                 if (blob.intersectsSprite(pig) && !blob.collided && !parachuteMode) {
                     blob.setCollided(true);
                     lifeCount = 0;
                 } else {
                     for (Sprite egg: eggs) {
                         if(egg.intersectsSprite(pig)) {
                             egg.setPositionXY(-100, -200);
                             score = score - 5;
                         }
                     }
                 }
             }

             // handle egg collision (update score or put on parachute)
             for (Sprite egg: eggs) {
                 if (blob.intersectsSprite(egg)) { //&& !egg.collided
                     if (!egg.isGoldenEgg) {
                         score += 5;
                         //egg.setCollided(true);
                     }
                     else {
                         startSnooze = System.nanoTime();
                         snoozeTime = 0;
                         egg.setCollided(true);
                         //parachute mode
                         parachuteMode = true;
                         //activateParachuteMode(startSnooze);                         
                     }
                     egg.setCollided(false);
                     egg.setPositionXY(-100, 100);
                 }
             }
             
             activateParachuteMode(startSnooze);
             
             if(lifeCount <= 0) {
                 GAME_OVER = true;
             }

             // end the game when blob hit stuff
             if (GAME_OVER) {
                 showHitEffect();
                 for (Sprite floor: floors) {
                     floor.setVelocity(0, 0);
                 }
                 timer.stop();
                 gameover.setOpacity(1);
                 description.setOpacity(1);
                 rect.setOpacity(1);
                 score = 0;
                 lifeCount = 0;
                 DEF.score.setText("score = " + score);
                 DEF.life.setText("life count: " + lifeCount);
             } else if (blob.collided){
                 blob.setCollided(false);
                 blob.setPositionXY(DEF.BLOB_POS_X, 235);
                 mediaPlayer.play();
             } else {
                 DEF.score.setText("score = " + score);
                 DEF.life.setText("life count: " + lifeCount);
             }
         }
         
         private void activateParachuteMode(double startSnooze) {
             if (parachuteMode) { 
                 blob.setPositionXY(DEF.BLOB_POS_X, 235);
                 blob.setVelocity(0, 0);
                 parachute.setOpacity(1);
                 if (snoozeTime > 6) {
                     parachuteMode = false;
                     parachute.setOpacity(0);
                 } else {
                     snoozeTime = (long) ((System.nanoTime() - startSnooze) * DEF.NANOSEC_TO_SEC);
                 }
             }
         }

         
         private void movePipes() {
             
             double randomYReversedPipe = 0;
             double randomGapHeight = 0;
             double randomYPipe = 0;
             double nextX = 0;
             
             for(int i=0; i<DEF.PIPE_COUNT; i++) {

                 //move pipes
                 randomYReversedPipe = (Math.floor(Math.random() *(reversedPipeMaxY - reversedPipeMinY + 1))) + reversedPipeMinY;
                 randomGapHeight = (Math.floor(Math.random() *(gapHeightMax - gapHeightMin + 1)))+ gapHeightMin;
                 randomYPipe = DEF.SCENE_HEIGHT - randomYReversedPipe - randomGapHeight;

                 if (pipes.get(i).getPositionX() <= -DEF.PIPE_WIDTH) {
                     nextX = pipes.get((i+5)%DEF.PIPE_COUNT).getPositionX() + DEF.PIPE_WIDTH;
                     pipes.get(i).setPositionXY(nextX, randomYPipe);
                 }              
                 pipes.get(i).render(gc);
                 pipes.get(i).update(DEF.SCENE_SHIFT_TIME);

                 // update score when blob passes pipe
                 if (pipes.get(i).getPositionX() == 80) {
                     score++;
                 }
                 
                 if (reversedPipes.get(i).getPositionX() <= -DEF.PIPE_WIDTH) {
                     nextX = pipes.get((i+5)%DEF.PIPE_COUNT).getPositionX() + DEF.PIPE_WIDTH;
                     reversedPipes.get(i).setPositionXY(nextX, randomYReversedPipe);
                 }
                reversedPipes.get(i).render(gc);
                reversedPipes.get(i).update(DEF.SCENE_SHIFT_TIME);
                
//                //move eggs
//                if (eggsIndexes.contains(i) && eggs.get(eggsIndexes.indexOf(i)).getPositionX() <= -DEF.EGG_WIDTH) {
//                    // move the egg onto a different upcoming pipe
//                    nextX = pipes.get((i+5)%DEF.PIPE_COUNT).getPositionX() + 20 + DEF.EGG_WIDTH;
//                    double eggNextY = randomYPipe - DEF.EGG_HEIGHT;
//                    eggs.get(eggsIndexes.indexOf(i)).setPositionXY(nextX + 5, eggNextY);
//                    eggs.get(eggsIndexes.indexOf(i)).setCollided(false);
//
//                    int randNum = (int) (Math.random() * 6);
//                    while (eggsIndexes.contains(randNum)) {
//                        randNum = (int) (Math.random() * 6);
//                    }
//                    eggsIndexes.set(eggsIndexes.indexOf(i), randNum);
//                 }
//                
//
//                 if (eggsIndexes.contains(i)) {
//                        eggs.get(eggsIndexes.indexOf(i)).render(gc);
//                        eggs.get(eggsIndexes.indexOf(i)).update(DEF.SCENE_SHIFT_TIME);
//                 }
                
                //move eggs
                if (eggs.get(i).getPositionX() <= -DEF.PIPE_WIDTH) {
                    nextX = pipes.get((i+5)%DEF.PIPE_COUNT).getPositionX() + 25;
                    double nextYEgg = pipes.get((i+5)%DEF.PIPE_COUNT).getPositionY() - DEF.EGG_HEIGHT;
                    double rNum = Math.random();
                    if(rNum < 0.5) {
                        eggs.get(i).setPositionXY(nextX, nextYEgg);
                        eggs.get(i).setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                    } else {
                        eggs.get(i).setPositionXY(nextX, 900);
                        eggs.get(i).setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                    }
                } 
                eggs.get(i).render(gc);
                eggs.get(i).update(DEF.SCENE_SHIFT_TIME);
                
               //move pigs
                 if (pigs.get(i).getPositionX() <= -DEF.PIPE_WIDTH) {
                     nextX = pipes.get((i+5)%DEF.PIPE_COUNT).getPositionX() + 25 + DEF.PIPE_WIDTH;
                     double rNum = Math.random();
                     if(rNum < 0.1) {
                         pigs.get(i).setPositionXY(nextX, 0);
                         pigs.get(i).setVelocity(DEF.SCENE_SHIFT_INCR, 0.25);
                     } else {
                         pigs.get(i).setVelocity(DEF.SCENE_SHIFT_INCR, 0);
                         pigs.get(i).setPositionXY(nextX, -80);
                     }
                 } 
                 pigs.get(i).render(gc);
                 pigs.get(i).update(DEF.SCENE_SHIFT_TIME);

             }                   
              
         }

         private void showHitEffect() {
            ParallelTransition parallelTransition = new ParallelTransition();
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
            fadeTransition.setToValue(0);
            fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
            fadeTransition.setAutoReverse(true);
            parallelTransition.getChildren().add(fadeTransition);
            parallelTransition.play();
         }

     // End of MyTimer class
    }

}// End of AngryFlappyBird Class
