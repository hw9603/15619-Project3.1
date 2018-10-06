package edu.cmu.cs.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Usage:
 * mvn test
 *
 * <p>You should pass all the provided test cases before you make any submission.
 *
 * <p>Feel free to add more test cases.
 */
class RedisTest {

    @Test
    void set() {
        Redis redisClient = new Redis();

        assertEquals("OK", redisClient.set("mykey", "cloud"));
        assertEquals("OK", redisClient.set("mykey", "cool"));
        assertEquals("OK", redisClient.set("secondkey", "yes"));
        assertNotEquals("Random", redisClient.set("thirdkey", "no"));
    }

    @Test
    void get() {
        Redis redisClient = new Redis();

        assertNull(redisClient.get("mykey"));

        redisClient.set("mykey", "cloud");
        assertNotNull(redisClient.get("mykey"));
        assertEquals("cloud", redisClient.get("mykey"));

        redisClient.set("mykey", "cool");
        assertNotEquals("cloud", redisClient.get("mykey"));
        assertEquals("cool", redisClient.get("mykey"));
    }

    @Test
    void del() {
        Redis redisClient = new Redis();

        assertEquals(0, redisClient.del("mykey"));

        redisClient.set("mykey1", "cloud");
        redisClient.set("mykey2", "cool");
        redisClient.set("mykey3", "awesome");
        assertEquals(2, redisClient.del("mykey1", "mykey3"));

        assertNotEquals(0, redisClient.del("mykey2"));
    }

    @Test
    void hset() {
        Redis redisClient = new Redis();

        assertEquals(1, redisClient.hset("myhash", "field1", "apple"));
        assertEquals(0, redisClient.hset("myhash", "field1", "banana"));
        assertEquals(1, redisClient.hset("myhash1", "field1", "apple"));
        assertEquals(1, redisClient.hset("myhash1", "field2", "apple"));
    }

    @Test
    void hget() {
        Redis redisClient = new Redis();
        redisClient.hset("myhash", "field1", "apple");

        assertEquals("apple", redisClient.hget("myhash", "field1"));
        assertNull(redisClient.hget("myhash", "field2"));
    }

    @Test
    void hgetall() {
        Redis redisClient = new Redis();
        redisClient.hset("myhash", "field1", "apple");
        redisClient.hset("myhash", "field2", "banana");

        List<String> ret = redisClient.hgetall("myhash");

        assertEquals(Arrays.asList("field1", "apple", "field2", "banana"), ret);
    }

    @Test
    void llen() {
        Redis redisClient = new Redis();
        assertEquals(0, redisClient.llen("mylist"));
    }

    @Test
    void rpush() {
        Redis redisClient = new Redis();
        assertEquals(1, redisClient.rpush("mylist", new String[]{"apple"}));
        assertEquals(3, redisClient.rpush("mylist", new String[]{"b", "c"}));
    }

    @Test
    void rpop() {
        Redis redisClient = new Redis();
        redisClient.rpush("mylist", new String[]{"a", "b", "c"});
        assertEquals("c", redisClient.rpop("mylist"));
        assertEquals("b", redisClient.rpop("mylist"));
        assertEquals("a", redisClient.rpop("mylist"));
        assertNull(redisClient.rpop("mylist"));
    }
}