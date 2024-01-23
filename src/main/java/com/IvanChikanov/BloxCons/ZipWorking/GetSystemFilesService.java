package com.IvanChikanov.BloxCons.ZipWorking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class GetSystemFilesService implements IGetFiles {

    private static Map<String, String> replacedPaths = new HashMap<>(){
        {
            put("../static/system_js/", "./system/");
            put("../system_js/", "./");
            put("../static/styles/", "./");
            put("../images/", "./media/");
            put("../fonts/", "./fonts/");
            put("HTMLFather as HTML", "HTML");
            put("HTMLFather.js", "HTML.js");
            put("/js/get/", "./");
        }
    };
    private Map<String, byte[]> preparedFiles = new HashMap<>();
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    public Map<String, byte[]> GetFiles() throws IOException, RuntimeException {
        Resource jsDir = resourceLoader.getResource("classpath:/static");
        String folder = "fonts/";
        for (File systemFolder : jsDir.getFile().listFiles()) {
            switch (systemFolder.getName()) {
                case "styles":
                    folder = "";
                    for (File style : systemFolder.listFiles()) {
                        FileToMapEntry(style, folder);
                    }
                    break;
                case "fonts":
                    for (File font : systemFolder.listFiles()) {
                        try(FileInputStream fis = new FileInputStream(font))
                        {
                            preparedFiles.put(folder + font.getName(), fis.readAllBytes());
                        }
                    }
                    break;
                case "system_js":
                    folder = "system/";
                    for(File min : Arrays.stream(systemFolder.listFiles())
                            .filter(file -> file.getName().contains(".min."))
                            .collect(Collectors.toList()))
                    {
                        FileToMapEntry(min, folder);
                    }
                    break;
                case "templates":
                    for(File template: systemFolder.listFiles())
                    {
                        FileToMapEntry(template, "");
                    }
            }
        }
        String json = "{\"type\":\"iframe\", \"entryPoint\":\"index.html\"}";
        preparedFiles.put("eom.json", json.getBytes(StandardCharsets.UTF_8));
        AddSystemImages();
        return preparedFiles;
    }
    private void FileToMapEntry(File file, String folderName) throws IOException
    {
        try(FileReader fr = new FileReader(file, StandardCharsets.UTF_8))
        {
            StringBuilder sb = new StringBuilder();
            int character;
            while ((character = fr.read()) != -1)
            {
                sb.append((char) character);
            }
            String allText = sb.toString();
            for ( var entry : GetSystemFilesService.replacedPaths.entrySet())
            {
                allText = allText.replaceAll(entry.getKey(), entry.getValue());
            }
            preparedFiles.put(folderName.concat(file.getName()), allText.getBytes(StandardCharsets.UTF_8));
        }
    }
    private void AddSystemImages() throws IOException
    {
        File images = resourceLoader.getResource("classpath:/static/images/backgrounds").getFile();
        for(File img: images.listFiles())
        {
            try(FileInputStream fis = new FileInputStream(img))
            {
                preparedFiles.put("media/backgrounds/" + img.getName(), fis.readAllBytes());
            }
        }
    }
}
