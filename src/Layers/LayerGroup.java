package Layers;

import Main.LayersControl;
import Main.MainFrame;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;

/**
 * pack a group of layers which belong to same tab
 */
public class LayerGroup implements Serializable,Cloneable {

    public ArrayList<Layer> layers= new ArrayList<>();
    String fileName = "New File";

    String modifyDescription="New File";

    public LayerGroup getClone() {
        LayerGroup clone=new LayerGroup();
        for(Layer layer:layers){
            clone.layers.add(layer.getClone());
        }
        clone.fileName=fileName;
        clone.modifyDescription=modifyDescription;
        return clone;
    }

    public void appendLayer(Layer layer) {
        layers.add(layer);
    }

    public void draw(GraphicsContext graphics) {
        for (Layer layer : layers) {
            layer.draw(graphics);
        }
    }

    public Layer getLayerByPosition(float x, float y) {
        int size = layers.size();
        // scan the layers decreasingly
        for (int i = size - 1; i >= 0; i--) {
            if (layers.get(i).isInner(x, y)) {
                return layers.get(i);
            }
        }
        return null;
    }

    public int getLayerIndexByPosition(float x, float y) {
        int size = layers.size();
        // scan the layers decreasingly
        for (int i = size - 1; i >= 0; i--) {
            if (layers.get(i).isInner(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public static LayerGroup readFile(String filename) throws IOException, ClassNotFoundException {
        // check if filename available
        File file = new File(filename);
        // if file doesn't exist, directly return
        if (!file.exists()) {
            return null;
        }
        ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(filename));
        LayerGroup layerGroup= (LayerGroup) objectInputStream.readObject();
        objectInputStream.close();
        return layerGroup;
    }

    void deleteCachedImage(){
        for(Layer layer:layers){
            if(layer instanceof Layer_Bitmap){
                ((Layer_Bitmap) layer).deleteCache();
            }
        }
    }

    /**
     * try to save file with existing filename
     */
    public void saveFile() throws IOException {
        deleteCachedImage();
        // check if filename available
        File file = new File(fileName);
        // if file doesn't exist, ask for a new filename
        if (!file.exists()) {
            saveFileAs();
            return;
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        objectOutputStream.writeObject(this);
        objectOutputStream.close();
        LayersControl.getInstance().getLayerHistory().saved=true;
    }

    /**
     * try to save file with a new filename
     */
    public void saveFileAs() throws IOException {
        deleteCachedImage();
        // ask for fileName
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as...");
        fileChooser.setInitialFileName("New File.jpf");
        FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("JPaint File", "*.jpf");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        File file = fileChooser.showSaveDialog(MainFrame.mainStage);
        if(file==null)return;
        fileName = file.getAbsolutePath();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        objectOutputStream.writeObject(this);
        objectOutputStream.close();

        LayersControl.getInstance().getLayerHistory().saved=true;
    }

    public void clear() {
        layers.clear();
    }

}
