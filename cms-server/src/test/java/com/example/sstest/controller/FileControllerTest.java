package com.example.sstest.controller;

import com.example.sstest.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileControllerTest {

    @Autowired
    private FileController fileController;

    @Test
    void deleteFile() {
        Result result = fileController.deleteFile("https://yanghi-save-1312993591.cos.ap-nanjing.myqcloud.com/img/2023/1/19/616ca8c1-ebf7-4725-af00-fafe0a50c036.png");
    }
}