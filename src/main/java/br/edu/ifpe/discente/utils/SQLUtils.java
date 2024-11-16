package br.edu.ifpe.discente.utils;

public class SQLUtils {

    public static String getInsertSQL(String tableName, String... columns) {
        String columnNames = String.join(",", columns);
        String placeholders = String.join(",", java.util.Collections.nCopies(columns.length, "?"));
        return "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";
    }

    public static String getSelectSQL(String tableName) {
        return "SELECT * FROM " + tableName;
    }
    
    public static String getSelectSQLById(String tableName,String idColumn) {
        return "SELECT * FROM " + tableName + " WHERE ID " + "= ?";
    }

    public static String getUpdateSQL(String tableName, String[] columns, String idColumn) {
        String setClause = String.join("=?, ", columns) + "=?";
        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + idColumn + "= ?";
    }

    public static String getDeleteSQL(String tableName, String idColumn) {
        return "DELETE FROM " + tableName + " WHERE " + idColumn + "= ?";
    }

}
