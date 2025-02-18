package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            // If no tweets, return a timespan with the current instant
            Instant now = Instant.now();
            return new Timespan(now, now);
        }
        
        // Initialize min and max timestamps
        Instant start = tweets.get(0).getTimestamp();
        Instant end = start;

        // Iterate through the list of tweets to find the earliest and latest timestamps
        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (timestamp.isBefore(start)) {
                start = timestamp;
            }
            if (timestamp.isAfter(end)) {
                end = timestamp;
            }
        }

        // Return a timespan from the earliest to the latest tweet
        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
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
        Set<String> mentionedUsers = new HashSet<>();

        // Define a regex pattern to extract valid mentions
        String regex = "(?<![A-Za-z0-9_-])@([A-Za-z0-9_-]+)";
        Pattern pattern = Pattern.compile(regex);

        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            Matcher matcher = pattern.matcher(text);

            // Find all mentions in the tweet
            while (matcher.find()) {
                String mention = matcher.group(1).toLowerCase();  // Case-insensitive
                mentionedUsers.add(mention);
            }
        }

        return mentionedUsers;
    }

}
