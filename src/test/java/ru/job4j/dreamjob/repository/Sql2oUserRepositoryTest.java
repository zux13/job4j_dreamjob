package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    void clearDatabase() {
        try (Connection con = sql2o.open()) {
            con.createQuery("TRUNCATE TABLE users").executeUpdate();
        }
    }

    @Test
    void whenSaveThenCanFindByEmailAndPassword() {
        var user = new User("test@mail.com", "Test", "pass");
        sql2oUserRepository.save(user);

        Optional<User> found = sql2oUserRepository.findByEmailAndPassword("test@mail.com", "pass");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test");
        assertThat(found.get()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void whenSaveUserThenReturnsUserWithId() {
        var user = new User("new@mail.com", "New", "1234");
        Optional<User> saved = sql2oUserRepository.save(user);

        assertThat(saved).isPresent();
        assertThat(saved.get().getId()).isGreaterThan(0);
    }

    @Test
    void whenEmailDoesNotExistThenFindReturnsEmpty() {
        Optional<User> result = sql2oUserRepository.findByEmailAndPassword("notfound@mail.com", "nopass");
        assertThat(result).isEmpty();
    }

    @Test
    void whenPasswordIsIncorrectThenFindReturnsEmpty() {
        var user = new User("secure@mail.com", "Sec", "secret");
        sql2oUserRepository.save(user);

        Optional<User> result = sql2oUserRepository.findByEmailAndPassword("secure@mail.com", "wrong");
        assertThat(result).isEmpty();
    }

    @Test
    void whenDuplicateEmailThenSecondSaveFails() {
        var first = sql2oUserRepository.save(new User("dupe@mail.com", "Dup1", "pass1"));
        var second = sql2oUserRepository.save(new User("dupe@mail.com", "Dup2", "pass2"));

        assertThat(first).isPresent();
        assertThat(second).isEmpty();
    }

}