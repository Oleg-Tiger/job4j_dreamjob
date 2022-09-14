package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class PostDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    private final BasicDataSource pool;
    private static final String SELECT_ALL = "SELECT * FROM post";
    private static final String ADD = "INSERT INTO post(name, description, city_id, visible, created) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE post SET name = ?, description = ?, city_id = ?, visible = ?, created = ? WHERE id = ?";
    private static final String FIND_BY_ID = SELECT_ALL + " WHERE id = ?";

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createPost(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            addPost(post, ps);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return post;
    }

    public boolean update(Post post) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(UPDATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            addPost(post, ps);
            ps.setInt(6, post.getId());
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return result;
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createPost(it);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    private Post createPost(ResultSet it) throws SQLException {
        Post post = new Post(it.getInt("id"), it.getString("name"));
        City city = new City();
        city.setId(it.getInt("city_id"));
        post.setCity(city);
        post.setVisible(it.getBoolean("visible"));
        post.setDescription(it.getString("description"));
        post.setCreated(it.getTimestamp("created").toLocalDateTime());
        return post;
    }

    private void addPost(Post post, PreparedStatement ps) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setInt(3, post.getCity().getId());
        ps.setBoolean(4, post.isVisible());
        ps.setObject(5, post.getCreated());
    }
}