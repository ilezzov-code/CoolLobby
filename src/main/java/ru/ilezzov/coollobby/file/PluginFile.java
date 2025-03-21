package ru.ilezzov.coollobby.models;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class PluginFile {
    private final Yaml YAML = new Yaml();
    private final String filePath;
    private final String fileName;

    private Map<String, Object> file;

    public PluginFile(final File file) throws FileNotFoundException {
        this.fileName = file.getName();
        this.filePath = file.getPath();
        this.file = load(this.filePath);
    }

    public Map<String, Object> load(final String filePath) throws FileNotFoundException {
        return YAML.load(new FileInputStream(filePath));
    }

    public void reload() throws FileNotFoundException {
        file = load(this.filePath);
    }

    public void reload(final File file) throws FileNotFoundException {
        this.file = load(file.getPath());
    }

    public String getString(final String key) throws NullPointerException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }

        return String.valueOf(getValue(key));
    }

    public Object getObject(final String key) throws NullPointerException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }
        return value;
    }

    public int getInt(final String key) throws NullPointerException, ClassCastException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new ClassCastException("The returned object is not an int. File: ".concat(fileName).concat(" Key: ").concat(key));
    }

    public double getDouble(final String key) throws NullPointerException, ClassCastException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new ClassCastException("The returned object is not a double. File: ".concat(fileName).concat(" Key: ").concat(key));
    }

    public long getLong(final String key) throws NullPointerException, ClassCastException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new ClassCastException("The returned object is not a long. File: ".concat(fileName).concat(" Key: ").concat(key));
    }

    public boolean getBoolean(final String key) throws NullPointerException, ClassCastException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }

        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new ClassCastException("The returned object is not a boolean. File: ".concat(fileName).concat(" Key: ").concat(key));
    }

    public List<Object> getList(final String key) throws NullPointerException, ClassCastException {
        final Object value = getValue(key);

        if (value == null) {
            throw new NullPointerException("The returned value is null. File: ".concat(fileName).concat(" Key: ").concat(key));
        }

        if (value instanceof List<?>) {
            return (List<Object>) value;
        }
        throw new ClassCastException("The returned object is not a List. File: ".concat(fileName).concat(" Key: ").concat(key));
    }

    private Object getValue(final String keyPath) {
        final String[] keys = keyPath.split("\\.");
        Map<String, Object> finalMap = file;

        for (int i = 0; i < keys.length -1; i++) {
            if (finalMap.get(keys[i]) instanceof Map) {
                finalMap = (Map<String, Object>) finalMap.get(keys[i]);
            }
        }

        return finalMap.get(keys[keys.length - 1]);
    }
}
