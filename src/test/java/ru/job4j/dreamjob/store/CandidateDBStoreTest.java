package ru.job4j.dreamjob.store;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.util.List;

public class CandidateDBStoreTest {
    @Test
    public void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Java Developer");
        City city = new City(1, "Москва");
        candidate.setCity(city);
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        Assertions.assertThat(candidateInDb.getName()).isEqualTo(candidate.getName());
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Java");
        City city = new City(1, "Москва");
        candidate.setCity(city);
        store.add(candidate);
        candidate.setName("Java Developer");
        store.update(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        Assertions.assertThat(candidateInDb.getName()).isEqualTo(candidate.getName());
    }

    @Test
    public void whenFindAllCandidates() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Java Developer");
        Candidate candidate2 = new Candidate(1, "Java Developer 2");
        Candidate candidate3 = new Candidate(2, "Java Developer 3");
        City city = new City(1, "Москва");
        candidate.setCity(city);
        candidate2.setCity(city);
        candidate3.setCity(city);
        List<Candidate> resultBefore = store.findAll();
        store.add(candidate);
        store.add(candidate2);
        store.add(candidate3);
        List<Candidate> result = store.findAll();
        Assertions.assertThat(resultBefore.size() + 3).isEqualTo(result.size());
        Assertions.assertThat(result).contains(candidate, candidate2, candidate3);
    }
}