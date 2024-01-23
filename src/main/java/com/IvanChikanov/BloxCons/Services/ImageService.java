package com.IvanChikanov.BloxCons.Services;

import com.IvanChikanov.BloxCons.Models.Image;
import com.IvanChikanov.BloxCons.Repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Long createAndSave(MultipartFile file) throws IOException
    {
        return imageRepository.save(new Image(file)).getId();
    }
    public Long updateImage(Long id, MultipartFile file) throws IOException
    {
        Image img = imageRepository.findById(id).get();
        img.Update(file);
        return imageRepository.save(img).getId();
    }
    public void DeleteImage(Image img)
    {
        imageRepository.delete(img);
    }
    public void DeleteAnId(Long id)
    {
        DeleteImage(imageRepository.findById(id).get());
    }
    public byte[] showImage(Long id)
    {
        return imageRepository.findById(id).get().getData();
    }
    public Image getImage(Long id)
    {
        return imageRepository.findById(id).get();
    }
    public void UpdateUnit(Image image)
    {
        imageRepository.save(image);
    }
}
