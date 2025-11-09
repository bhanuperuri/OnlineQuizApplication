package com.quizapp.util;

import com.quizapp.DBHelper;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        DBHelper.init(); // will load driver + create tables
        try (Connection conn = DBHelper.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Connection successful!");
            } else {
                System.out.println("❌ Connection failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
