package com.IvanChikanov.BloxCons.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String displayName;

    private String type;
    @Column(columnDefinition = "TEXT")
    private String code;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Module brother;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Module> other = new HashSet<>();

    @OneToMany(mappedBy = "module", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Image> images = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OtherModules> otherModules = new HashSet<>();

    public Module(MultipartFile file) throws IOException
    {
        Update(file);
    }
    public void Update(MultipartFile file) throws IOException
    {
        this.code = ReadJs(file.getResource());
        this.name = file.getOriginalFilename();
    }
    public void addOther(Module other)
    {
        this.other.add(other);
    }
    public void addGeneral(OtherModules om)
    {
        this.otherModules.add(om);
        om.AddModulesMain(this);
    }
    private String ReadJs(Resource res) throws IOException {
        return res.getContentAsString(StandardCharsets.UTF_8);
    }
    public void UpdateGenerals(Set<OtherModules> ids)
    {
        for(var i : ids)
        {
            if(!this.otherModules.contains(i))
            {
                addGeneral(i);
            }
        }
        Iterator<OtherModules> iter = this.otherModules.iterator();
        while(iter.hasNext())
        {
           OtherModules oms = iter.next();
           if(!ids.contains(oms))
           {
                oms.RemoveModule(this);
                iter.remove();
           }
        }

    }
    public Map<String, Long> getImageMap()
    {
        Map<String, Long> map = new HashMap<>();
        for(Image img: this.images)
        {
            map.put(img.getFileName(), img.getId());
        }
        return map;
    }
    public String getFullCode(Map<String, Long> images)
    {
        String result = this.code;
        for(var img : images.keySet())
        {
            result  = result.replaceAll(img, images.get(img).toString());
        }
        return result;
    }
}
