package com.IvanChikanov.BloxCons;

import com.IvanChikanov.BloxCons.Controllers.AdminController;
import com.IvanChikanov.BloxCons.Controllers.ModuleController;
import com.IvanChikanov.BloxCons.ZipWorking.ZipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.util.List;

@SpringBootTest
public class ZipTest extends ZipService{
    @Autowired
    private ZipService zipService;

    @MockBean
    private AdminController adminController;

    @MockBean
    private ModuleController moduleController;

}
