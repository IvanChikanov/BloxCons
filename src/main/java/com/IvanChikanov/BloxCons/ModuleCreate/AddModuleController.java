package com.IvanChikanov.BloxCons.ModuleCreate;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/add_module/")
public class AddModuleController {
    @Autowired
    private AddModuleService addModuleService;
    @PostMapping("/get_empty_module")
    @ResponseBody
    public ResponseEntity<Long> createEmptyModule(@RequestParam("displayedName") String name) throws JsonProcessingException {
        Long id = addModuleService.CreateEmpty(name);
        return ResponseEntity.ok(id);
    }
    @GetMapping("/get_saved_module/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> getSavedModule(@PathVariable Long id)
    {
        Map<String, List<String>> result = addModuleService.GetSavedModule(id);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/update_image")
    @ResponseBody
    public ResponseEntity<String> updateImage(@RequestParam("mainModule_id") Long module_id,
                                              @Nullable @RequestParam("image_id") Long image_id,
                                              @RequestParam("file")MultipartFile file)
    {
        try {
            if(image_id != null)
            {
                addModuleService.UpdateModuleImage(image_id, file);
                return ResponseEntity.ok("update");
            }
            else
            {
                Long id = addModuleService.CreateNewModuleImage(module_id, file);
                return ResponseEntity.ok(id.toString());
            }
        }
        catch (IOException ex)
        {
            return ResponseEntity.ok(ex.getMessage());
        }
    }
    @PostMapping("/update_module_file")
    @ResponseBody
    public ResponseEntity<String> updateModule(@RequestParam("mainModule_id") Long mainModule_id,
                                               @Nullable @RequestParam("module_id") Long module_id,
                                               @RequestParam("file")MultipartFile file,
                                               @Nullable @RequestParam("type") String type)
    {
        try {
            if(module_id != null)
            {
                addModuleService.UpdateModuleFile(module_id, file);
                return ResponseEntity.ok("update");
            }
            else
            {
                Long id = addModuleService.CreateNewModuleFile(file, type, mainModule_id);
                return ResponseEntity.ok(id.toString());
            }
        }
        catch (IOException ex)
        {
            return ResponseEntity.ok(ex.getMessage());
        }
    }
    @PostMapping("/update_displayed_name")
    @ResponseBody
    public void updateDisplayedName(@RequestParam("name") String name, @RequestParam("module_id") Long module_id) throws IOException
    {
        addModuleService.UpdateName(name, module_id);
    }
}
