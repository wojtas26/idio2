package cz.idio.api;

import java.util.HashMap;

public class TempDataHolder {
    private static TempDataHolder instance;
    private HashMap<String, String> data;

    private TempDataHolder() {
        data = new HashMap<>();
    }

    public static TempDataHolder getInstance() {
        if (instance == null) {
            instance = new TempDataHolder();
        }
        return instance;
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public void remove(String key) {
        data.remove(key);
    }
}
