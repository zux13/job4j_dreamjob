package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(
                0,
                "Intern Java Developer",
                "Entry-level internship for aspiring Java developers. Gain hands-on experience with mentorship."
                )
        );
        save(new Vacancy(
                0,
                "Junior Java Developer",
                "Entry position for beginner Java developers. Basic knowledge required, learning on the job."
                )
        );
        save(new Vacancy(
                0,
                "Junior+ Java Developer",
                "Transition level between Junior and Middle. Some project experience expected."
                )
        );
        save(new Vacancy(
                0,
                "Middle Java Developer",
                "Mid-level Java developer role. Solid Java skills, active participation in development projects."
                )
        );
        save(new Vacancy(
                0,
                "Middle+ Java Developer",
                "Advanced Mid-level role. Strong framework knowledge (e.g., Spring), code optimization skills."
                )
        );
        save(new Vacancy(
                0,
                "Senior Java Developer",
                "Senior-level Java developer. Leads complex projects, mentors juniors, designs architecture."
                )
        );
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy)
                        -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
