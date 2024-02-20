package com.IvanChikanov.BloxCons.ZipWorking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
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

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    private String[][] folders = new String[][]{
            new String[]{"styles", ""},
            new String[]{"system_js", "system/"},
            new String[]{"templates", ""},
    };
    @Override
    public Map<String, byte[]> GetFiles() throws IOException, RuntimeException {

        for(String[] path : folders)
        {
            Resource[] jsDir = resourcePatternResolver.getResources("classpath:/static/" + path[0] + "/*");
            for(var res: jsDir)
            {
                FileToMapEntry(res, path[1]);
            }
        }
        String json = "{\"type\":\"iframe\", \"entryPoint\":\"index.html\"}";
        preparedFiles.put("eom.json", json.getBytes(StandardCharsets.UTF_8));
        AddSystemNotText("images/backgrounds", "media/backgrounds/");
        AddSystemNotText("fonts", "fonts/");
        return preparedFiles;
    }

    private void FileToMapEntry(Resource resource, String folderName) throws IOException
    {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream())))
        {
            StringBuilder sb = new StringBuilder();
            int character;
            while ((character = br.read()) != -1)
            {
                sb.append((char) character);
            }
            String allText = sb.toString();
            for ( var entry : GetSystemFilesService.replacedPaths.entrySet())
            {
                allText = allText.replaceAll(entry.getKey(), entry.getValue());
            }
            preparedFiles.put(folderName.concat(resource.getFilename()), allText.getBytes(StandardCharsets.UTF_8));
        }
    }
    private void AddSystemNotText(String path, String endPath) throws IOException
    {
        Resource[] files = resourcePatternResolver.getResources("classpath:/static/" + path + "/*");
        for(var file : files)
        {
            try(BufferedInputStream bis = new BufferedInputStream(file.getInputStream()))
            {
                preparedFiles.put(endPath + file.getFilename(), bis.readAllBytes());
            }
        }
    }
}
