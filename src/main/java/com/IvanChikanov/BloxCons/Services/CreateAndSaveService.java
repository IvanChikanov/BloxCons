package com.IvanChikanov.BloxCons.Services;

import com.IvanChikanov.BloxCons.Models.*;
import com.IvanChikanov.BloxCons.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CreateAndSaveService {
    @Autowired
    private ImageService imageService;
    @Autowired
    private CellRepository cellRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private ModuleUnitRepository mdlRep;

    public Page createPage(String name)
    {
        Page page = new Page(name);
        Page savedPage = pageRepository.save(page);
        return savedPage;
    }
    public List<String[]> loadAllPages()
    {
        return pageRepository.getPageList();
    }
    public Page loadSavedPage(Long id)
    {
        Page p =  pageRepository.findById(id).get();
        return p;
    }
    public void saveGeneralPage(Page buff)
    {
        Page inBase = pageRepository.findById(buff.getId()).get();
        inBase.setTheme(buff.getTheme());
        inBase.setMath(buff.getMath());
        pageRepository.save(inBase);
    }
    public Grid saveGrid(Long page_id, int rows)
    {
        Page page = pageRepository.findById(page_id).get();
        Grid grid = new Grid(rows);
        page.AddGrid(grid);
        pageRepository.save(page);
        Grid buff = gridRepository.save(grid);
        return buff;
    }
    public Cell addCell(Long grid_id)
    {
        Cell cell = cellRepository.save(new Cell());
        Grid grid = gridRepository.findById(grid_id).get();
        cell = grid.addCell(cell);
        gridRepository.save(grid);
        return cell;
    }

    public void updateCells(Cell[] cells)
    {
        for(Cell cell: cells)
        {
            cellRepository.updateCell(cell.getModuleLink(), cell.getNumber(), cell.getId());
        }
    }

    public void deleteCell(Long id)
    {
        try {
            Cell cell = cellRepository.findById(id).get();
            cell.getGrid().deleteCell(cell);
            List<ModuleUnit> units = mdlRep.getModels(cell);
            mdlRep.deleteAll(units);
            cellRepository.deleteById(id);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public ModuleUnit getEmptyUnit(Long cell_id)
    {
        Cell cell = cellRepository.findById(cell_id).get();
        ModuleUnit mdl = new ModuleUnit();
        mdl.setCell(cell);
        ModuleUnit newMd = mdlRep.save(mdl);
        cell.AddUnit(newMd);
        return newMd;
    }
    public void deleteUnit(Long id)
    {
        mdlRep.deleteById(id);
    }
    public void UpdateImage(MultipartFile file, Long id) throws IOException
    {
        imageService.updateImage(id, file);
    }
    public void SaveUnit(ModuleUnit moduleUnit)
    {
        mdlRep.UpdateUnit(moduleUnit.getImageId(), moduleUnit.getTextContent(),
                moduleUnit.getOtherJsonOption(), moduleUnit.getTag(), moduleUnit.getId());
    }

    public List<ModuleUnit> getUnits(Long cell_id)
    {
        return mdlRep.getModels(cellRepository.findById(cell_id).get());
    }
    public void deleteGrid(Long id)
    {
        gridRepository.deleteById(id);
    }
}
