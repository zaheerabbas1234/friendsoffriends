package com.friend;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FriendsOfFriendsDriver {
    public static void main(String[] args) throws Exception {
        // Check arguments
        if (args.length != 2) {
            System.err.println("Usage: FriendsOfFriendsDriver <input path> <output path>");
            System.exit(-1);
        }

        // Configuration setup
        Configuration conf = new Configuration();

        // Create a job
        Job job = Job.getInstance(conf, "Friends of Friends");
        job.setJarByClass(FriendsOfFriendsDriver.class);

        // Set the Mapper and Reducer classes
        job.setMapperClass(FriendsOfFriendsMapper.class);
        job.setReducerClass(FriendsOfFriendsReducer.class);

        // Set the output types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Set the input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Run the job
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
