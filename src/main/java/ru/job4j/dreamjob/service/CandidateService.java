package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.Collection;

public class CandidateService {

    private static final CandidateService INST = new CandidateService();
    private final CandidateStore store;

    private CandidateService() {
        store = CandidateStore.instOf();
    }

    public Candidate add(Candidate candidate) {
        return store.add(candidate);
    }

    public Candidate findById(int id) {
        return store.findById(id);
    }

    public Candidate update(Candidate candidate) {
        return store.update(candidate);
    }

    public Collection<Candidate> findAll() {
        return store.findAll();
    }

    public static CandidateService instOf() {
        return INST;
    }
}
