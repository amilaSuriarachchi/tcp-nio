package cs.colostate.edu.tcp;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/2/15
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileProcessor {

    public static void main(String[] args) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/amila/csu-msc/research/projects/tcp/graphs/failure.data"));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/home/amila/csu-msc/research/projects/tcp/graphs/new_failure.data"));
            String nextLine = null;
            double throughput;

            while ((nextLine = bufferedReader.readLine()) != null) {
                throughput = Double.parseDouble(nextLine.split("\t")[1]);
                String line = nextLine.split("\t")[0] + "\t" + Math.log10(throughput * 46 * 8 / 1048576) + "\n";
                bufferedWriter.write(line);

            }

            bufferedReader.close();
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
