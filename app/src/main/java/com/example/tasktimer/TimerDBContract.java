package com.example.tasktimer;

import android.provider.BaseColumns;

public class TimerDBContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private void FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class TimerEntry implements BaseColumns {
        public static final String TABLE_NAME = "timerEntry";
        public static final String COLUMN_NAME = "timerName";
        public static final String COLUMN_TIME= "timerTime";
    }
}


