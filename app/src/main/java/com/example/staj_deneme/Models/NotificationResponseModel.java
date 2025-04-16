package com.example.staj_deneme.Models;

import java.util.List;

public class NotificationResponseModel {
    public List<NotificationModel> notificationList;
    public List<WorkOrderModel> workNotificationList;

    public List<NotificationModel> getNotificationList() {
        return notificationList;
    }
    public List<WorkOrderModel> getWorkNotificationList() {
        return workNotificationList;
    }
}
