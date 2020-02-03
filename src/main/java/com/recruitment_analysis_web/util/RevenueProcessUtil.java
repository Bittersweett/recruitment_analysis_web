package com.recruitment_analysis_web.util;

import java.util.LinkedHashMap;

public class RevenueProcessUtil {

    static private LinkedHashMap<Integer, String> map = new LinkedHashMap<>();

    static public LinkedHashMap<Integer, String> getRevenueMap() {
        for (int i = 1000; i < 100000; i += 5) {
            if (i < 3000) {
                map.put(i, "3k以下");
            } else if (i < 5000) {
                map.put(i, "3k-5k");

            } else if (i < 7000) {
                map.put(i, "5k-7k");

            } else if (i < 9000) {
                map.put(i, "7k-9k");

            } else if (i < 11000) {
                map.put(i, "9k-11k");

            } else if (i < 13000) {
                map.put(i, "11k-13k");

            } else if (i < 15000) {
                map.put(i, "13k-15k");

            } else if (i < 17000) {
                map.put(i, "15k-17k");

            } else if (i < 19000) {
                map.put(i, "17k-19k");

            } else if (i < 21000) {
                map.put(i, "19k-21k");

            } else if (i < 23000) {
                map.put(i, "21k-23k");

            } else if (i < 25000) {
                map.put(i, "23k-25k");

            } else if (i < 27000) {
                map.put(i, "25k-27k");

            } else if (i < 29000) {
                map.put(i, "27k-29k");

            } else if (i < 31000) {
                map.put(i, "29k-31k");

            } else if (i < 35000) {
                map.put(i, "31k-25k");

            } else if (i < 40000) {
                map.put(i, "35k-40k");

            } else if (i < 45000) {
                map.put(i, "40k-45k");

            } else if (i < 50000) {
                map.put(i, "45k-50k");

            } else {
                map.put(i, "50k以上");
            }
        }
        return map;
    }
}
