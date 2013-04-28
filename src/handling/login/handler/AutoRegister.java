package handling.login.handler;

import client.LoginCrypto;
import client.MapleClient;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class AutoRegister {
        private static final int ACCOUNTS_PER_IP = 100; //change the value to the amount of accounts you want allowed for each ip
        public static final boolean autoRegister = true; //enable = true or disable = false

        public static boolean success = false; // DONT CHANGE
        
        public static boolean getAccountExists(String login) {
                boolean accountExists = false;
                Connection con = DatabaseConnection.getConnection();
                try {
                        PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
                        ps.setString(1, login);
                        ResultSet rs = ps.executeQuery();
                        if(rs.first()) {
                        accountExists = true;
                    }
                } catch (Exception ex) {
                      System.out.println(ex);
                }
                return accountExists;
        }

        public static void createAccount(String login, String pwd, String eip) {
                String sockAddr = eip;
                Connection con;

                //connect to database or halt
                try {
                        con = DatabaseConnection.getConnection();
                } catch (Exception ex) {
                        System.out.println(ex);
                        return;
                }

                try {
            ResultSet rs;
            try (PreparedStatement ipc = con.prepareStatement("SELECT SessionIP FROM accounts WHERE SessionIP = ?")) {
                ipc.setString(1, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                rs = ipc.executeQuery();
                if (rs.first() == false || rs.last() == true && rs.getRow() < ACCOUNTS_PER_IP) {
                        try {
                        try (PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, SessionIP) VALUES (?, ?, ?, ?, ?, ?)")) {
                            ps.setString(1, login);
                            ps.setString(2, LoginCrypto.hexSha1(pwd));
                            ps.setString(3, "no@email.provided");
                            ps.setString(4, "2008-04-07");
                            ps.setString(5, "00-00-00-00-00-00");
                          ///  ps.setInt(6, 123456);
                            ps.setString(6, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                            ps.executeUpdate();
                        }

                                success = true;
                        } catch (SQLException ex) {
                                System.out.println(ex);
                                return;
                        }
                }
            }
                        rs.close();
                } catch (SQLException ex) {
                        System.out.println(ex);
                }
        }
}
