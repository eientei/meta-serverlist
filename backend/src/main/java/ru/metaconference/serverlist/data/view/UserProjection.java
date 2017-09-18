package ru.metaconference.serverlist.data.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.metaconference.serverlist.data.entity.User;

import java.util.List;

/**
 * Created by user on 2017-09-03.
 */
@Projection(name = "user", types = User.class)
public interface UserProjection {
    String getName();

    String getEmail();

    @Value("#{target.servers.![serverId]}")
    List<String> getServers();

    @Value("#{target.groups.![name]}")
    List<String> getGroups();
}
