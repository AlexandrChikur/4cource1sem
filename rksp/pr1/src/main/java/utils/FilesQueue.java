package utils;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;

public class FilesQueue<K> extends ArrayList<K> {

    private int maxSize;

    public FilesQueue(int size){
        this.maxSize = size;
    }

    public boolean add(K k){
        if (size() >= maxSize){
            throw new RuntimeException("Couldn't add new element");
        }
        boolean r = super.add(k);
        return r;
    }

    public K pop() {
        var res = get(0);
        remove(0);
        return res;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public boolean isFull() {
        return size() == maxSize;
    }
}