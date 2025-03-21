package ru.ilezzov.pluginBlank.manager;

import ru.ilezzov.pluginBlank.Main;
import ru.ilezzov.pluginBlank.models.PluginFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static ru.ilezzov.pluginBlank.Main.*;

public class FileManager {
    private final String pluginDirectory = getInstance().getDataPath().toString();

    public PluginFile newFile(final String fileName, final String filePath) throws IllegalArgumentException, IOException {
        final String path = Paths.get(pluginDirectory, filePath).toString();
        final File directory = new File(path);
        final File file = new File(path, fileName);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
            copyFile(file);
        }

        return new PluginFile(file);
    }


    private void copyFile(final File file) throws IllegalArgumentException, NullPointerException {
        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file.getName());
             final OutputStream outputStream = new FileOutputStream(file)) {

            if (inputStream == null) {
                throw new NullPointerException("File ".concat(file.getName()).concat(" doesn't copy, because it's null"));
            }

            int data;
            while ((data = inputStream.read()) != -1) {
               outputStream.write(data);
            }

            outputStream.flush();

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }


    }



}
