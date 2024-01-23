package com.IvanChikanov.BloxCons.Controllers;

import com.IvanChikanov.BloxCons.Services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/empty")
    public String getEmptyCons()
    {
        return "main_cons";
    }

    @GetMapping(value = "/load/{id}", produces = "text/javascript")
    public ResponseEntity<Resource> getModule(@PathVariable Long id) throws IOException {
            //File mdl = moduleService.getJsModule(id);
            //Resource fileRes = new FileSystemResource(mdl);
            return ResponseEntity.ok().build();
    }
    @GetMapping("/player/{id}")
    public String getPlayer(@PathVariable Long id, Model model)
    {
        model.addAttribute("id", id);
        return "player";
    }
}
