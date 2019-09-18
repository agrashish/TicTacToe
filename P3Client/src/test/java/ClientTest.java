import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.function.Consumer;


// Creating the Tests for Client
class ClientTest {
    GameInfo info = new GameInfo();
    String ip = "127.0.0.1";
    int port = 5;



    // Testing for the right class name
    @Test
    void testGameInfo() {
        assertEquals("GameInfo",info.getClass().getName(),"The names match.");
    }

    // Testing for the right port
    @Test
    void testPort() {
        assertEquals(5,port,"The ports match.");
    }

    // Testing for the right ip Address
    @Test
    void testIP() {
        assertNotEquals("127.0.0.0",ip,"The ip addresses don't match.");
    }

    // Testing to make sure gameinfo knows it doesn't have two players
    @Test
    void testGameInfoOpponentPlayed() {
        assertNotEquals(1,info.opponentPlayed,"The values are not equal.");
    }
}
