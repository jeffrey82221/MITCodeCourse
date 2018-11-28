/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    /**
     * Test Startegy:
     * Input is partitioned as follows:
     * 1) number of node in the graph. (number of different usernames).   
     * 2) tweets.length() = 0, 1, 2, > 2 
     * 3) consider different type of links between any two tweets, A and B, as follows: 
     *     1) "A being followed by B" 2) "A following B", or 3) "no relationshiop". 
     * 
     * Postcondition of followsGraph:
     * 1) followsGraph[A] is the set of people that person A follows on Twitter. 
     * 2) Each element of set A is represented by Twitter usernames, which are case insensitive. 
     * 3) If A doesn't follow anybody, then map[A] may be the empty set, or A may not even exist as a key in the map. 
     * 4) A username should appear at most once as a key in the map or in any given map[A] set. (??? what if A->B and B->A.).  
     */
    @Test
    public void testGuessFollowsGraph() {
    	/*
    	 * #Node = 0 , tweets.length() = 0
    	 */
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
        /*
    	 * #Node = 1,  
    	 */        
        // tweets.length() = 1 
        Tweet tweet1 = new Tweet(1, "alyssa", "This is me. ", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        assertTrue("expected graph with only one node", followsGraph.keySet().size()==1);
        assertTrue("expected graph with only one node, alyssa.", followsGraph.containsKey("alyssa"));
        assertEquals("no followers", 0, followsGraph.get(tweet1.getAuthor()).size());
        // tweets.length() = 2 
        Tweet tweet2 = new Tweet(2, "alyssa", "Today I am happy.", Instant.parse("2016-02-17T10:00:00Z")); 
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2)); 
        assertTrue("expected graph with only one node", followsGraph.keySet().size()==1);
        assertTrue("expected graph with only one node, alyssa.", followsGraph.containsKey("alyssa"));
        assertEquals("no followers", 0, followsGraph.get(tweet1.getAuthor()).size());
        /*
    	 * Node = 2; (Suppose they are A and B);       
    	 */  
        // A follows B. tweets.length() = 1 ,  
        tweet1 = new Tweet(1, "alyssa", "This is me, and my friend, @bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1)); 
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertEquals("alyssa followers one user", 1, followsGraph.get("alyssa").size());
        // A follows B. tweets.length() = 2 ,
        tweet1 = new Tweet(1, "alyssa", "This is me, and my friend, @bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2)); 
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertEquals("alyssa followers one user", 1, followsGraph.get("alyssa").size());
        // A don't follow B, B don't follow A., tweets.length() = 2. 
        tweet1 = new Tweet(1, "alyssa", "This is me.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2)); 
        assertEquals("alyssa followers zero user", 0, followsGraph.get("alyssa").size());
        assertEquals("bobby followers zero user", 0, followsGraph.get("bobby").size());
        assertTrue("at most two keys", followsGraph.keySet().size()==2);
        // A don't follow B, B don't follow A., tweets.length() = 3. 
        tweet1 = new Tweet(1, "alyssa", "This is me.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        Tweet tweet3 = new Tweet(3, "bobby", "I am bobby, again.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3)); 
        assertEquals("alyssa followers zero user", 0, followsGraph.get("alyssa").size());
        assertEquals("bobby followers zero user", 0, followsGraph.get("bobby").size());
        assertTrue("at most two keys", followsGraph.keySet().size()==2);
        // A follows B, and B follows A. , tweets.length() = 2.
        tweet1 = new Tweet(1, "alyssa", "This is me, and this is my friend, @bobby!", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and I follow @alyssa", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("alyssa is followed by bobby.", followsGraph.get("bobby").contains("alyssa"));
        assertTrue("at most two keys", followsGraph.keySet().size()==2);
        // A follows B, and B follows A. , tweets.length() = 3.
        tweet1 = new Tweet(1, "alyssa", "This is me, and this is my friend, @bobby!", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and I follow @alyssa", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "bobby", "I am bobby, and I follow @alyssa", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("alyssa is followed by bobby .", followsGraph.get("bobby").contains("alyssa"));
        assertTrue("at most two keys", followsGraph.keySet().size()==2);
        /*
    	 * Node = 3; (Suppose they are A, B, and C);       
    	 */  
        // A, B, C not followed by any another. 
        tweet1 = new Tweet(1, "alyssa", "This is me.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        assertEquals("alyssa followers zero user", 0, followsGraph.get("alyssa").size());
        assertEquals("bobby followers zero user", 0, followsGraph.get("bobby").size());
        assertEquals("cat followers zero user", 0, followsGraph.get("cat").size());
        // A follows B, and C not followed by any another.  tweets.length() = 2
        tweet1 = new Tweet(1, "alyssa", "This is me. @bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet3));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertEquals("cat followers zero user", 0, followsGraph.get("cat").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==2);
        // A follows B, and C not followed by any another.  tweets.length() = 3
        tweet1 = new Tweet(1, "alyssa", "This is me. @bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertEquals("cat followers zero user", 0, followsGraph.get("cat").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==2);
        // A follows B and C. tweets.length() = 1. 
        tweet1 = new Tweet(1, "alyssa", "This is me, and @bobby and @cat are my friends.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("cat is followed by alyssa.", followsGraph.get("alyssa").contains("cat"));
        assertEquals("alyssa followers two user", 2, followsGraph.get("alyssa").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==1);
        // A follows B and C. tweets.length() = 2. 
        tweet1 = new Tweet(1, "alyssa", "This is me, and @bobby and @cat are my friends.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("cat is followed by alyssa.", followsGraph.get("alyssa").contains("cat"));
        assertEquals("alyssa followers two user", 2, followsGraph.get("alyssa").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==1);
        // A follows B and C. tweets.length() = 3. 
        tweet1 = new Tweet(1, "alyssa", "This is me, and @bobby and @cat are my friends.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        assertTrue("bobby is followed by alyssa.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("cat is followed by alyssa.", followsGraph.get("alyssa").contains("cat"));
        assertEquals("alyssa followers two user", 2, followsGraph.get("alyssa").size());
        assertTrue("with at most one keys", followsGraph.keySet().size()==1);
        // B and C follow A. tweets.length() = 2. 
        tweet2 = new Tweet(2, "bobby", "I am bobby, and @alyssa is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat, and @alyssa is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2,tweet3));
        assertTrue("bobby follows alyssa.", followsGraph.get("bobby").contains("alyssa"));
        assertTrue("cat follows alyssa.", followsGraph.get("cat").contains("alyssa"));
        assertEquals("bobby followers one user", 1, followsGraph.get("bobby").size());
        assertEquals("cat followers one user", 1, followsGraph.get("cat").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==2); 
        // B and C follow A. tweets.length() = 3. 
        tweet1 = new Tweet(1, "alyssa", "This is me.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and @alyssa is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat, and @alyssa is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        assertTrue("bobby follows alyssa.", followsGraph.get("bobby").contains("alyssa"));
        assertTrue("cat follows alyssa.", followsGraph.get("cat").contains("alyssa"));
        assertEquals("bobby followers one user", 1, followsGraph.get("bobby").size());
        assertEquals("cat followers one user", 1, followsGraph.get("cat").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==2);
        // A follows B, B follows C. tweets.length() = 2. 
        tweet1 = new Tweet(1, "alyssa", "This is me. @bobby. ", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and @cat is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2)); 
        assertTrue("alyssa follows bobby.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("bobby follows cat.", followsGraph.get("bobby").contains("cat"));
        assertEquals("alyssa followers one user", 1, followsGraph.get("alyssa").size());
        assertEquals("bobby followers one user", 1, followsGraph.get("bobby").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==2);
        // A follows B, B follows C. tweets.length() = 3. 
        tweet1 = new Tweet(1, "alyssa", "This is me. @bobby. ", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and @cat is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3)); 
        assertTrue("alyssa follows bobby.", followsGraph.get("alyssa").contains("bobby"));
        assertTrue("bobby follows cat.", followsGraph.get("bobby").contains("cat"));
        assertEquals("alyssa followers one user", 1, followsGraph.get("alyssa").size());
        assertEquals("bobby followers one user", 1, followsGraph.get("bobby").size());
        assertTrue("with at most two keys", followsGraph.keySet().size()==2);
        
    }
    /**
     * Test Startegy:
     * Input is partitioned as the above testing method:
     * 1) number of node in the graph. (number of different usernames).   
     * 2) tweets.length() = 0, 1, 2, > 2 
     * 3) consider different type of links between any two tweets, A and B, as follows: 
     *     1) "A being followed by B" 2) "A following B", or 3) "no relationshiop".   
     */
    @Test
    public void testInfluencers() {
    	/*
    	 * #Node = 0 , tweets.length() = 0
    	 */
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
        /*
    	 * #Node = 1,  
    	 */        
        // tweets.length() = 1 
        Tweet tweet1 = new Tweet(1, "alyssa", "This is me. ", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("one node only", influencers.contains("alyssa"));
        assertEquals("size of list", 1, influencers.size());
        // tweets.length() = 2 
        Tweet tweet2 = new Tweet(2, "alyssa", "Today I am happy.", Instant.parse("2016-02-17T10:00:00Z")); 
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("one node only", influencers.contains("alyssa"));
        assertEquals("size of list", 1, influencers.size());
        /*
    	 * Node = 2; (Suppose they are A and B);       
    	 */  
        // A follows B. tweets.length() = 1 ,  
        tweet1 = new Tweet(1, "alyssa", "This is me, and my friend, @bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("bobby is a larger influencer than alyssa", "bobby", influencers.get(0));
        assertEquals("bobby is a larger influencer than alyssa", "alyssa", influencers.get(1));
        assertEquals("size of list", 2, influencers.size());
        // A follows B. tweets.length() = 2 ,
        tweet1 = new Tweet(1, "alyssa", "This is me, and my friend, @bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("bobby is a larger influencer than alyssa", "bobby", influencers.get(0));
        assertEquals("bobby is a larger influencer than alyssa", "alyssa", influencers.get(1));
        assertEquals("size of list", 2, influencers.size());
        // B follows A. tweets.length() = 1 ,  
        tweet1 = new Tweet(1, "bobby", "This is me, and my friend, @alyssa.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("bobby is a smaller influencer than alyssa", "alyssa", influencers.get(0));
        assertEquals("bobby is a smaller influencer than alyssa", "bobby", influencers.get(1));
        assertEquals("size of list", 2, influencers.size());
        // B follows A. tweets.length() = 2 ,
        tweet1 = new Tweet(1, "bobby", "This is me, and my friend, @alyssa.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "alyssa", "I am alyssa.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("bobby is a smaller influencer than alyssa", "alyssa", influencers.get(0));
        assertEquals("bobby is a smaller influencer than alyssa", "bobby", influencers.get(1));
        assertEquals("size of list", 2, influencers.size());
        // A don't follow B, B don't follow A., tweets.length() = 2. 
        tweet1 = new Tweet(1, "alyssa", "This is me.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("size of list", 2, influencers.size());
        // A don't follow B, B don't follow A., tweets.length() = 3. 
        tweet1 = new Tweet(1, "alyssa", "This is me.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby.", Instant.parse("2016-02-17T10:00:00Z"));
        Tweet tweet3 = new Tweet(3, "bobby", "I am bobby, again.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("size of list", 2, influencers.size());
        // A follows B, and B follows A. , tweets.length() = 2.
        tweet1 = new Tweet(1, "alyssa", "This is me, and this is my friend, @bobby!", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and I follow @alyssa", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("size of list", 2, influencers.size());
        // A follows B, and B follows A. , tweets.length() = 3.
        tweet1 = new Tweet(1, "alyssa", "This is me, and this is my friend, @bobby!", Instant.parse("2016-02-17T10:00:00Z"));
        tweet2 = new Tweet(2, "bobby", "I am bobby, and I follow @alyssa", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "bobby", "I am bobby, and I follow @alyssa", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1,tweet2,tweet3));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertEquals("size of list", 2, influencers.size());
        
        /*
    	 * Node = 3; (Suppose they are A, B, and C);       
    	 */  
        
        // B and C follow A. tweets.length() = 2. 
        tweet2 = new Tweet(2, "bobby", "I am bobby, and @alyssa is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        tweet3 = new Tweet(3, "cat", "I am cat, and @alyssa is my friend.", Instant.parse("2016-02-17T10:00:00Z"));
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2,tweet3));
        influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("contain", influencers.contains("alyssa"));
        assertTrue("contain", influencers.contains("bobby"));
        assertTrue("contain", influencers.contains("cat"));
        assertEquals("bobby is a smaller influencer than alyssa", "alyssa", influencers.get(0));
        assertEquals("size of list", 3, influencers.size()); 
        
        
        
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
