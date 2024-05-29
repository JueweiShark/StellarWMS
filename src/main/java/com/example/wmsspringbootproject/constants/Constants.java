package com.example.wmsspringbootproject.constants;

public interface Constants {

    interface PageQuery{
        int DEFAULT_PAGE=1;
        int DEFAULT_SIZE=10;
    }

    interface Snowflake {
        long TWEPOCH = 1288834974657L;
        long WORKER_ID_BITS = 5L;
        long DATACENTER_ID_BITS = 5L;
        long SEQUENCE_BITS = 12L;
    }

    interface TimeValueInMillions{
        long SECOND=1000;
        long MIN=60*SECOND;
        long HOUR=60*MIN;
        long DAY=24*HOUR;
        long WEEK=7*DAY;
        long MONTH=30*DAY;
        long YEAR=12*MONTH;
    }

}
