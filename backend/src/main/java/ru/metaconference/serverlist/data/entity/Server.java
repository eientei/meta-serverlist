package ru.metaconference.serverlist.data.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by user on 2017-09-02.
 */
@Entity
@Data
public class Server implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    @ReadOnlyProperty
    private String serverId;

    @ManyToMany
    @JoinTable
    private Set<ServerCategory> categories;

    @ManyToMany
    @JoinTable
    private Set<ServerLanguage> languages;

    private String description;

    @ManyToOne
    @NotNull
    private User owner;

    @NotEmpty
    private String invite;

    @ReadOnlyProperty
    private Long likes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Server server = (Server) o;

        if (id != null ? !id.equals(server.id) : server.id != null) return false;
        if (serverId != null ? !serverId.equals(server.serverId) : server.serverId != null) return false;
        if (description != null ? !description.equals(server.description) : server.description != null) return false;
        if (owner != null ? !owner.equals(server.owner) : server.owner != null) return false;
        if (invite != null ? !invite.equals(server.invite) : server.invite != null) return false;
        return likes != null ? likes.equals(server.likes) : server.likes == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (serverId != null ? serverId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (invite != null ? invite.hashCode() : 0);
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        return result;
    }
}
