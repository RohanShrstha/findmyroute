package com.findmrotue.fmr.constant;
public final class CurrentDbServer {

    private static final String MSSQL = "/patch_my_sql";
    private static final String MYSQL = "/patch_my_sql";
    private static final String ORACLE = "/patch_oracle";
    private static final String POSTGRES = "/patch_post_gres";


    private CurrentDbServer() {
    }

    public static String currentConnectedDb(String db) {
        if (db.contains("mysql")) {
            return CurrentDbServer.MYSQL;
        }

        if (db.contains("sqlserver")) {
            return CurrentDbServer.MSSQL;
        }

        if (db.contains("oracle")) {
            return CurrentDbServer.ORACLE;
        }

        if (db.contains("postgres")) {
            return CurrentDbServer.POSTGRES;
        }

        throw new RuntimeException("NO Driver Connected");

    }
}
