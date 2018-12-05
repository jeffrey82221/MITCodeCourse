/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.time.Instant;
import java.util.Collections; 
import java.util.Comparator;
import java.time.Duration;
import java.lang.Character;
import java.lang.StringBuilder;
/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
    	Instant smallestTimestamp = tweets.get(0).getTimestamp(); 
		Instant largestTimestamp = tweets.get(0).getTimestamp(); 
		for(Tweet tweet: tweets) {
			if(tweet.getTimestamp().isBefore(smallestTimestamp)) {
				smallestTimestamp = tweet.getTimestamp();
			}
			else if(tweet.getTimestamp().isAfter(largestTimestamp)) { // else if instead of if
				largestTimestamp = tweet.getTimestamp(); 
			}
		}
		return new Timespan(smallestTimestamp, largestTimestamp); 
    }
    /**
     * Check if a char follows the rule of valid chars: 
     *  (all characters in author are drawn from {A..Z, a..z, 0..9, _, -})
     */
    private static boolean isUserNameValid(char ch) {
    	return Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ch == '-';
    }
    /**
     * Identify MentionedUsers for a tweet.text
     */
    private static Set<String> getMentionedUsersFromText(String text) {
    	Set<String> usernameSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    	int i = 0;
    	while(i < text.length()){
    	    if(text.charAt(i) == '@'){
    	    	if(i>0 && isUserNameValid(text.charAt(i-1))){ // not a beginning of username
    	    		i++;
    	    		continue; // continue to find the beginning 
    	    	}
    	    	StringBuilder username = new StringBuilder(""); 
    	    	//username.append(text.charAt(i));//add @ into name 
    	    	i++;
    	    	while(i<text.length()&&isUserNameValid(text.charAt(i))) {
    	    		username.append(text.charAt(i));
    	    		i++;
    	    	}
    	    	usernameSet.add(username.toString());
    	    }
    	    i++;
    	}
    	return usernameSet;
    }
    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	//throw new RuntimeException("not implemented");
    	final Set<String> usernameSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);// Merging of usernames should consider case-insensitiveness.
    	for(Tweet tweet:tweets) {
    		//System.out.println(tweet.getText());
    		usernameSet.addAll(getMentionedUsersFromText(tweet.getText()));
    	}
    	return usernameSet;
    }

}
