package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.CandidateDBStore;
import ru.job4j.dreamjob.store.CandidateStore;
import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class CandidateService {

    private final CandidateDBStore store;
    private final CityService cityService = new CityService();

    public CandidateService(CandidateDBStore store) {
        this.store = store;
    }

    public Candidate add(Candidate candidate) {
        return store.add(candidate);
    }

    public Candidate findById(int id) {
       Candidate result = store.findById(id);
        result.setCity(cityService.findById(result.getCity().getId()));
        return result;
    }

    public boolean update(Candidate candidate) {
        return store.update(candidate);
    }

    public Collection<Candidate> findAll() {
        List<Candidate> candidates = store.findAll();
        candidates.forEach(
                candidate -> candidate.setCity(
                        cityService.findById(candidate.getCity().getId())
                )
        );
        return candidates;
    }
}
