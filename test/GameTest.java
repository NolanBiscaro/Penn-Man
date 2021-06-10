import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JLabel;

/**
 * You can use this file (and others) to test your implementation.
 */

public class GameTest {
/*
    @Test
    public void testAddRecord() {
        Man.setScore(5);
        Game.setUser("test");
        Game.addRecord();
        assertEquals(5, Game.getRecords().get("test"));
    }

    @Test
    public void testResetLocks() {
        GameCourt.setLeftLock(true);
        GameCourt.setDownLock(true);
        GameCourt.setRightLock(true);
        GameCourt.resetLocks();
        assertFalse(GameCourt.getLeftLock());
        assertFalse(GameCourt.getRightLock());
        assertFalse(GameCourt.getDownLock()); 
        assertFalse(GameCourt.getUpLock()); 

    }

    @Test
    public void testStopMovement() {
        GameCourt gc = new GameCourt(new JLabel());
        TA[] tas = GameCourt.getTas();
        TA ta1 = tas[0];
        TA ta2 = tas[1];
        ta1.setVx(10);
        ta2.setVy(5);
        gc.stopMovement();
        assertEquals(ta1.getVx(), 0);
        assertEquals(ta2.getVy(), 0);
    }

    @Test
    public void testUpdateScore() {
        JLabel s = new JLabel();
        s.setText(("SCORE: " + Integer.toString(0)));
        Game.updateScore(10);
        assertEquals("SCORE: " + Integer.toString(10), Game.getScore().getText());
    }

    @Test
    public void testUpdateLives() {
        Game.updateLives();
        assertEquals(Game.getLives().getText(), "LIVES: " + 3);
        Man.setLives(Man.getLives() - 1);
        Game.updateLives();
        assertEquals(Game.getLives().getText(), "LIVES: " + 2);

    }*/

}
