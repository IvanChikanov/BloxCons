package com.IvanChikanov.BloxCons.ModuleCreate;

import com.IvanChikanov.BloxCons.Models.Image;
import com.IvanChikanov.BloxCons.Models.Module;
import com.IvanChikanov.BloxCons.Repositories.ImageRepository;
import com.IvanChikanov.BloxCons.Repositories.ModuleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AddModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Transactional
    public Long CreateEmpty(String name) throws JsonProcessingException {
        Module module = new Module();
        module.setDisplayName(name);
        Module returned = moduleRepository.save(module);
        return returned.getId();
    }

    @Transactional
    public Map<String, List<String>> GetSavedModule(Long id)
    {
        Module module = moduleRepository.findById(id).get();
        Map<String, List<String>> moduleData = new HashMap<>();
        moduleData.put("display", new ArrayList<>(List.of(module.getDisplayName())));
        moduleData.put("main", new ArrayList<>(List.of(module.getName()+"~~"+module.getId())));
        moduleData.put("brother",new ArrayList<>(List.of(module.getBrother().getName()+"~~"+module.getBrother().getId())));
        moduleData.put("otherClasses", module.getOther().stream().sorted()
                .map(other -> other.getName()+"~~"+other.getId()).collect(Collectors.toList()));
        moduleData.put("otherModules", module.getOtherModules().stream()
                .map(generalScript -> generalScript.getId().toString())
                .collect(Collectors.toList()));
        moduleData.put("images", module.getImages().stream()
                .map(img -> img.getId().toString()).collect(Collectors.toList()));
        return moduleData;
    }
    @Transactional
    public void UpdateModuleImage(Long id, MultipartFile file) throws IOException
    {
        Image image = imageRepository.findById(id).get();
        image.setData(file.getBytes());
        image.setContentType(file.getContentType());
        image.setFileName(file.getOriginalFilename());
        imageRepository.save(image);
    }
    @Transactional
    public Long CreateNewModuleImage(Long module_id, MultipartFile file) throws IOException
    {
        Image image = new Image(file);
        image.setModule(moduleRepository.findById(module_id).get());
        Long id = imageRepository.save(image).getId();
        return id;
    }

    @Transactional
    public void UpdateModuleFile(Long module_id, MultipartFile file) throws IOException
    {
        Module module = moduleRepository.findById(module_id).get();
        module.Update(file);
        moduleRepository.save(module);
    }
    @Transactional
    public Long CreateNewModuleFile(MultipartFile file, String type, Long mainModule_id) throws IOException
    {
        Module module = new Module(file);
        Module main = moduleRepository.findById(mainModule_id).get();
        switch (type)
        {
            case "main":
                module = main;
                module.Update(file);
                break;
            case "child":
                main.setBrother(module);
                break;
            case "other":
                main.addOther(module);
                moduleRepository.save(main);
                break;
        }
        module.setType(type);
        return moduleRepository.save(module).getId();
    }
    @Transactional
    public void UpdateName(String name, Long module_id) throws IOException
    {
        Module module = moduleRepository.findById(module_id).get();
        module.setDisplayName(name);
        moduleRepository.save(module);
    }
}
