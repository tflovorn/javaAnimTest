package javaAnimTest;

import java.io.*;

public class Logger {
    FileWriter log = null;
    int offset = 0;

    public Logger(String fileName) {
        try {
            log = new FileWriter(fileName);
        } catch (IOException e) { }
    }
    
    public void write(String data) {
        try {
            log.write(data, offset, data.length());
            offset += data.length();
            log.flush();
        } catch (IOException e) { }
    }

    public void writeln(String data) {
        write(data + "\n");
    }

    public void close() {
        try {
            log.close();
        } catch (IOException e) { }
    }
}
