package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public MemoryCandidateRepository() {
        save(new Candidate(0, "Alexander", "Junior Java Developer", LocalDateTime.now(), 2));
        save(new Candidate(0, "Maria", "Senior Python Developer", LocalDateTime.now(), 1));
        save(new Candidate(0, "Kristina", "Middle Plus Scala Developer", LocalDateTime.now(), 1));
        save(new Candidate(0, "Roman", "C++ developer", LocalDateTime.now(), 2));
        save(new Candidate(0, "Vyacheslav", "QA Automation Engineer", LocalDateTime.now(), 3));
        save(new Candidate(0, "Alena", "DevOps", LocalDateTime.now(), 3));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate)
                        -> new Candidate(
                                oldCandidate.getId(),
                                candidate.getName(),
                                candidate.getDescription(),
                                oldCandidate.getCreationDate(),
                                candidate.getCityId()
                        )
        ) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return new ArrayList<>(candidates.values());
    }
}
