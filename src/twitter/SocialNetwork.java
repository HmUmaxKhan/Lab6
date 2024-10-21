package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * SocialNetwork provides methods that operate on a social network.
 */
public class SocialNetwork {

    /**
     * Guess follows graph based on a list of tweets.
     * 
     * @param tweets list of tweets, each tweet is represented as a string and contains
     *               a username and mentions of other users in the format "@username".
     * @return a Map where each key is a user (author of the tweet), and each value is a
     *         Set of users they mention (i.e., users they are inferred to follow).
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        
        for (Tweet tweet : tweets) {
            String user = tweet.getAuthor();  // Get the author of the tweet
            Set<String> mentionedUsers = Extract.getMentionedUsers(List.of(tweet));  // Use Extract.getMentionedUsers to extract mentions
            
            followsGraph.putIfAbsent(user.toLowerCase(), new HashSet<>());  // Add user in lowercase
            followsGraph.get(user.toLowerCase()).addAll(mentionedUsers);  // Add mentioned users
        }
        
        return followsGraph;
    }

    /**
     * Influencers sorted by their number of followers.
     * 
     * @param followsGraph a social network graph where keys are users and values are the
     *                     sets of people they follow.
     * @return a list of users sorted in descending order of influence (number of followers).
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();
        
        // Count the followers for each user
        for (Set<String> followedUsers : followsGraph.values()) {
            for (String followedUser : followedUsers) {
                followerCount.put(followedUser, followerCount.getOrDefault(followedUser, 0) + 1);
            }
        }
        
        // Create a list of users, sorted by the number of followers in descending order
        List<String> influencers = new ArrayList<>(followerCount.keySet());
        influencers.sort((a, b) -> followerCount.get(b).compareTo(followerCount.get(a)));
        
        return influencers;
    }

}
