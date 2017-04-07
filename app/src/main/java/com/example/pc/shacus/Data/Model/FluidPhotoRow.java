package com.example.pc.shacus.Data.Model;

/**
 * Created by licl on 2017/2/4.
 */
import java.util.ArrayList;

public class FluidPhotoRow{
    private ArrayList<ImageData> imageDatas;
    private ArrayList<String> titles;
    private ArrayList<String> contents;
    private int rowHeight;

    public FluidPhotoRow(ArrayList<ImageData> imageDatas, int rowHeight){
        setImageData(imageDatas);
        setRowHeight(rowHeight);
    }


    public ArrayList<ImageData> getImageDatas() {
        return imageDatas;
    }


    public void setImageData(ArrayList<ImageData> imageDatas) {
        this.imageDatas = imageDatas;
    }


    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
}