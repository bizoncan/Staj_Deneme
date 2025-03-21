package com.example.staj_deneme.Models;

public class ImageCollectionModel {
    private int id;
    private int errorId;
    private byte[] imageDataByte;

    public ImageCollectionModel() {
    }

    public ImageCollectionModel(int id, int errorId, byte[] imageDataByte) {
        this.id = id;
        this.errorId = errorId;
        this.imageDataByte = imageDataByte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public byte[] getImageDataByte() {
        return imageDataByte;
    }

    public void setImageDataByte(byte[] imageDataByte) {
        this.imageDataByte = imageDataByte;
    }
}
