package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IndexControllerTest {

    @Test
    public void whenGetIndexThenReturnIndexPage() {
        var expectedView = "index";

        IndexController indexController = new IndexController();
        var actualView = indexController.getIndex();

        assertThat(expectedView).isEqualTo(actualView);
    }
}