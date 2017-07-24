package com.lch.demo.aop.hooker;

import android.util.Log;

public  class TimeLog {

        private static long start;

        public static TimeLog newInstance() {
            return new TimeLog();
        }

        public static void startTime() {
            start=System.currentTimeMillis();

        }

        public static void endTime() {

            Log.e("lich","spend time:"+(System.currentTimeMillis()-start));

        }
    }