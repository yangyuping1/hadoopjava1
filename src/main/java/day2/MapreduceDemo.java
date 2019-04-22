package day2;

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

import java.io.IOException;

public class MapreduceDemo {
    /**
     * 1.LongWritable定义下标
     */
    //mapper端
    public static class MapTask extends Mapper<LongWritable, Text,Text, IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split(" ");
            for (String word : splits) {
                context.write(new Text(word),new IntWritable(1));
            }
        }
    }
    //reduced端
    public static class ReducerTask extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable words : values) {
                count++;
            }
            context.write(key,new IntWritable(count));
        }

        public static void main(String[] args) throws Exception {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf);
            //设置类型
            job.setMapperClass(MapTask.class);
            job.setReducerClass(ReducerTask.class);
            job.setJarByClass(MapreduceDemo.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job,new Path("D:\\111\\wa.txt"));
            FileOutputFormat.setOutputPath(job,new Path("D:\\add"));

            boolean b = job.waitForCompletion(true);
            System.out.println(b?"123":"456");

        }
    }
}
