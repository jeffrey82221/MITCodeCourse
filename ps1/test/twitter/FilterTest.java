/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(2, "alyssa", "cat is my favorite animal.", d3);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    /** Testing strategy
     * Partition the inputs as follows:
     * 1) A ~ tweets.length() = 1, >1, 
     * 2) B ~ number of tweets with username as author < tweets.length()
     * 3) Case(username) ==/!= Case(T.getAuthor())  for T is a tweet with username, "username".  
     */
    @Test
    public void testWrittenByMultipleTweetsSingleResultSameCase() {
    	// 1) Case(username) == Case(T.getAuthor())
    	// A = 1; B = 0;  
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "bbitdiddle");
        assertEquals("expected empty list", 0, writtenBy.size());
        // A = 1; B = 1; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "alyssa"); 
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1)); 
        // A = 2; B = 0; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet2), "cat"); 
        assertEquals("expected singleton list", 0, writtenBy.size());
        // A = 2; B = 1; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet2), "alyssa"); 
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        // A = 2; B = 2; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet3), "alyssa"); 
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweets", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
    }
    @Test
    public void testWrittenByMultipleTweetsSingleResultDifferentCase() {
        // 2) Case(username) != Case(T.getAuthor())
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "bbitdiddle");
        assertEquals("expected empty list", 0, writtenBy.size());
        // A = 1; B = 1; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "ALYSSA"); 
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1)); 
        // A = 2; B = 0; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet2), "CAT"); 
        assertEquals("expected empty list", 0, writtenBy.size());
        // A = 2; B = 1; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet2), "ALYSSA"); 
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        // A = 2; B = 2; 
        writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet3), "ALYSSA"); 
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweets", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
        
    }
    
    /** Testing strategy
     * Partition the inputs as follows:
     * 1) A ~ tweets.length() = 1, >1 
     * 2) B ~ number of tweets with timestamp within the Timespan = 0, 1, or tweets.length(). 
     * 3) C ~ number of tweets before the Timespan. 
     * 4) D ~ number of tweets after the Timespan. 
     * Precondition: B + C + D = A . 
     * My Test Cases: 
     *  - For A = 0: B, C, D = 0; 
     *  - For A = 1: B = 1, C, D = 0 ; C = 1, B, D = 0 ; D = 1, B, C = 0 ;
     *  - For A = 2: B = 2, C, D = 0 ; C = 2, B, D = 0 ; D = 2, B, C = 0 ; 
     *               B = 0, C, D = 1 ; C = 0, B, D = 1 ; D = 0, B, C = 1 ;      
     */
    // Setup the testing boundaries for "Timespan".  
    private static final Instant bound1 = Instant.parse("2016-02-16T09:00:00Z"); 
    private static final Instant bound2 = Instant.parse("2016-02-17T10:30:00Z"); 
    private static final Instant bound3 = Instant.parse("2016-02-17T11:30:00Z");
    private static final Instant bound4 = Instant.parse("2016-02-18T11:30:00Z");
    @Test
    public void testInTimespanZeroTweetMultipleResults() {
        // NOTE: bound1 < tweet1.getTimestamp() < bound2 < tweet2.getTimestamp() < bound3 < tweet3.getTimestamp() < bound4
        /*
         * A = 0; B, C, D = 0;
         */
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(bound1, bound2));
        assertTrue("expected non-empty list", inTimespan.isEmpty());
    } 
    @Test
    public void testInTimespanOneTweetMultipleResults() {
        /*
         * A = 1;
         */
        // B = 1, C and D = 0;  
    	List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(bound1, bound2));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1)));
        // B = 0, one of C or D = 1;   
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(bound1, bound1));
        assertTrue("expected non-empty list", inTimespan.isEmpty());
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(bound2, bound2));
        assertTrue("expected non-empty list", inTimespan.isEmpty());
    } 
    @Test
    public void testInTimespanTwoTweetsMultipleResults() {
        /*
         * A = 2;
         */
        // B = 0, one of C or D = 2;   
    	List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(bound1, bound1));
        assertTrue("expected non-empty list", inTimespan.isEmpty());
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(bound3, bound3));
        assertTrue("expected non-empty list", inTimespan.isEmpty());
        // B = 0, C and D = 1;  
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(bound2, bound2));
        assertTrue("expected non-empty list", inTimespan.isEmpty());
        // B = 1, one of C or D = 1, and the other = 0;   
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(bound1, bound2));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1)));
        
        
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(bound2, bound3));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet2)));
        // B = 2, C and D = 0; 
        	/* 
        	 * (At the same time, check if the result is in the same order of the input tweets.)    
        	 */
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(bound1, bound3));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1)); // Check order
        	/*
        	 * (Re-order the input list and check the order again. )   
         	 */
        inTimespan = Filter.inTimespan(Arrays.asList(tweet2, tweet1), new Timespan(bound1, bound3));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2))); 
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet2)); // Check order
    }
    @Test
    public void testInTimespacTweetatTimespanBoundary() {
    	// Tweet at bound 1 or bound 2 
        Tweet tweet1_new = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", bound1);
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1_new, tweet2), new Timespan(bound1, bound2));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1_new)));
        tweet1_new = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", bound2);
        inTimespan = Filter.inTimespan(Arrays.asList(tweet1_new, tweet2), new Timespan(bound1, bound2));
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1_new)));
    }
    /** Testing strategy
     * 
     * Partition the inputs as follows:
     * 1) A ~ tweets.length() = 0, 1, >1. 
     * 2) B ~ words.length() = 0, 1, >1.
     * 3) C ~ tweet.text.size() = 0, 1, > 1. for each tweet.   
     * 3) D ~ Number of tweets containing at least one of the word in words. (D<=A) 
     * 4) Case(matched word in "words" list) ==/!= Case(matched word in "tweet.text") 
     * 
     * Examples of Test: 
     * A = 0; XXX B, C => don't care. 
     * A = 1; 
     * e.g. of tweet.text and words:  
     * - for words == [] (B = 0); XXX tweet.text => don't care.   
     * - for words == ["x"] (B = 1): tweet.text = "x" , "xy", " x", "x " (C = 1, 2)
     * - for words == ["x","y"] (B = 2): tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y " (C = 2, 3, 4) 
     * - for tweet.text == "" (C = 0) => XXX words => don't care. 
     * - for tweet.text == " " (C = 1) => words => don't care. 
     * - for tweet.text == "x" (C = 1) => words ["x"],["y"]. 
     * - for tweet.text == "xy" (C = 2) => words ["xy"], ["x"]
     * - for tweet.text == "x " (C = 2) => words ["x"] 
     * - for tweet.text == " x" (C = 2) => words ["x"]
     * - for tweet.text == " x " (C = 2) => words ["x"]
     * - for tweet.text == " xy" (C = 3) => words ["xy"]
     * - for tweet.text == "x y" (C = 3) => words ["x"], ["y"]  
     * A = 2; both tweet[1].text and tweet[2].text are the same as above ; only one of tweet[1].text or tweet[2].text is the same as above
     *   
     */
    @Test
    public void testEmptyTweetContaining() {
    	/*
    	 * tweets.length() = 0
    	 */
        List<Tweet> containing = Filter.containing(Arrays.asList(), Arrays.asList("x","y"));
        assertTrue("expected empty list", containing.isEmpty());
    } 
    @Test
    public void testOneTweetContaining() {
        /*
    	 * tweets.length() = 1
    	 */
        // for words == []
        List<String> words = Arrays.asList();
        Tweet tweet_test = new Tweet(1, "alyssa", "any words here", d1); 
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected empty list", containing.isEmpty());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test), words);
        assertTrue("expected empty list", containing.isEmpty());
        // for words == ["x"]
        words = Arrays.asList("x");
        // tweet.text = "x" , "xy", " x", "x "  
        tweet_test = new Tweet(1, "alyssa", "x", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));       
        tweet_test = new Tweet(1, "alyssa", "xy", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected empty list", containing.isEmpty());
        tweet_test = new Tweet(1, "alyssa", " x", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "x ", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        // for words == ["x", "y"]
        words = Arrays.asList("x","y");
        // tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y "  
        tweet_test = new Tweet(1, "alyssa", "xy", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected empty list", containing.isEmpty());
        tweet_test = new Tweet(1, "alyssa", "x y", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "y x", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", " x y", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "x y ", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", " x y ", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        // for words == ["xy"]
        words = Arrays.asList("xy");
        // tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y "  
        tweet_test = new Tweet(1, "alyssa", "xy", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "xy ", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", " xy", d1); 
        containing = Filter.containing(Arrays.asList(tweet_test), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
    }
    @Test
    public void testTwoTweetsContaining() {
        /*
    	 * tweets.length() = 2 , and both tweet1.text and tweet2.text are in result.  
    	 */
        // for words == ["x"]
    	List<String> words = Arrays.asList("x");
        // tweet.text = "x" , "xy", " x", "x "  
    	Tweet tweet_test = new Tweet(1, "alyssa", "x", d1); 
        Tweet tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp()); 
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));       
        tweet_test = new Tweet(1, "alyssa", "xy", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected empty list", containing.isEmpty());
        tweet_test = new Tweet(1, "alyssa", " x", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", "x ", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        // for words == ["x", "y"]
        words = Arrays.asList("x","y");
        // tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y "  
        tweet_test = new Tweet(1, "alyssa", "xy", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected empty list", containing.isEmpty());
        tweet_test = new Tweet(1, "alyssa", "x y", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", "y x", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", " x y", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", "x y ", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", " x y ", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        // for words == ["xy"]
        words = Arrays.asList("xy");
        // tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y "  
        tweet_test = new Tweet(1, "alyssa", "xy", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", "xy ", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        tweet_test = new Tweet(1, "alyssa", " xy", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), tweet_test.getText(), tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test,tweet_test2)));
        /*
    	 * tweets.length() = 2 , and but only one of tweet1.text and tweet2.text is in result.  
    	 */
        // for words == ["x"]
        words = Arrays.asList("x");
        // tweet.text = "x" , "xy", " x", "x "  
        tweet_test = new Tweet(1, "alyssa", "x", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp()); 
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));      
        tweet_test = new Tweet(1, "alyssa", "xy", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected empty list", containing.isEmpty());
        tweet_test = new Tweet(1, "alyssa", " x", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "x ", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        // for words == ["x", "y"]
        words = Arrays.asList("x","y");
        // tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y "  
        tweet_test = new Tweet(1, "alyssa", "xy", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected empty list", containing.isEmpty());
        tweet_test = new Tweet(1, "alyssa", "x y", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "y x", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", " x y", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "x y ", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", " x y ", d1);
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        // for words == ["xy"]
        words = Arrays.asList("xy");
        // tweet.text =  "xy", "x y", "y x", " x y", "x y ", " x y "  
        tweet_test = new Tweet(1, "alyssa", "xy", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", "xy ", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        tweet_test = new Tweet(1, "alyssa", " xy", d1); 
        tweet_test2 = new Tweet(2, tweet_test.getAuthor(), "abc", tweet_test.getTimestamp());
        containing = Filter.containing(Arrays.asList(tweet_test,tweet_test2), words);
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet_test)));
        ///////////////////
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
