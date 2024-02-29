package com.IvanChikanov.BloxCons.Controllers;

import com.IvanChikanov.BloxCons.Models.*;
import com.IvanChikanov.BloxCons.Services.CreateAndSaveService;
import com.IvanChikanov.BloxCons.ZipWorking.ZipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    CreateAndSaveService createServ;

    @Autowired
    ZipService zipService;

    @PostMapping("/create")
    @ResponseBody
    public Page CreatePage(@RequestBody String name)
    {
        return createServ.createPage(name);
    }
    @PostMapping("/load_pages")
    @ResponseBody
    public List<String[]> loadPageList(@RequestBody @Nullable String text)
    {
        List<String[]> p = createServ.loadAllPages(text);
        return p;
    }
    @GetMapping("/load_page/{id}")
    @ResponseBody
    public ResponseEntity<Page> loadPage(@PathVariable Long id)
    {
        Page p = createServ.loadSavedPage(id);
        return ResponseEntity.ok().body(p);
    }
    @PostMapping("/save_page")
    @ResponseBody
    public ResponseEntity<String> savePage(@RequestBody Page page)
    {
        createServ.saveGeneralPage(page);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/save_mg/{page_id}/{rowsCount}")
    @ResponseBody
    public Grid SaveGrid(@PathVariable Long page_id, @PathVariable int rowsCount)
    {
        return createServ.saveGrid(page_id, rowsCount);
    }
    @GetMapping("/pages/new_cell/{grid_id}")
    @ResponseBody
    public Cell addCell(@PathVariable Long grid_id)
    {
        return createServ.addCell(grid_id);
    }

    @PostMapping("/pages/update_cells")
    @ResponseBody
    public ResponseEntity<String> updateCells(@RequestBody Cell[] cells)
    {
        System.out.println(cells.length);
        createServ.updateCells(cells);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/pages/delete_cell/{cell_id}")
    @ResponseBody
    public ResponseEntity<Entity> deleteCell(@PathVariable Long cell_id)
    {
        createServ.deleteCell(cell_id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/pages/get_empty_unit/{cell_id}")
    public ResponseEntity<ModuleUnit> emptyUnit(@PathVariable Long cell_id)
    {
        try{
            return ResponseEntity.ok().body(createServ.getEmptyUnit(cell_id));
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/pages/save_unit")
    @ResponseBody
    public void SaveUnit(@RequestBody ModuleUnit moduleUnit)
    {
        createServ.SaveUnit(moduleUnit);
    }

    @GetMapping("/pages/get_all_units/{cell_id}")
    public ResponseEntity<List<ModuleUnit>> getAllUnits(@PathVariable Long cell_id)
    {
        try {
            return ResponseEntity.ok().body(createServ.getUnits(cell_id));
        }
        catch (IllegalArgumentException ex)
        {
            return null;
        }
    }
    @GetMapping("/loadzip/{page_id}")
    public ResponseEntity<Resource> getZip(@PathVariable Long page_id) throws IOException
    {
        /*Page page = createServ.loadSavedPage(page_id);
        ObjectMapper om = new ObjectMapper();
        File file = File.createTempFile("json", ".json");
        try(FileOutputStream fos = new FileOutputStream(file))
        {
            fos.write(om.writeValueAsBytes(page));
        }
        Resource res = new FileSystemResource(file);*/
        Resource res = zipService.StartLoadingZip(page_id);
        return ResponseEntity.ok().body(res);
    }
    @GetMapping("/pages/delete_unit/{id}")
    @ResponseBody
    public void delUnit(@PathVariable Long id)
    {
        createServ.deleteUnit(id);
    }

    @GetMapping("/pages/delete_grid/{id}")
    @ResponseBody
    public void delGrid(@PathVariable Long id)
    {
        createServ.deleteGrid(id);
    }
}
