package com.IvanChikanov.BloxCons.StarApp;

import com.IvanChikanov.BloxCons.Models.OtherModules;
import com.IvanChikanov.BloxCons.Repositories.OtherModulesRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StartAppService {

    private Set<InsideFile> sortedFiles = new HashSet<>();
    private String peacePath = "classpath:/static/reservedOtherModules/";
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OtherModulesRepo otherModulesRepo;

    @PostConstruct
    public void GetReservedFiles() throws IOException
    {
        this.createInsideFiles();
        CheckInDataBase();
    }
    @Transactional
    private void CheckInDataBase() throws IOException {

        List<OtherModules> toSavedBuffer = new ArrayList<>();
        for(var file : sortedFiles.stream().sorted().collect(Collectors.toList()))
        {
            if(otherModulesRepo.findByName(file.name) == null)
            {
                OtherModules newOther = new OtherModules(resourceLoader.getResource(peacePath + file.name));
                newOther = otherModulesRepo.save(newOther);
                for(var depFile: file.dependencies)
                {
                    OtherModules oms = otherModulesRepo.findByName(depFile);
                    newOther.AddModules(oms);
                }
                newOther.setIsPermanent(file.isPermanent);
                toSavedBuffer.add(newOther);
            }
        }
        toSavedBuffer.stream().forEach(unit -> System.out.println(unit.getId()));
        otherModulesRepo.saveAll(toSavedBuffer);
    }
    private void createInsideFiles()
    {
        sortedFiles.add(new InsideFile("Radio.js", new String[]{}, true));
        sortedFiles.add(new InsideFile("HTML.js", new String[]{}, true));
        sortedFiles.add(new InsideFile("Popup.js", new String[]{"HTML.js"}, true));
        sortedFiles.add(new InsideFile("ToolsKeeper.js", new String[]{"Radio.js", "HTML.js"}, true));
        sortedFiles.add(new InsideFile("UComm.js", new String[]{}, false));
        sortedFiles.add(new InsideFile("Windows.js", new String[]{"HTML.js"}, true));
    }
    private class InsideFile implements Comparable<InsideFile>
    {
        private String name;
        private String[] dependencies;

        private boolean isPermanent;
        private int depSize;
        InsideFile(String name, String[] dependencies, boolean perm)
        {
            this.name = name;
            this.dependencies = dependencies;
            this.depSize = this.dependencies.length;
            this.isPermanent = perm;
        }
        @Override
        public int compareTo(InsideFile o) {
            if(this.depSize > o.depSize)
            {
                return 1;
            } else if (this.depSize < o.depSize) {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
}
