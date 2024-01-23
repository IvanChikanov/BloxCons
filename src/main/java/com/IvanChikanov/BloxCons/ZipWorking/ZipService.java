package com.IvanChikanov.BloxCons.ZipWorking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Scope("prototype")
public class ZipService {

    @Autowired
    private GetSystemFilesService getSystemFilesService;

    @Autowired
    private GetModuleFilesService getModuleFilesService;

    public Resource StartLoadingZip(Long pageId) throws IOException
    {

        ByteArrayOutputStream zip = new ByteArrayOutputStream();
        try(ZipOutputStream zos = new ZipOutputStream(zip))
        {
            IGetFilesWriter(getSystemFilesService, zos);
            getModuleFilesService.SetPageId(pageId);
            IGetFilesWriter(getModuleFilesService, zos);
        }
        return new ByteArrayResource(zip.toByteArray());
    }
    private void WriteFile(String name, byte[] file, ZipOutputStream zos) throws IOException
    {
        ZipEntry entry = new ZipEntry(String.format("%s", name));
        zos.putNextEntry(entry);
        zos.write(file);
        zos.closeEntry();
    }
    private void IGetFilesWriter(IGetFiles iGetFiles, ZipOutputStream zos) throws IOException {
        iGetFiles.GetFiles().forEach((key, value) -> {
            try {
                WriteFile(key, value, zos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
