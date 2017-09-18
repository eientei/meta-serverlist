package ru.metaconference.serverlist.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by user on 2017-09-02.
 */
@Entity
@Data
public class ServerCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Column(unique = true)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "categories")
    @RestResource(exported = false)
    @JsonIgnore
    private Set<Server> servers;
}
