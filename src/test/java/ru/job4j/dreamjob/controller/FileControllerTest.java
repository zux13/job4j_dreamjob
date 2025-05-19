package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileController fileController;

    @BeforeEach
    public void initService() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Test
    public void whenGetByIdThenResponseEntityStatusOkAndHasBody() {
        var expectedStatusCode = HttpStatus.OK;
        var expectedContent = new byte[] {1, 2, 3};
        when(fileService.getFileById(1)).thenReturn(Optional.of(new FileDto("fileName", expectedContent)));

        var actualResponse = fileController.getById(1);
        var actualStatusCode = actualResponse.getStatusCode();
        var actualContent = actualResponse.getBody();

        assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
        assertThat(actualContent).isEqualTo(expectedContent);
    }

    @Test
    public void whenGetByIdThenResponseEntityStatusNotFound() {
        var expectedStatusCode = HttpStatus.NOT_FOUND;
        when(fileService.getFileById(1)).thenReturn(Optional.empty());

        var actualResponse = fileController.getById(1);
        var actualStatusCode = actualResponse.getStatusCode();

        assertThat(expectedStatusCode).isEqualTo(actualStatusCode);
        assertThat(actualResponse.hasBody()).isFalse();
    }

}