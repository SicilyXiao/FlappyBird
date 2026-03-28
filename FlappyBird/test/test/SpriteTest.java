package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import angryflappybird.Sprite;
import javafx.scene.image.Image;

class SpriteTest {
    
    Sprite s;

    @BeforeEach
    void setUp() throws Exception {
        s = new Sprite();
    }

    @Test
    void testGetPositionX() {
        assertEquals(s.getPositionX(),0);
    }
    
    @Test
    void testGetPositionY() {
        assertEquals(s.getPositionY(),0);
    }
    
    @Test
    void testGetVelocityX() {
        assertEquals(s.getVelocityX(),0);
    }
    
    @Test
    void testGetVelocityY() {
        assertEquals(s.getVelocityY(),0);
    }
    
    @Test
    void testSetPosition() {
        s.setPositionXY(20, 30);
        assertEquals(s.getPositionX(), 20);
        assertEquals(s.getPositionY(), 30);
    }
    
    @Test
    void testSetVelocity() {
        s.setVelocity(40, 50);
        assertEquals(s.getVelocityX(), 40);
        assertEquals(s.getVelocityY(), 50);
    }
    
    @Test
    void testAddVelocity() {
        s.setVelocity(0, 10);
        assertEquals(s.getVelocityX(), 0);
        assertEquals(s.getVelocityY(), 10);
        s.addVelocity(20, 10);
        assertEquals(s.getVelocityX(), 20);
        assertEquals(s.getVelocityY(), 20);
    }
    
    @Test
    void testUpdate() {
        s.setPositionXY(20, 30);
        s.setVelocity(0, 10);
        s.update(2);
        assertEquals(s.getPositionX(), 20);
        assertEquals(s.getPositionY(), 50);
    }

}
