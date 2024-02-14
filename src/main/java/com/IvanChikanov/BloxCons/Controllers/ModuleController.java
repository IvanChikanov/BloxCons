package com.IvanChikanov.BloxCons.Controllers;

import com.IvanChikanov.BloxCons.Other.JsType;
import com.IvanChikanov.BloxCons.Services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/js")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @GetMapping("/get/{module_id}")
    public ResponseEntity<byte[]> getJS(@PathVariable Long module_id) throws IOException
    {
        byte[] created = moduleService.createFile(JsType.FULL, module_id);
        return ResponseEntity.ok().headers(getJsHeaders(created.length)).body(created);
    }
    @GetMapping("/list")
    @ResponseBody
    public List<String[]> get()
    {
        List<String[]> m = moduleService.getModuleList();
        return m;
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteModule(@PathVariable Long id)
    {
        moduleService.DeleteModule(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/get_other_script/{id}")
    public ResponseEntity<byte[]> loadResourceJsOther(@PathVariable Long id) throws IOException
    {
        byte[] other = moduleService.createOtherFile(id);
        return ResponseEntity.ok().headers(getJsHeaders(other.length)).body(other);
    }
    @PostMapping("/update_other_dependencies")
    public ResponseEntity<String> updateOthers(@RequestParam("otherIds") Long[] ids, @RequestParam("moduleId") Long moduleId)
    {
        try {
            moduleService.UpdateOtherModulesSet(ids, moduleId);
            return ResponseEntity.ok("ok");
        }
        catch (Exception ex)
        {
            return ResponseEntity.ok(ex.getMessage());
        }
    }
    private HttpHeaders getJsHeaders(int length)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(length);
        headers.setContentType(MediaType.parseMediaType("application/javascript"));
        return headers;
    }
}

