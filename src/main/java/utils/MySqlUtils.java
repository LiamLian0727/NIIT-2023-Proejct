package utils;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.*;

import static config.Config.*;

/**
 * @author 连仕杰
 */
public class MySqlUtils {

    public static Connection createConnection() throws SQLException, ClassNotFoundException {

        Connection con;
        Class.forName(JDBC);
        con = DriverManager.getConnection(JDBC_URL, DATABASE_USER, DATABASE_PASSWORD);
        return con;
    }

    public static boolean putFilesInToMySQL(InputStream fin, String fileName) {
        System.out.println(fileName);
        return true;
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
