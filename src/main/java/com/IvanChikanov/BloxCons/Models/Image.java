package com.IvanChikanov.BloxCons.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.sql.Update;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String contentType;
    @Lob
    @Column(columnDefinition = "mediumblob", length = 4194304)
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id")
    private ModuleUnit unit;

    @ManyToOne
    @JoinColumn(name = "module")
    private Module module;

    public Image(MultipartFile fl) throws IOException
    {
        Update(fl);
    }
    public Long Update(MultipartFile fl) throws IOException
    {
        this.contentType = fl.getContentType();
        this.fileName = fl.getOriginalFilename();
        this.data = fl.getBytes();
        return this.id;
    }
}
