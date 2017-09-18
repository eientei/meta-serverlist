package ru.metaconference.serverlist.data.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.metaconference.serverlist.data.entity.Server;

import java.util.List;

/**
 * Created by user on 2017-09-03.
 */
@Projection(name = "server", types = Server.class)
public interface ServerProjection {
    String getServerId();
    @Value("#{target.categories.![name]}")
    List<String> getCategories();
    @Value("#{target.languages.![name]}")
    List<String> getLanguages();
    String getDescription();
    String getInvite();
    Long getLikes();
}
