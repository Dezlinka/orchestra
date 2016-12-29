package controller;

import model.Claim;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import java.sql.*;
import java.util.*;

import static controller.ApachePastvuOrchestrator.MAXCOUNTER;

/**
 * Created by Ilya Evlampiev on 09.12.2015.
 */
public class ApachePastvuLoader extends Thread {

    private final String USER_AGENT = "Mozilla/5.0";


    public void run() {
        int id = 0;
        while (id < MAXCOUNTER) {
            id = ApachePastvuOrchestrator.getNext();
            String url = "https://uslugi.tatarstan.ru/open-gov/detail/" + id;

            try {
                Document html = Jsoup.connect(url).ignoreContentType(true).userAgent(USER_AGENT).ignoreHttpErrors(true).timeout(10000).get();
                if (checkClaim(id)) continue;
                if (html.getElementsByClass("errorBlock").first() == null) {
                    if(html.getElementsContainingOwnText("Категория").first() != null) {
                        Claim claim = new Claim();
                        claim.setId(id);
                        String status = html.getElementsContainingOwnText("Статус").first().nextSibling().toString();
                        if (!checkStatuses(status)) addStatus(status);
                        claim.setStatus(getStatusId(status));
                        addClaim(claim);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[])
    {
        (new ApachePastvuLoader()).run();
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String urlp = "jdbc:mysql://localhost:3306/uslugi_crap?characterEncoding=utf8";
            connection = DriverManager.getConnection(urlp, "root", "a0k5331su9mn");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void addClaim(Claim claim) {
        Connection connection = getConnection();
        String sql = "INSERT INTO claims (id, status) VALUES(?,?);";
        PreparedStatement prst = null;
        try {
            prst = connection.prepareStatement(sql);
            prst.setLong(1, claim.getId());
            prst.setLong(2, claim.getStatus());
            prst.execute();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    public static void addStatus(String status) {
        Connection connection = getConnection();
        String sql = "INSERT INTO statuses (status_text) VALUES(?);";

        PreparedStatement prst = null;
        try {
            prst = connection.prepareStatement(sql);
            prst.setString(1, status);
            prst.execute();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    public static boolean checkStatuses(String status) {
        Connection connection = getConnection();
        String sql = "SELECT * FROM statuses WHERE status_text = ?;";
        PreparedStatement prst = null;
        boolean isHave = false;
        try {
            prst = connection.prepareStatement(sql);
            prst.setString(1, status);
            ResultSet rs = prst.executeQuery();
            if (rs.next()) isHave = true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return isHave;
    }

    public static boolean checkClaim(long id) {
        Connection connection = getConnection();
        String sql = "SELECT * FROM claims WHERE id = ?;";
        PreparedStatement prst = null;
        boolean isHave = false;
        try {
            prst = connection.prepareStatement(sql);
            prst.setLong(1, id);
            ResultSet rs = prst.executeQuery();
            if (rs.next()) isHave = true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return isHave;
    }

    public static Map<Integer, String> getStatuses() {
        Connection connection = getConnection();
        String sql = "SELECT * FROM statuses;";
        PreparedStatement prst = null;
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            prst = connection.prepareStatement(sql);
            ResultSet rs = prst.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("status_text"));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return map;
    }

    public static int getStatusId(String status) {
        Connection connection = getConnection();
        String sql = "SELECT * FROM statuses WHERE status_text = ?;";
        PreparedStatement prst = null;
        int id = -1;
        try {
            prst = connection.prepareStatement(sql);
            prst.setString(1, status);
            ResultSet rs = prst.executeQuery();
            if (rs.next()) id = rs.getInt("id");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return id;
    }

    public static long getCountOfClaim(int status) {
        Connection connection = getConnection();
        String sql = "SELECT COUNT(id) 'count' FROM claims WHERE status = ?;";
        PreparedStatement prst = null;
        long count = 0;
        try {
            prst = connection.prepareStatement(sql);
            prst.setInt(1, status);
            ResultSet rs = prst.executeQuery();
            if (rs.next()) count = rs.getLong("count");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                prst.close();
                connection.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return count;
    }
}
