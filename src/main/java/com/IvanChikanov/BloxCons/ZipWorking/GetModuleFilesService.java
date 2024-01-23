package com.IvanChikanov.BloxCons.ZipWorking;

import com.IvanChikanov.BloxCons.Models.Cell;
import com.IvanChikanov.BloxCons.Models.Image;
import com.IvanChikanov.BloxCons.Models.ModuleUnit;
import com.IvanChikanov.BloxCons.Models.Page;
import com.IvanChikanov.BloxCons.Services.CreateAndSaveService;
import com.IvanChikanov.BloxCons.Services.ImageService;
import com.IvanChikanov.BloxCons.Services.ModuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class GetModuleFilesService implements IGetFiles{
    private Long pageId;
    private Set<Cell> allCells;

    private Map<String, byte[]> preparedFiles = new HashMap<>();
    @Autowired
    private CreateAndSaveService crServ;
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ImageService imageService;

    public void SetPageId(Long id)
    {
        pageId = id;
    }
    @Override
    public Map<String, byte[]> GetFiles() throws IOException {
        Page loadedPage = crServ.loadSavedPage(pageId);
        FindAllPageCells(loadedPage);
        preparedFiles = moduleService.getModulesFiles(allCells.stream()
                .map(result -> Long.parseLong(result.getModuleLink()))
                .collect(Collectors.toList()));
        PreparedPageJsonFile(loadedPage);
        return preparedFiles;
    }
    private void FindAllPageCells(Page loadedPage)
    {
        this.allCells = loadedPage.getGrids().stream()
                .map(grid -> grid.getCellArray()).collect(Collectors.toSet())
                .stream()
                .collect(
                        () -> new HashSet<Cell>(),
                        (set, item) -> {item.stream().forEach(cell -> set.add(cell));},
                        (set1, set2) -> set1.addAll(set2)
                );
    }
    private void PreparedPageJsonFile(Page page)
    {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Set<Cell> cells = page.getGrids().stream()
                    .map(grid -> grid.getCellArray()).collect(Collectors.toSet())
                    .stream()
                    .collect(
                            () -> new HashSet<Cell>(),
                            (set, item) -> {item.stream().forEach(cell -> set.add(cell));},
                            (set1, set2) -> set1.addAll(set2)
                    );
            cells.stream().forEach(cell -> cell.setModuleLink(moduleService.getModuleName(Long.parseLong(cell.getModuleLink()))));
            Set<ModuleUnit> mUnits = cells.stream().map(oneCell -> oneCell.getModuleUnits())
                    .collect(Collectors.toList()).stream().collect(
                            () -> new HashSet<ModuleUnit>(),
                            (set, item) -> {item.stream().forEach(unit -> set.add(unit));},
                            (set1, set2) -> set1.addAll(set2));
            for(ModuleUnit mu: mUnits)
            {
                if(mu.getImageId() != null) {
                    Image image = imageService.getImage(Long.parseLong(mu.getImageId()));
                    String[] buffExtension = image.getFileName().split("\\.");
                    String extension = buffExtension[buffExtension.length - 1];
                    mu.setImageId(String.format("%s.%s", mu.getImageId(), extension));
                    preparedFiles.put("media/" + mu.getImageId(), image.getData());
                }
            }
            String pageJson = mapper.writeValueAsString(page);
            preparedFiles.put("data.json", pageJson.getBytes(StandardCharsets.UTF_8));
        }
        catch (JsonProcessingException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
