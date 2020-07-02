package Utils;

import models.FastaEntry;
import models.RNASingleChain;
import models.RnaNode;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ScriptsAdapter {

    public String[] ScriptName = {"RNAfold", "RNACentroid"};

    public Integer version;

    private final Config config;

    ScriptsAdapter(int ver) {
        config = new Config();
        version = ver;
    }

    /**
     * TODO
     */
    public void showFile() {
        try {
            Process p = Runtime.getRuntime().exec("python3 ");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ret = in.readLine();
            System.out.println("value is : " + ret);
            while ((ret = in.readLine()) != null) {
                System.out.println("value is : " + ret);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Use of script that extract single chains with border prom predicted struct
     *
     * @return list of single chain fragments
     */
    public ArrayList<RNASingleChain> getSingleChains() {

        ArrayList<RNASingleChain> score = new ArrayList<>();

        try {
            String command = config.python_script_path;
//            String command = "./src/rna_lines_extractor.py ";
            String arg = config.dot_bracket_file;

            Process p = Runtime.getRuntime().exec("python3 " + command + " " + arg);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            RNASingleChain tempObject = new RNASingleChain();
            String ret = in.readLine();
            int lineNumber = 1;
            while ((ret = in.readLine()) != null) {
//                System.out.println(ret);
                switch (lineNumber % 3) {
                    case 1:
                        tempObject.id = Integer.parseInt(ret.trim());
                        break;
                    case 2:
                        tempObject.sequence = ret;
                        break;
                    case 0:
                        {
                        final Pattern patern = Pattern.compile("[0-9]+");
                        CharSequence input;
                        Matcher m = patern.matcher(ret);
                        if (m.find()) {
                            int value1 = Integer.parseInt(m.group(0).trim());
                            m.find(m.end() + 1);
                            int value2 = Integer.parseInt(m.group(0).trim());
                            Integer array[] = {value1, value2};
                            tempObject.indexes = array;

//                            System.out.println(Arrays.toString(tempObject.indexes));
                        }
                        score.add(tempObject);
                        tempObject = new RNASingleChain();
                    }
                }

                lineNumber++;

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Bad luck!");
        }
//        System.out.println(score.size());
        return score;
    }

    public void predictStructure() throws IOException {


//        Process p = Runtime.getRuntime().exec("RNAfold ./src/found_pre_mirs.fasta --noPS -otemp.txt");

        createFile(config.dot_bracket_file);
        String command = "RNAfold ";
        String arg1 = " --noPS ";
        String arg2 = config.fasta_entry_file;
        String arg3 = "-o" + config.dot_bracket_file;

        Process p = Runtime.getRuntime().exec(command + arg2 + arg1 + arg3);
    }

    public void createFile(String fileName) throws IOException {

        File file = new File(fileName);

        if (file.createNewFile()) {
//            System.out.println(fileName + " File Created in Project root directory");
        } else {
//            System.out.println("File " + fileName + " already exists in the project root directory");
//            System.out.println("Trunc...");

            try (FileChannel outChan = new FileOutputStream(file, true).getChannel()) {
                outChan.truncate(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void createHelpFile(String fileName, String nChain) throws IOException {

        //  Create file to help script use
//        TODO make config with files name etc
//        String fileName = config.dot_bracket_file;

        createFile(fileName);
        File file = new File(fileName);

        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(file));
//            writer.write(nChain + "\n" + dotChain);
            writer.write(nChain + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {

            }
        }

    }

    public void createDirectory(String fileName)
    {
        File theDir = new File(fileName);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }

    public void saveOutput(FastaEntry entry_params, ArrayList<RnaNode> outputNodes, String dir)
    {
        String pathFile = dir + "/" +entry_params.name.substring(1);

        try {
            createDirectory(dir);
            createFile(pathFile);
            File file = new File(pathFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for( RnaNode node : outputNodes)
            {
                writer.write(node.toString());
            }
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("Error with saving");
            e.printStackTrace();
        }


    }


}
