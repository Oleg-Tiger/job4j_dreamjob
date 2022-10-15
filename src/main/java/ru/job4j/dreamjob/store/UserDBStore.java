package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class UserDBStore {
    private final BasicDataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(UserDBStore.class.getName());
    private static final String ADD = "INSERT INTO users(email, password) VALUES (?, ?)";
    private static final String FIND_EMAIL_PASSWORD = "SELECT * FROM users WHERE email = ? AND password = ?";

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in UserDBStore.add()", e);
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_EMAIL_PASSWORD)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    User user = new User(it.getString("email"), it.getString("password"));
                    user.setId(it.getInt("id"));
                    result = Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in UserDBStore.findUserByEmailAndPwd()", e);
        }
        return result;
    }
}
