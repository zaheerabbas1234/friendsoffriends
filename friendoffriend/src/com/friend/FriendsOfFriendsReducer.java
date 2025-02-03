package com.friend;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FriendsOfFriendsReducer extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Set<String> friendsOfFriends = new HashSet<>();
        @SuppressWarnings("unused")
		Set<String> directFriends = new HashSet<>();
        Set<String> personFriends = new HashSet<>();

        // Collect all friends of key (the friend)
        for (Text val : values) {
            String valStr = val.toString();
            if (valStr.startsWith("friend:")) {
                // If it's a direct friend, add to directFriends set
                personFriends.add(valStr.substring(7));
            } else {
                // Otherwise, it's a friend of friend
                friendsOfFriends.add(valStr);
            }
        }

        // Now, remove the direct friends and the person itself from friends of friends
        friendsOfFriends.removeAll(personFriends);
        friendsOfFriends.remove(key.toString()); // Ensure the person is not considered as friend of themselves

        // Emit friends of friends
        for (String friendOfFriend : friendsOfFriends) {
            result.set(friendOfFriend);
            context.write(key, result);
        }
    }
}
