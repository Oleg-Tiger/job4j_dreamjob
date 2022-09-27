package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class CandidateDBStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());
    private final BasicDataSource pool;
    private static final String SELECT_ALL = "SELECT * FROM candidate";
    private static final String ADD = "INSERT INTO candidate(name, description, city_id, visible, created, photo) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE candidate SET name = ?, description = ?, city_id = ?, visible = ?, created = ?, photo =? WHERE id = ?";
    private static final String FIND_BY_ID = SELECT_ALL + " WHERE id = ?";

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(SELECT_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(createCandidate(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            addCandidate(candidate, ps);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return candidate;
    }

    public boolean update(Candidate candidate) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(UPDATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            addCandidate(candidate, ps);
            ps.setInt(7, candidate.getId());
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return result;
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createCandidate(it);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    private Candidate createCandidate(ResultSet it) throws SQLException {
        Candidate candidate = new Candidate(it.getInt("id"), it.getString("name"));
        City city = new City();
        city.setId(it.getInt("city_id"));
        candidate.setCity(city);
        candidate.setVisible(it.getBoolean("visible"));
        candidate.setDescription(it.getString("description"));
        candidate.setCreated(it.getTimestamp("created").toLocalDateTime());
        candidate.setPhoto(it.getBytes("photo"));
        return candidate;
    }

    private void addCandidate(Candidate candidate, PreparedStatement ps) throws SQLException {
        ps.setString(1, candidate.getName());
        ps.setString(2, candidate.getDescription());
        ps.setInt(3, candidate.getCity().getId());
        ps.setBoolean(4, candidate.isVisible());
        ps.setObject(5, candidate.getCreated());
        ps.setBytes(6, candidate.getPhoto());
    }
}
