package utils;

import models.FastaEntry;
import models.RNASingleChain;
import models.RnaNode;
import models.SequenceContainer;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class with functions used to contact with system
 * <ul>
 * <li> Loading and reading files</li>
 * <li> Creating necessary files and cleaning them </li>
 * <li> Saving results </li>
 * <li> Using outsourced functions </li>
 * </ul>
 */
public class RuntimeAdapter {


    private final Config config;

    public RuntimeAdapter(Config _config) {
        config = _config;

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

            Process p;
            if(config.version == 0)
            {
                p = Runtime.getRuntime().exec("python3 " + command + " " + arg + " -v 0" );
            }
            else
            {
                p = Runtime.getRuntime().exec("python3 " + command + " " + arg + " -v 1");
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            RNASingleChain tempObject = new RNASingleChain();
//            It reads first line and jumps to sec
//            Pozwala przeskoczyć pierwszą linie
            String ret = in.readLine();
            int lineNumber = 1;
//            Przetwarzanie wyjścia ze skryptu do obiektu
            while ((ret = in.readLine()) != null) {
//                System.out.println(ret);
                switch (lineNumber % 3) {
                    case 1:
                        tempObject.id = Integer.parseInt(ret.trim());
                        break;
                    case 2:
                        tempObject.sequence = ret;
                        break;
                    case 0: {
                        final Pattern patern = Pattern.compile("[0-9]+");
                        CharSequence input;
                        Matcher m = patern.matcher(ret);
                        if (m.find()) {
                            int value1 = Integer.parseInt(m.group(0).trim());
                            m.find(m.end() + 1);
                            int value2 = Integer.parseInt(m.group(0).trim());
                            tempObject.indexes = new Integer[]{value1, value2};

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
            System.out.print("ERROR with python script procesing\n");
        }
//        System.out.println(score.size());
        return score;
    }

    /**
     * Wywołanie predykcji struktury drugorzędowej , dostępne dwie wersje
     * @throws IOException
     */
    public void predictStructure() throws IOException {


//        Process p = Runtime.getRuntime().exec("RNAfold ./src/found_pre_mirs.fasta --noPS -otemp.txt");


        createFile(config.dot_bracket_file);
        if (config.version == 1) {
            String command = "RNAfold ";
            String arg1 = config.fasta_entry_file;
            String arg2 = " --noPS ";
            String arg3 = "-o" + config.dot_bracket_file;
            Process p = Runtime.getRuntime().exec(command + arg1 + arg2 + arg3);

            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("finishing prediction");
            }



        } else if (config.version == 0) {
            String command = "java -cp ";
            String arg1 = config.contextFoldPath + "bin ";
            String arg2 = "in:" + config.fasta_entry_file;
            String arg3 = " out:" + config.dot_bracket_file;
            String arg4 = "contextFold.app.Predict ";
            String arg5 = " model:" + config.contextFoldPath + "trained/StHighCoHigh.model";

//            System.out.println(command + arg1 + arg4+ arg2 + arg3 + arg5);

            Process p = Runtime.getRuntime().exec(command + arg1 + arg4+ arg2 + arg3 + arg5);

            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("finishing prediction");
            }
//            String line;
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(p.getInputStream()) );
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
        }
    }

    /**
     * Tworzenie jakiegokolwiek pustego pliku
     * @param fileName
     * @throws IOException
     */
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

    /**
     * Stworzenie pliku zawierającego sekwencje do przetworzenia dla skryptu pythonowego
     * @param fileName
     * @param nChain
     * @throws IOException
     */
    public void createHelpFile(String fileName, String nChain) throws IOException {

        createFile(fileName);

        File file = new File(fileName);

        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(nChain + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void createDirectory(String fileName) {
        File theDir = new File(fileName);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
    }

    /**
     * Metoda do zapisu wyniku
     * @param entry_params
     * @param outputNodes
     * @param dir
     * @param subtitle
     */
    public void saveOutput(FastaEntry entry_params, ArrayList<RnaNode> outputNodes, String dir, String subtitle) {
        String pathFile = dir + "/" + entry_params.name.substring(1) + "/" + entry_params.name.substring(1) + subtitle;

        try {
            createDirectory(dir);
            createDirectory(dir + "/" + entry_params.name.substring(1));

            createFile(pathFile);
            File file = new File(pathFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

//            writer.write(entry_params.name + "\n");
//            writer.write(entry_params.chain + "\n");

            writer.write("[" + "\n");

            boolean flag = false;
            for (RnaNode node : outputNodes) {
                if (flag) {
                    writer.write(",");
                } else {
                    flag = true;
                }
                writer.write(node.toString());

            }
            writer.write("]" + "\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("Error with saving");
            e.printStackTrace();
        }

    }

    public void saveSummary(FastaEntry entry_params, ArrayList<SequenceContainer> summarizedNodes, String dir) {
        String pathFile = dir + "/" + entry_params.name.substring(1) + "/" + entry_params.name.substring(1);

        try {
            createDirectory(dir);
            createDirectory(dir + "/" + entry_params.name.substring(1));
            pathFile = dir + "/" + entry_params.name.substring(1) + "/" + entry_params.name.substring(1) + "_summary";

            createFile(pathFile);
            File file = new File(pathFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

//            writer.write(entry_params.name + "\n");
//            writer.write(entry_params.chain + "\n");

            writer.write("[" + "\n");
            boolean flag = false;
            for (SequenceContainer node : summarizedNodes) {
                if (flag) {
                    writer.write(",");
                } else {
                    flag = true;
                }
                writer.write(node.toString());
            }
            writer.write("]" + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error with saving");
            e.printStackTrace();
        }
    }

    public void deleteFiles() {
        try {
            File f1 = new File(config.fasta_entry_file);           //file to be delete
            File f2 = new File(config.dot_bracket_file);           //file to be delete
            f1.delete();
            f2.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



