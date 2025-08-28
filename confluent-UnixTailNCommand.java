package dsalldmix;

import java.io.*;


/**
 * design unix tail -n  command
 */
public class UnixTailNCommand {

    public static void tailNCommand(String filePath, String nArg) throws IOException {
        File file = new File(filePath);

        // Parse nArg (could be -10 or +10)
        boolean fromStart = false;
        int n;
        if (nArg.startsWith("+")) {
            fromStart = true;
            n = Integer.parseInt(nArg.substring(1));
        } else {
            n = Integer.parseInt(nArg.replace("-", ""));
        }

        if (fromStart) {
            // tail -n +N : print from Nth line onward
            printFromLineN(file, n);
        } else {
            // tail -n N : print last N lines
            printLastNLines(file, n);
        }
    }

    private static void printFromLineN(File file, int startLine) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                if (lineNum >= startLine) {
                    System.out.println(line);
                }
                lineNum++;
            }
        }
    }

    private static void printLastNLines(File file, int n) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        long pointer = raf.length() - 1;
        int lineCount = 0;
        StringBuilder sb = new StringBuilder();

        while (pointer >= 0) {
            raf.seek(pointer);
            char c = (char) raf.readByte();
            if (c == '\n') {
                lineCount++;
                if (lineCount > n) break;
            }
            sb.append(c);
            pointer--;
        }
        raf.close();

        // Reverse the collected lines since we read backward
        System.out.print(sb.reverse().toString());
    }

    // Test main
    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\himan\\IdeaProjects\\Design-Pattern\\src\\main\\resources\\data.txt"; // change to your file
        tailNCommand(filePath, "-10");  // last 10 lines
        System.out.println("------------");
        tailNCommand(filePath, "+5");   // from line 5 onward
    }
}
