package com.IvanChikanov.BloxCons.Controllers;

import com.IvanChikanov.BloxCons.Models.Image;
import com.IvanChikanov.BloxCons.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ImageContoller {
    @Autowired
    private ImageService imageService;

    @PostMapping("/create_image")
    public ResponseEntity<Long> firstSave(@RequestParam("image")MultipartFile image) throws IOException
    {
        return ResponseEntity.ok().body(imageService.createAndSave(image));
    }

    @PostMapping("/update_image")
    @ResponseBody
    public Long updateImage(@RequestParam("image")MultipartFile image, @RequestParam("id") Long id) throws IOException {
        return imageService.updateImage(id, image);
    }
    @GetMapping("/load_image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> showPic(@PathVariable Long id)
    {
        Image img = imageService.getImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(img.getContentType()));
        return ResponseEntity.ok().headers(headers).body(img.getData());
    }
}
