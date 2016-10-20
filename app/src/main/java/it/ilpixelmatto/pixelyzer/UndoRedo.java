package it.ilpixelmatto.Pixelyzer;


import android.graphics.Bitmap;

import java.util.ArrayList;

public class UndoRedo {


    private static ArrayList<Bitmap> undoRedoArray;

    public UndoRedo(Bitmap bMap) {
        undoRedoArray = new ArrayList<>();
        undoRedoArray.add(bMap.copy(bMap.getConfig(), true));
    }

    public void addBitmap(Bitmap b) {
        undoRedoArray.add(b.copy(b.getConfig(), true));
    }


    public Bitmap undo() {
        if (undoRedoArray.isEmpty())
            return null;
        //Bitmap temp =
        return undoRedoArray.remove(undoRedoArray.size() - 1);
    }


    public ArrayList<Bitmap> getbMapArray() {
        return undoRedoArray;
    }


}
