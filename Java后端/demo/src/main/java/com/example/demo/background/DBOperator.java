package com.example.demo.background;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBOperator {
    Connection con;
    Statement st;
    String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String dbURL="jdbc:sqlserver://localhost:1433;integratedSecurity=true; DatabaseName=JavaDB";

    public DBOperator(){
        try{
            Class.forName(driverName);
            con = DriverManager.getConnection(dbURL);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet query(String sql){
        ResultSet rs=null;
        try {
            st=con.createStatement();
            rs=st.executeQuery(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public void update(String sql){
        try {
            st=con.createStatement();
            st.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
