package com.IvanChikanov.BloxCons.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OtherModules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String displayName;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OtherModules> modulesSet = new HashSet<>();
    @JsonIgnore
    @ManyToMany
    private Set<Module> modules = new HashSet<>();

    private Boolean isPermanent = false;

    public OtherModules(MultipartFile file) throws IOException {
        this.code = ReadJs(file.getResource());
        this.name = file.getOriginalFilename();
        this.displayName = this.name.split(".js")[0];
    }
    public OtherModules(Resource resource) throws IOException {
        this.code = ReadJs(resource);
        this.name = resource.getFilename();
        this.displayName = this.name.split(".js")[0];
    }
    public void AddModules(OtherModules om)
    {
        this.modulesSet.add(om);
    }
    public void AddModulesMain(Module mdl)
    {
        this.modules.add(mdl);
    }
    private String ReadJs(Resource res) throws IOException {
        return res.getContentAsString(StandardCharsets.UTF_8);
    }
    public void RemoveModule(Module mdl)
    {
        this.modules.remove(mdl);
    }
    public String GetServerCode()
    {
        return String.format("import {%s} from '/js/get_other_script/%d';\n",this.displayName, this.id);
    }
    public String GetZipCode()
    {
        return String.format("import {%s} from './%s';\n", this.displayName, this.getName());
    }
}
