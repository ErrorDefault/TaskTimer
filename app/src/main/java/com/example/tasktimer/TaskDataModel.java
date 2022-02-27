package com.example.tasktimer;

public class TaskDataModel {
    String name;
    int seconds;
    int id;
    String notifMessage;

    public TaskDataModel(String name, int seconds, int id){
        setData(name, seconds, null, id);
    }

    public TaskDataModel(String name, int seconds, String notifMessage, int id) {
        setData(name, seconds, notifMessage, id);
    }

    private void setData(String name, int seconds, String notifMessage, int id){
        this.name=name;
        this.seconds=seconds;
        this.notifMessage = this.notifMessage;
        this.id = id;
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

    public int getID(){
        return id;
    }

    public String getNotifMessage(){
        return notifMessage;
    }
}
