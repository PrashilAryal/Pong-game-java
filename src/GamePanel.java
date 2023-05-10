import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class GamePanel extends JPanel implements Runnable{

    static final int GAME_WIDTH = 1000;     //static:so that all the instances made of GamePanel will be sharing same game width; final:is a little bit faster nothing other specific
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    private Image backgroundImage;
//    Clip goalSound = loadSound("goal.wav");


    GamePanel(){
//        File file = new File("goal.wav");
//        Clip clip = null;
//        try {
//            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
//            clip = AudioSystem.getClip();
//            clip.open(audioStream);
//        } catch (UnsupportedAudioFileException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        }
//        clip.start();


        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new ActListner());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    private void playSound(){
        File file = new File("goal.wav");
        Clip clip = null;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        clip.start();
    }
    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
    }
    public void newPaddles(){
        paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT,1);
        paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT,2);

    }


    public void paint(Graphics g){
        backgroundImage = new ImageIcon("hockeyGround.jpg").getImage();
        g.drawImage(backgroundImage,0,0,getWidth(),getHeight(),this);
        draw(g);


//        image = createImage(getWidth(),getHeight());
//        graphics = image.getGraphics();
//        draw(graphics);
//        g.drawImage(image,0,0,this);
    }

    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }
    public void checkCollision(){
        //bounce ball off top & bottom window edges
        if(ball.y<=0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y>=GAME_HEIGHT-BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        //bounces ball off paddles
        if(ball.intersects(paddle1)){       //cause ball extends all the properties of rectangle so we can use intersects function
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;       //optional for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++;   //optional for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)){       //cause ball extends all the properties of rectangle so we can use intersects function
            ball.xVelocity=Math.abs(ball.xVelocity);
            ball.xVelocity++;       //optional for more difficulty
            if(ball.yVelocity>0)
                ball.yVelocity++;   //optional for more difficulty
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        //stops paddles at window edges
        if(paddle1.y<=0)                                //upside
            paddle1.y=0;
        if(paddle1.y>=(GAME_HEIGHT-PADDLE_HEIGHT))      //downside
            paddle1.y=GAME_HEIGHT-PADDLE_HEIGHT;

        if(paddle2.y<=0)
            paddle2.y=0;
        if(paddle2.y>=(GAME_HEIGHT-PADDLE_HEIGHT))
            paddle2.y=GAME_HEIGHT-PADDLE_HEIGHT;

        //give a player 1 point and create new paddles & ball
        if(ball.x<=0){
            playSound();
            score.scorePlayer2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: "+score.scorePlayer2);
        }
        if(ball.x>=GAME_WIDTH-BALL_DIAMETER){
            playSound();
            score.scorePlayer1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: "+score.scorePlayer1);
        }
    }
    public void run(){
        //game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double nanoSeconds = 1000000000/amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta+=(now-lastTime)/nanoSeconds;
            lastTime = now;
            if(delta >=1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    public class ActListner extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
