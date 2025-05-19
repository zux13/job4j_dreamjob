package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegistrationPageSuccessfully() {
        var actualView = userController.getRegistrationPage();
        assertThat(actualView).isEqualTo("users/register");
    }

    @Test
    public void whenUserRegisteredSuccessfullyThenRedirectToVacanciesPage() {
        var user = new User("user@mail.ru", "name", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var actualView = userController.register(model, user);

        assertThat(actualView).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenUserWithEmailAlreadyExistsThenAddErrorMessageAndReturn404Page() {
        var expectedMessage = "Пользователь с такой почтой уже существует";

        var user = new User("user@mail.ru", "name", "password");
        when(userService.save(user)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var actualView = userController.register(model, user);
        var actualMessage = model.getAttribute("message");

        assertThat(actualView).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenGetLoginPageSuccessfully() {
        var actualView = userController.getLoginPage();
        assertThat(actualView).isEqualTo("users/login");
    }

    @Test
    public void whenUserLoginSuccessfullyThenRedirectToVacanciesPage() {
        var expectedView = "redirect:/vacancies";

        var user = new User("user@mail.ru", "name", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));

        var request = new MockHttpServletRequest();
        var model = new ConcurrentModel();
        var actualView = userController.loginUser(user, model, request);
        var session = request.getSession(false);

        assertThat(actualView).isEqualTo(expectedView);
        assertThat(session).isNotNull();
        assertThat(user).isEqualTo(session.getAttribute("user"));
    }

    @Test
    public void whenUserNotFoundThenRedirectToLoginPage() {
        var expectedView = "users/login";
        var expectedMessage = "Почта или пароль введены неверно";

        var user = new User("user@mail.ru", "name", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.empty());

        var request = new MockHttpServletRequest();
        var model = new ConcurrentModel();
        var actualView = userController.loginUser(user, model, request);
        var actualMessage = model.getAttribute("error");

        assertThat(actualView).isEqualTo(expectedView);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenLogoutEndsSessionAndRedirects() {
        var expectedView = "redirect:/users/login";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());

        var actualView = userController.logout(session);

        assertThat(actualView).isEqualTo(expectedView);
        assertTrue(session.isInvalid());
    }
}