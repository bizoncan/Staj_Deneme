package com.example.staj_deneme.Models;

import java.util.List;

public class ErrorResponseModel {
    List<ErrorModel> errorModelList;
    List<ErrorInfoModel> errorInfoModelList;

    public List<ErrorModel> getErrorModelList() {
        return errorModelList;
    }

    public List<ErrorInfoModel> getErrorInfoModelList() {
        return errorInfoModelList;
    }
}
