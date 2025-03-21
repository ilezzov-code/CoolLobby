package ru.ilezzov.coollobby.manager;

import ru.ilezzov.coollobby.file.PluginFile;

import java.io.*;
import java.nio.file.Paths;

import static ru.ilezzov.coollobby.Main.*;

public class FileManager {
    public static PluginFile newFile(final String fileName, final String filePath, final String pluginDirectory) throws IllegalArgumentException, IOException {
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


    private static void copyFile(final File file) throws IllegalArgumentException, NullPointerException {
        try (final InputStream inputStream = FileManager.class.getClassLoader().getResourceAsStream(file.getName());
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
