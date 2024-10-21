/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class SocialNetworkTest {

    @Test
    public void testGuessFollowsGraphEmptyTweets() {
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowsGraphNoMentions() {
        Tweet tweet = new Tweet(1, "user1", "Hello world!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("user1"));
        assertTrue(followsGraph.get("user1").isEmpty());
    }

    @Test
    public void testGuessFollowsGraphSingleMention() {
        Tweet tweet = new Tweet(2, "user1", "Hello @user2!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("user1"));
        assertTrue(followsGraph.get("user1").contains("user2"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        Tweet tweet = new Tweet(3, "user1", "Hello @user2 @user3!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.get("user1").contains("user2"));
        assertTrue(followsGraph.get("user1").contains("user3"));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweetsSameUser() {
        Tweet tweet1 = new Tweet(4, "user1", "Hello @user2!", Instant.now());
        Tweet tweet2 = new Tweet(5, "user1", "Goodbye @user3!", Instant.now());
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.get("user1").contains("user2"));
        assertTrue(followsGraph.get("user1").contains("user3"));
    }

    @Test
    public void testInfluencersEmptyGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user2")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("user2", influencers.get(0));
    }

    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user2", "user3")));
        followsGraph.put("user2", new HashSet<>(Arrays.asList("user3")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("user3", influencers.get(0));
        assertEquals("user2", influencers.get(1));
    }

    @Test
    public void testInfluencersTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user2")));
        followsGraph.put("user3", new HashSet<>(Arrays.asList("user2")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("user2", influencers.get(0));
    }
}
