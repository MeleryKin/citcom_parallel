package service;

import exception.FileParserException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileWork {

    public ArrayList<String> getFile(String fileDataName) throws FileParserException {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(fileDataName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            ArrayList<String> result = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return result;
        }
        catch (Exception exception) {
            throw new FileParserException("Ошибка при чтении файла!");
        }
    }
}
