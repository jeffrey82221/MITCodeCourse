/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2); 
    private static final Tweet tweet3 = new Tweet(3, "jeffrey", "Today I am so happy.", d3);
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    
    /* Testing strategy 
     * 
     * Partition the inputs as follows: 
     * (1) tweets.length() == 1, 2, > 2.  
     * (2) For tweets.length()>=1, set the elements in tweets in order (tweets[i] < tweets[j] for all i<j , 
     *     or  permute the elements.  
     */
    @Test
    public void testGetTimespan() {
    	// tweets.length() == 1: 
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
    	assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
        // tweets.length() == 2: 
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
        // tweets.length() == 3: 
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertTrue(d1==timespan.getStart()&&d3==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3,tweet2));
        assertTrue(d1==timespan.getStart()&&d3==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1,tweet3));
        assertTrue(d1==timespan.getStart()&&d3==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3,tweet1));
        assertTrue(d1==timespan.getStart()&&d3==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1,tweet2));
        assertTrue(d1==timespan.getStart()&&d3==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet2,tweet1));
        assertTrue(d1==timespan.getStart()&&d3==timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    /* Testing strategy
     * 
     * Partition the inputs as follows:
     * 1) total mentioned user = (or <) the sum of mentioned users number of each tweets
     * 2) tweet.text with 0 or >0 mentioned users
     * 3) tweets.length() = 1, >1
     * 4) Multiple usernames with different case type (upper or lower), or not.  
     * 5) The @ is preceded with a valid username character, e.g.,"jeffrey@gmail.com", or not.
     * 6) Test case with text start and end with username.
     * 7) Username preceding with different kind of charater, (1) A-Z, (2) a-z, (3) 1-9, (4) - , (5) _
     */
    @Test 
    public void testGetMentionedUsers() {
    	/*
    	 * 1-1) total mentioned user = the sum of mentioned users numbers of each tweet
    	 */
    	// 2-1): tweet.text with 0 mentioned users.   3-1):  tweets.length() = 1.
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	// 2-1): tweet.text with 0 mentioned users.   3-2):  tweets.length() > 1. 
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1,tweet2));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	// 2-2): tweet.text with >0 mentioned users.  3-1):  tweets.length() = 1. 
    	Tweet tweet1_with_mentioned_user =  new Tweet(1, "alyssa", "is it reasonable @alyssa to talk about rivest so much?", d1);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user));
    	Set<String> expected_result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    	expected_result.add("@alyssa"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	// 2-2): tweet.text with >0 mentioned users.  3-2):  tweets.length() > 1
    	Tweet tweet2_with_mentioned_user =  new Tweet(2, "bbitdiddle", "rivest @bbitdiddle talk in 30 minutes #hype", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user,tweet2_with_mentioned_user));
    	expected_result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    	expected_result.add("@alyssa");expected_result.add("@bbitdiddle"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	/*
    	 * 1-2) total mentioned user < the sum of mentioned users numbers of each tweet
    	 */
    	// Here, we use one unit user mentioned name only but with the sum of mentioned users numbers of each tweet = 2
    	// This setting is same as the above case with tweet.text with >0 mentioned users, and tweets.length() > 1.
    	tweet2_with_mentioned_user =  new Tweet(2, "bbitdiddle", "rivest @alyssa talk in 30 minutes #hype", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user,tweet2_with_mentioned_user));
    	expected_result = new TreeSet<>();
    	expected_result.add("@alyssa"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	// Test case with usernames with different cases. 
    	tweet2_with_mentioned_user =  new Tweet(2, "bbitdiddle", "rivest @alySSA talk in 30 minutes #hype", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user,tweet2_with_mentioned_user));
    	expected_result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    	expected_result.add("@alySSA"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	// Test case with usernames with different cases. 
    	Tweet tweet_with_confusing_string=  new Tweet(1, "bbitdiddle", "rivest jeffrey@gmail.com talk in 30 minutes #hype", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_confusing_string));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	// Test case with text start and end with username.   
    	Tweet tweet_with_username_as_text=  new Tweet(1, "bbitdiddle", "@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_username_as_text));
    	expected_result = new TreeSet<>();
    	expected_result.add("@happy"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	// Test case with different kind of preceding charater: 
    	Tweet tweet_with_invalid_preceding_char=  new Tweet(1, "bbitdiddle", "1@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_invalid_preceding_char));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	tweet_with_invalid_preceding_char=  new Tweet(1, "bbitdiddle", "_@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_invalid_preceding_char));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	tweet_with_invalid_preceding_char=  new Tweet(1, "bbitdiddle", "-@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_invalid_preceding_char));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	tweet_with_invalid_preceding_char=  new Tweet(1, "bbitdiddle", "j@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_invalid_preceding_char));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	tweet_with_invalid_preceding_char=  new Tweet(1, "bbitdiddle", "J@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_with_invalid_preceding_char));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	Tweet tweet_without_invalid_preceding_char=  new Tweet(1, "bbitdiddle", ".@happy", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet_without_invalid_preceding_char));
    	expected_result = new TreeSet<>();
    	expected_result.add("@happy"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
