package com.example.staj_deneme.Models;

import java.util.List;

public class WorkImagePostModel {
    List<String> imageCollectionModel;
    int workId;

    public WorkImagePostModel(List<String> imageCollectionModel, int workId) {
        this.imageCollectionModel = imageCollectionModel;
        this.workId = workId;
    }

    public WorkImagePostModel() {
    }

    public List<String> getImageCollectionModel() {
        return imageCollectionModel;
    }

    public void setImageCollectionModel(List<String> imageCollectionModel) {
        this.imageCollectionModel = imageCollectionModel;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }
}
