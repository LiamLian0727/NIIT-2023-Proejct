package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;

import static config.Config.*;

/**
 * @author 连仕杰
 */
public class MySqlUtils {

    private static String line;
    private static String[] row;
    private static String[] title;
    private static String csvSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Connection con;
        Class.forName(JDBC);
        con = DriverManager.getConnection(JDBC_URL, DATABASE_USER, DATABASE_PASSWORD);
        return con;
    }

    public static boolean isTableExist(Connection conn, String tableName) throws SQLException {
        boolean isExist = false;
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
        if (rs.next()) {
            isExist = true;
        }
        rs.close();
        return isExist;
    }

    public static void dropTable(Connection conn, String tableName) throws SQLException {
        String dropTableSql = "DROP TABLE IF EXISTS " + tableName;
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(dropTableSql);
        System.out.println("Table " + tableName + " dropped successfully.");
        stmt.close();
    }

    public static String createTable(String tableName, String[] columnNames) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        String columnTypes = "text";
        String insertTableSql = null;
        try {
            conn = createConnection();
            if (isTableExist(conn, tableName)) {
                dropTable(conn, tableName);
            }

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE `").append(tableName).append("` (");
            for (int i = 0; i < columnNames.length; i++) {
                sb.append("`").append(columnNames[i]).append("` ").append(columnTypes);
                if (i < columnNames.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(");");
            String createTableSql = sb.toString();

            // 执行建表语句
            stmt = conn.createStatement();
            stmt.executeUpdate(createTableSql);

            System.out.println("Table " + tableName + " created successfully.");

            // 构造插入语句
            StringBuilder sq = new StringBuilder();
            sq.append("INSERT INTO ").append(tableName).append(" (");
            for (int i = 0; i < columnNames.length; i++) {
                sq.append(columnNames[i]);
                if (i < columnNames.length - 1) {
                    sq.append(", ");
                } else {
                    sq.append(") ");
                }
            }
            sq.append("VALUE ").append(" (");
            for (int i = 0; i < columnNames.length; i++) {
                sq.append("?");
                if (i < columnNames.length - 1) {
                    sq.append(", ");
                } else {
                    sq.append("); ");
                }
            }

            insertTableSql = sq.toString();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接和 Statement 对象
            stmt.close();
            conn.close();
        }
        return insertTableSql;

    }

    public static boolean putFilesInToMySQL(InputStream fin, String fileName) throws SQLException, ClassNotFoundException, IOException {
        PreparedStatement statement = null;
        boolean is_successful = true;
        int count = 0;
        System.out.println(fileName);

        Connection connection = createConnection();
        connection.setAutoCommit(false);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fin))) {
            line = reader.readLine();
            title = line.split(csvSplitBy);
            String insertSql = createTable(TABLE_NAME, title);
            statement = connection.prepareStatement(insertSql);

            while ((line = reader.readLine()) != null) {
                row = line.split(csvSplitBy);
                for (int i = 0; i < row.length; i++) {
                    statement.setString(i + 1, "".equals(row[i]) ? NULLVALUE : row[i]);
                }
                if (row.length < title.length) {
                    for (int i = row.length; i < title.length; i++) {
                        statement.setString(i + 1, NULLVALUE);
                    }
                }
                statement.addBatch();
                if (count != 0 && count % BATCH_LINE == 0) {
                    statement.executeBatch();
                }
                count++;
                if (count == MAX_LINE) {
                    statement.executeBatch();
                    break;
                }
            }
            if (count < MAX_LINE) {
                statement.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
            is_successful = false;
        } finally {
            statement.close();
            connection.setAutoCommit(true);
            connection.close();
        }
        return is_successful;
    }

    public static boolean signIn(String name, String password) throws SQLException, ClassNotFoundException {

        Connection con = createConnection();
        Statement stmt = con.createStatement();

        String sql = "Select password from niit.user where username = '" + name + "'";
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String spwd = rs.getString(1);
            if (spwd.equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExist(String name) throws SQLException, ClassNotFoundException {
        Connection con = createConnection();
        Statement stmt = con.createStatement();

        String sql = "SELECT UserName from user where UserName = '" + name + "'";
        ResultSet resultSet = stmt.executeQuery(sql);
        return resultSet.next();
    }

    public static void signUp(String name, String password, String email) throws SQLException, ClassNotFoundException {
        Connection con = createConnection();
        Statement stmt = con.createStatement();
        String sql = "insert into user values('" + name + "','"
                + password + "','"
                + email + "')";
        stmt.executeUpdate(sql);
    }

    public static String getEmail(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = createConnection();

        String sql = "select * from user where username = ? and password = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("EmailID");
        }
        return "NULL";

    }
}
