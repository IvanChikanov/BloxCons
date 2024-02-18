package com.IvanChikanov.BloxCons.Controllers;

import com.IvanChikanov.BloxCons.Models.Module;
import com.IvanChikanov.BloxCons.Models.OtherModules;
import com.IvanChikanov.BloxCons.Repositories.ModuleRepository;
import com.IvanChikanov.BloxCons.Services.ModuleService;
import com.IvanChikanov.BloxCons.StarApp.StartAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private ModuleService ms;

    @Autowired
    private StartAppService startAppService;

    @GetMapping("/get_js/{module_id}")
    public ResponseEntity<Module> getJS(@PathVariable Long module_id)
    {
        return ResponseEntity.ok().body(moduleRepository.findById(module_id).get());
    }
    @GetMapping("/add")
    public String newJsAppender(Model model)
    {
        model.addAttribute("moduls", moduleRepository.getListModule());
        model.addAttribute("others", ms.getOtherModuleList());
        return "addmodule";
    }
    @GetMapping("/other_add")
    public String getOtherScriptPage(Model model) throws IOException
    {
        startAppService.GetReservedFiles();
        model.addAttribute("otherModules", ms.getOtherModuleList());
        return "other_page";
    }
    @PostMapping("/other_add")
    public ResponseEntity<String> saveOtherModule(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("ids") @Nullable Long[] ids) throws IOException
    {
        try {
            return ResponseEntity.ok(ms.OtherModuleCreator(file, ids));
        }
        catch (Exception ex)
        {
            return ResponseEntity.ok(ex.getMessage());
        }
    }
    @GetMapping("/other_list")
    @ResponseBody
    public List<String[]> getOtherList()
    {
        return ms.getOtherModuleList();
    }

    @GetMapping("/other_instance/{id}")
    @ResponseBody
    public OtherModules getOm(@PathVariable Long id)
    {
        return ms.getOm(id);
    }

    @PostMapping("/other_instance/")
    @ResponseBody
    public void updateOm(@RequestParam("id") Long id, @RequestParam("code") String code,
                         @Nullable @RequestParam("ids") Long[] ids, @RequestParam("isPermanent") Boolean isPermanent)
    {
        ms.UpdateOm(id, code,ids, isPermanent);
    }
}
