package ru.metaconference.serverlist.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by user on 2017-09-03.
 */
@Entity(name = "app_user")
@Data
public class User implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    @ReadOnlyProperty
    private String name;

    @Email
    private String email;

    @NotEmpty
    @RestResource(exported = false)
    @JsonIgnore
    private String passwordHash;

    @OneToMany(mappedBy = "owner")
    @RestResource(exported = false)
    @JsonIgnore
    private Set<Server> servers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    @ReadOnlyProperty
    private Set<Group> groups;

    @Override
    @RestResource(exported = false)
    @JsonIgnore
    public User getOwner() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return passwordHash != null ? passwordHash.equals(user.passwordHash) : user.passwordHash == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        return result;
    }
}
