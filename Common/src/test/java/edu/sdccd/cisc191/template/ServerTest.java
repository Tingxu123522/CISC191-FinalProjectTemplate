import com.sun.security.ntlm.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    private Server server;

    @BeforeEach
    void setUp() {
        server = new Server();
    }

    @Test
    void testGetAtIndex() {
        // Test valid index
        server.array[2][3] = 42;
        assertEquals(42, server.getAtIndex(2, 3));

        // Test invalid index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> server.getAtIndex(10, 10));
    }

    @Test
    void testSetAtIndex() {
        // Test valid index
        server.setAtIndex(1, 1, 99);
        assertEquals(99, server.array[1][1]);

        // Test invalid index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> server.setAtIndex(10, 10, 99));
    }

    @Test
    void testFindIndexOf() {
        // Test existing value
        server.array[3][2] = 123;
        assertEquals(new int[]{3, 2}, server.findIndexOf(123));

        // Test non-existing value
        assertNull(server.findIndexOf(999));
    }

    @Test
    void testPrintAll() {
        // Assuming printAll() method prints the entire array
        // We can't directly test the console output, so we'll just call the method
        server.printAll();
    }

    @Test
    void testDeleteAtIndex() {
        // Test valid index
        server.array[4][0] = 77;
        server.deleteAtIndex(4, 0);
        assertEquals(0, server.array[4][0]);

        // Test invalid index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> server.deleteAtIndex(10, 10));
    }

}
