/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    
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
    
    @Test
    /* Testing strategy 
     * 
     * Partition the inputs as follows: 
     * (1) tweets.length() == 2, > 2.  
     * (2) If tweets.length()>=2 and the elements in tweets are in order (tweets[i] < tweets[j] for all i<j), 
     * partition tweets as: 
     * 		(2.1) (tweets[i]-tweets[i+1]) == (tweets[i+1]-tweets[i+2]) for all i >= 0, and 
     *      (2.2) (tweets[i]-tweets[i+1]) != (tweets[i+1]-tweets[i+2]) for all i >= 0
     * (3) Otherwise, permute the elements in tweets for each test case in (2), 
     * and check if the results are the same as their counterparts of (2).   
     */
    public void testGetTimespan() {
    	// tweets.length() == 2: 
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        
        // tweets.length() == 3: 
        // tweets is in order with partition (2.1): 
        Instant d3 = Instant.parse("2016-02-17T12:00:00Z"); 
        Tweet tweet3 = new Tweet(3, "jeffrey", "Today I am so happy.", d3);
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2,tweet3));
        assertTrue((d1==timespan.getStart()&&d2==timespan.getEnd())||(d2==timespan.getStart()&&d3==timespan.getEnd()));
        // Permuted: 
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3,tweet2));
        assertTrue((d1==timespan.getStart()&&d2==timespan.getEnd())||(d2==timespan.getStart()&&d3==timespan.getEnd()));
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1,tweet3));
        assertTrue((d1==timespan.getStart()&&d2==timespan.getEnd())||(d2==timespan.getStart()&&d3==timespan.getEnd()));
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3,tweet1));
        assertTrue((d1==timespan.getStart()&&d2==timespan.getEnd())||(d2==timespan.getStart()&&d3==timespan.getEnd()));
        timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1,tweet2));
        assertTrue((d1==timespan.getStart()&&d2==timespan.getEnd())||(d2==timespan.getStart()&&d3==timespan.getEnd()));
        timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet2,tweet1));
        assertTrue((d1==timespan.getStart()&&d2==timespan.getEnd())||(d2==timespan.getStart()&&d3==timespan.getEnd()));
        // tweets is in order with partition (2.2):
        d3 = Instant.parse("2016-02-17T13:00:00Z"); 
        tweet3 = new Tweet(3, "jeffrey", "Today I am so happy.", d3);
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2,tweet3));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        // Permuted
        timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3,tweet2));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1,tweet3));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3,tweet1));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1,tweet2));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
        timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet2,tweet1));
        assertTrue(d1==timespan.getStart()&&d2==timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test 
    /* Testing strategy
     * 
     * Partition the inputs as follows:
     * 1) total mentioned user = (or <) the sum of mentioned users number of each tweets
     * 2) tweet.text with 0 or >0 mentioned users
     * 3) tweets.length() = 1, >1  
     *  
     */
    public void testGetMentionedUsers() {
    	/*
    	 * total mentioned user = the sum of mentioned users numbers of each tweet
    	 */
    	// 2): tweet.text with 0 mentioned users. 3):  tweets.length() = 1.
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	// 2): tweet.text with 0 mentioned users. 3):  tweets.length() > 1. 
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1,tweet2));
    	assertTrue("expected empty set", mentionedUsers.isEmpty());
    	// 2): tweet.text with >0 mentioned users. 3):  tweets.length() = 1. 
    	Tweet tweet1_with_mentioned_user =  new Tweet(1, "alyssa", "is it reasonable @alyssa to talk about rivest so much?", d1);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user));
    	Set<String> expected_result = new HashSet<>();
    	expected_result.add("@alyssa"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	// 2): tweet.text with >0 mentioned users. 3):  tweets.length() > 1
    	Tweet tweet2_with_mentioned_user =  new Tweet(2, "bbitdiddle", "rivest @bbitdiddle talk in 30 minutes #hype", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user,tweet2_with_mentioned_user));
    	expected_result = new HashSet<>();
    	expected_result.add("@alyssa");expected_result.add("@bbitdiddle"); 
    	assertEquals("expected set",expected_result, mentionedUsers);
    	/*
    	 * total mentioned user < the sum of mentioned users numbers of each tweet
    	 */
    	// In the case of 2): tweet.text with >0 mentioned users. 3):  tweets.length() > 1
    	// We use a unit user mentioned name : 
    	tweet2_with_mentioned_user =  new Tweet(2, "bbitdiddle", "rivest @alyssa talk in 30 minutes #hype", d2);
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1_with_mentioned_user,tweet2_with_mentioned_user));
    	expected_result = new HashSet<>();
    	expected_result.add("@alyssa"); 
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
