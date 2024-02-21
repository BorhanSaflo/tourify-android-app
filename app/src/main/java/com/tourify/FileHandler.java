package com.tourify;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileHandler {

    public static String fileName = "credentials.dat";

    public static void writeData(String type, String data, Context context){
        HashMap<String, String> dataMap = new HashMap<>();

        // Try to read existing data
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataMap = (HashMap<String, String>)ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, it's created
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Add or update the type:data pair
        dataMap.put(type, data);

        // Write the updated map to the file
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataMap);
            oos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readData(String type, Context context){
        HashMap<String, String> dataMap = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            dataMap = (HashMap<String, String>)ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, don't read any data
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Return the data associated with the type, or null if not found
        return dataMap != null ? dataMap.get(type) : null;
    }
}
