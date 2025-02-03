package com.friend;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FriendsOfFriendsMapper extends Mapper<Object, Text, Text, Text> {
    private Text person = new Text();
    private Text friend = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] parts = line.split("\t");

        // Ensure line has enough parts to split
        if (parts.length < 2) {
            return;  // skip lines that do not match expected format
        }

        // Person and their direct friends list
        String currentPerson = parts[0];
        String[] friends = parts[1].split(",");

        // Emit reverse friend relationship
        for (String f : friends) {
            person.set(f);
            friend.set(currentPerson);
            context.write(person, friend);
        }

        // Emit direct friend relationships
        for (String f : friends) {
            person.set(currentPerson);
            friend.set(f);
            context.write(person, new Text("friend:" + f));  // Mark direct friends
        }
    }
}
