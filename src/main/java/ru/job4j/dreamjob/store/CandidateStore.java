package ru.job4j.dreamjob.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private static final AtomicInteger ID = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Junior Java Developer"));
        candidates.put(2, new Candidate(2, "Middle Java Developer"));
        candidates.put(3, new Candidate(3, "Senior Java Developer"));
    }

    public Candidate add(Candidate candidate) {
        candidate.setId(ID.incrementAndGet());
        return candidates.put(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public Candidate update(Candidate candidate) {
        return candidates.replace(candidate.getId(), candidate);
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
