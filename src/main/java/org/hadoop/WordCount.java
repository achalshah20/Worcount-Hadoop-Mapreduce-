package org.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount{
	
	 public static void main(String args[]) throws Exception {
		 	Configuration conf = new Configuration();
		 	
		    
		 	Job job = Job.getInstance(conf, "word count by achal");
		 			 	
		 	job.setJarByClass(WordCount.class);
		 	
		    job.setMapperClass(WordCountMapper.class);
		    job.setReducerClass(WordCountReducer.class);
		    
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(IntWritable.class);
		    
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }

		 

	 public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		    private Text currentWord = new Text();
		        
		    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		    	
		    	String line = value.toString();
		    	StringTokenizer tokenizer = new StringTokenizer(line);
		    	
		    	while(tokenizer.hasMoreTokens()){
		    		currentWord.set(tokenizer.nextToken());
		    		context.write(currentWord, new IntWritable(1));
		    	}
		    	
		    }
		 } 
	 
	 
	 public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		    @Override
		    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		     int sum = 0;
		     for(IntWritable value : values){
		    	 sum += value.get();
		     }
		      context.write(key, new IntWritable(sum));
		    }
		  }
}