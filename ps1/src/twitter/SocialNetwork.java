/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear (at most once as a key in the map) or (in any given
 * map[A] set).
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
    	final Map<String, Set<String>>  followsGraph = new TreeMap<String, Set<String>>();
    	Set<String> userSetOfTweet;
    	String authorName;
    	for(Tweet tweet: tweets) {
    		userSetOfTweet = (TreeSet<String>) Extract.getMentionedUsers(Arrays.asList(tweet));
    		authorName = tweet.getAuthor();
    		if(followsGraph.containsKey(authorName)) {
    			userSetOfTweet.addAll(followsGraph.get(authorName));
    			followsGraph.put(authorName, userSetOfTweet);
    		}else {
    			followsGraph.put(authorName, userSetOfTweet);
    		}
    	}
    	// Because of the precondition -- "A username should appear at most once as a key in the map or in any given
    	// map[A] set", we consider a procedure as follows:
    	// If a key have empty set, check if the key is in the mentioned username list. If so, remove it from keys.
    	Set<String> allMentionedUsers = (TreeSet<String>) Extract.getMentionedUsers(tweets);
    	Set<String> userKeySet = new TreeSet<String>(followsGraph.keySet());
    	for(String key: userKeySet) {
    		if(allMentionedUsers.contains(key) && followsGraph.get(key).size()==0) {
    			followsGraph.remove(key);
    		}
    	}
    	return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        //throw new RuntimeException("not implemented");
    	final List<String> influencerList = new ArrayList<String>();
    	Map<String,Integer> influenceOfUser = new TreeMap<String,Integer>(); 
    	for(String user: followsGraph.keySet()) {
    		for(String followedUser: followsGraph.get(user)){
    			if(influenceOfUser.containsKey(followedUser)){
    				influenceOfUser.put(followedUser, influenceOfUser.get(followedUser)+1);
    			}else {
    				influenceOfUser.put(followedUser, 1); 
    			}
    		}
    	}
    	// add keys  
    	for(String user: followsGraph.keySet()) {
    		if(!influenceOfUser.containsKey(user)) {
    			influenceOfUser.put(user,0);
    		}
    	}
    	// sort the Map by values: 
    	Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                // TODO Auto-generated method stub
                return o2.getValue()-o1.getValue();
            }
        };
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(influenceOfUser.entrySet());
        Collections.sort(list,valueComparator);
        for (Map.Entry<String, Integer> entry : list) {
        	influencerList.add(entry.getKey());
        }
    	return influencerList;
    	
    }

}
