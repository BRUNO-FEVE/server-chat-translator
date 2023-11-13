package infra;

import java.sql.*;

public class JDBC {
    public String name;
    public String email;
    public String password;
    public String phone;
    public String language;

    public JDBC(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public JDBC(String name, String email, String password, String phone, String language) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.language = language;
    }

    public JDBC() {
    }

    public String getUserData() {
        return this.name + ";" + this.email + ";" + this.password + ";" + this.phone + ";" + this.language;
    }

    public void createTable(Connection connection) {
        String useDataBase = "USE `chat-translator-db`";
        String createTable = "CREATE TABLE IF NOT EXISTS users ("
                + "name VARCHAR(30), "
                + "email VARCHAR(30), "
                + "password VARCHAR(30), "
                + "phone VARCHAR(14), "
                + "language VARCHAR(20)"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(useDataBase);
            statement.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void createUser(Connection connection) {
        String sqlCommand = "INSERT INTO users (name,email,password,phone,language) VALUES (?,?,?,?,?)";

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sqlCommand);
            statement.setString(1, this.name);
            statement.setString(2, this.email);
            statement.setString(3, this.password);
            statement.setString(4, this.phone);
            statement.setString(5, this.language);
            statement.execute();
            System.out.println("Usuario registrado");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean login(Connection connection) {
        String sqlCommand = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlCommand)) {
            statement.setString(1, this.email);
            statement.setString(2, this.password);

            try (ResultSet response = statement.executeQuery();){
                while (response.next()) {
                    this.name = response.getString(1);
                    this.email = response.getString(2);
                    this.password = response.getString(3);
                    this.phone = response.getString(4);
                    this.language = response.getString(5);
                    return true;
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        } catch (SQLException error) {
            System.out.println(error.getStackTrace());
        }
        return false;
    }
}
