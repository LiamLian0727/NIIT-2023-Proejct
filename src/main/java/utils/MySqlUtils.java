package utils;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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

    public static boolean putFilesInToMySQL(InputStream fin, String fileName) throws SQLException, ClassNotFoundException, IOException {
        boolean is_successful = true;
        int count = 0;
        System.out.println(fileName);
        String sql = "INSERT INTO imdb_movies(imdb_title_id, title, original_title, year, date_published, genre, duration, country, language, director, writer, production_company, actors, description, avg_vote, votes, budget, usa_gross_income, worlwide_gross_income, metascore, reviews_from_users, reviews_from_critics) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = createConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(sql);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(fin))) {
            line = reader.readLine();
            title = line.split(csvSplitBy);
            System.out.println("title :" + Arrays.toString(title));
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
                if(count != 0 && count % BATCH_LINE == 0){
                    statement.executeBatch();
                }
                count ++;
                if (count == MAX_LINE){
                    statement.executeBatch();
                    break;
                }
            }
            if(count < MAX_LINE){
                statement.executeBatch();
            }
        }catch (Exception e){
            e.printStackTrace();
            is_successful = false;
        }finally {
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
