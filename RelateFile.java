import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class RelateFile {
    String filePath = "";
    File file = null;
    BufferedReader reader = null;
    BufferedWriter writer = null;

    RelateFile(String path) {
        filePath = path;
        file = new File(filePath);
    }

    private void initReader() {
        try {
            this.reader = new BufferedReader(new FileReader(this.file));
        } catch (FileNotFoundException e) {
            Screen.display("not found " + this.filePath);
        }
    }

    private void initWriter() {
        try {
            writer = new BufferedWriter(new FileWriter(this.file));
        } catch (IOException e) {
            Screen.display("cant write " + this.filePath);
        }
    }

    public ArrayList<String> getAllLine() {
        initReader();
        ArrayList<String> returnList = new ArrayList<String>();
        String line = "";
        try {
            while ((line = this.reader.readLine()) != null)
                returnList.add(line);
        } catch (IOException e) {
            Screen.display("failed to read" + this.filePath);
        }
        this.reader = null;
        return returnList;

    }

    public void writeList(ArrayList<String> list) {
        initWriter();
        try {
            for (int i = 0; i < list.size(); i++)
                this.writer.write(list.get(i) + "\n");
            this.writer.flush();
            this.writer.close();
        } catch (IOException e) {
            Screen.display("failed to write" + this.filePath);
        }

    }
}
