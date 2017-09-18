package ru.metaconference.serverlist.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.metaconference.serverlist.data.entity.RememberMeToken;

/**
 * Created by user on 2017-09-03.
 */
@RestResource(exported = false)
public interface RememberMeTokenRepository extends CrudRepository<RememberMeToken, String> {
    void deleteAllByUsername(String username);
}
