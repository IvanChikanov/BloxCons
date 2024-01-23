package com.IvanChikanov.BloxCons.Services;

import com.IvanChikanov.BloxCons.Models.Image;
import com.IvanChikanov.BloxCons.Models.Module;
import com.IvanChikanov.BloxCons.Models.OtherModules;
import com.IvanChikanov.BloxCons.Other.JsType;
import com.IvanChikanov.BloxCons.Repositories.ModuleRepository;
import com.IvanChikanov.BloxCons.Repositories.OtherModulesRepo;
import com.IvanChikanov.BloxCons.Interfaces.FileCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class ModuleService implements FileCreator {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private OtherModulesRepo otherModulesRepo;
    @Autowired
    private ImageService imageService;


    public List<String[]> getModuleList()
    {
        return moduleRepository.getListModule();
    }
    public byte[] createFile(JsType jsType, Long id) throws IOException
    {
        Module module = moduleRepository.findById(id).get();
        String alltext = new String();
        if(jsType == JsType.FULL)
        {
            for(var i : module.getOtherModules())
            {
                alltext += i.GetServerCode();
            }
            alltext += module.getFullCode(module.getImageMap()) + "\n";
            alltext += "let mdl = " + module.getBrother().getFullCode(module.getImageMap()) + "\n";
            for(Module m : module.getOther())
            {
                alltext += m.getFullCode(module.getImageMap()) + "\n";
            }
            alltext += "export{mdl}";
        }
        return fileCreator(alltext);
    }
    public void DeleteModule(Long id)
    {
        moduleRepository.deleteById(id);
    }

    public String OtherModuleCreator(MultipartFile file, Long[] ids) throws IOException {
        try {
            OtherModules om = new OtherModules(file);
            if(ids != null) {
                for (Long i : ids) {
                    om.AddModules(otherModulesRepo.getReferenceById(i));
                }
            }
            otherModulesRepo.save(om);
            return "ok";
        }
        catch (Exception ex)
        {
            return ex.getMessage();
        }
    }
    public List<String[]> getOtherModuleList()
    {
        return otherModulesRepo.getOthersList();
    }
    public byte[] createOtherFile(Long id) throws IOException
    {
        String code = new String();
        OtherModules oms = otherModulesRepo.getReferenceById(id);
        for(OtherModules om : oms.getModulesSet())
        {
            code += om.GetServerCode();
        }
        code += oms.getCode();
        return fileCreator(code);
    }

    public OtherModules getOm(Long id)
    {
        return otherModulesRepo.findById(id).get();
    }
    public void UpdateOm(Long id, String code, Long[] ids, Boolean isPerm)
    {
        OtherModules om = otherModulesRepo.findById(id).get();
        om.setCode(code);
        om.setIsPermanent(isPerm);
        List<OtherModules> oms = otherModulesRepo.findAllById(Arrays.asList(ids));
        om.setModulesSet(new HashSet<>(oms));
        otherModulesRepo.save(om);
    }
    public Map<String, byte[]> getModulesFiles(List<Long> ids) throws IOException
    {
        List<Module> modules = moduleRepository.findAllByIdIn(ids);
        Map<String, byte[]> mainModules = createModuleFiles(modules);
        Set<OtherModules> om = modules.stream()
                .map(mdl -> mdl.getOtherModules())
                .collect(()-> new HashSet<>(), (set, item) -> set.addAll(item), (set1, set2) -> set1.addAll(set2));
        Map<String, byte[]> otherModules = CreateOtherModulesFiles(om);
        mainModules.putAll(otherModules);
        return mainModules;
    }
    private Map<String, byte[]> createModuleFiles(List<Module> modules) throws IOException
    {
        Map<String, byte[]> files = new HashMap<>();
        for(Module module: modules)
        {
            String codeText = module.getOtherModules().stream().filter(om -> om.getIsPermanent())
                    .map(other -> other.GetZipCode()).collect(Collectors.joining());
            codeText = codeText.concat("import {App} from './mainApp.min.js'; \n");
            codeText = codeText.concat("let mdl = ");
            codeText = codeText.concat(module.getCode()
                    .replaceAll("/load_image/", "./media/") + "\n");
            codeText = codeText.concat(module.getOther().stream()
                    .map(otherMd -> otherMd.getCode() + "\n").collect(Collectors.joining()));
            codeText = codeText.concat("export{mdl}");
            files.put("system/" + module.getName(), codeText.getBytes(StandardCharsets.UTF_8));
        }
        return files;
    }
    private Map<String, byte[]> CreateOtherModulesFiles(Set<OtherModules> oms)
    {
        Map<String, byte[]> files = new HashMap<>();
        for(OtherModules om: oms)
        {
            if(om.getIsPermanent()) {
                String code = om.getModulesSet().stream().map(other -> other.GetZipCode() + "\n").collect(Collectors.joining());
                code += om.getCode();
                files.put("system/" + om.getName(), code.getBytes(StandardCharsets.UTF_8));
            }
        }
        return files;
    }
    public String getModuleName(Long id)
    {
        return moduleRepository.findById(id).get().getName();
    }
}
