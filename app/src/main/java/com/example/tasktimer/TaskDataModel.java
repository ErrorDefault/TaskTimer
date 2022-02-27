package com.example.tasktimer;

public class TaskDataModel {
    String name;
    int seconds;
    String notifMessage;

    public TaskDataModel(String name, int seconds, String notifMessage) {
        this.name=name;
        this.seconds=seconds;
        this.notifMessage = this.notifMessage;
    }

    public String getName() {
        return name;
    }

    public int getHours() {
        return seconds / 3600;
    }

    public int getMinutes() {
        return seconds / 60 % 60;
    }

    public int getSeconds() {
        return seconds % 60;
    }

    public int getMilliseconds(){
        return seconds * 1000;
    }

    public String getNotifMessage(){
        return notifMessage;
    }
}
