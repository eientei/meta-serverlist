package ru.metaconference.serverlist.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.metaconference.serverlist.data.entity.Server;
import ru.metaconference.serverlist.data.view.ServerProjection;

import javax.annotation.security.DenyAll;

/**
 * Created by user on 2017-09-02.
 */
@RepositoryRestResource(excerptProjection = ServerProjection.class)
public interface ServerRepository extends CrudRepository<Server, Long> {
    @PreAuthorize("hasPermission(#s, 'write')")
    @Override
    <S extends Server> S save(@Param("s") S s);

    @DenyAll
    @Override
    <S extends Server> Iterable<S> save(Iterable<S> iterable);

    @DenyAll
    @Override
    void delete(Long aLong);

    @PreAuthorize("hasPermission(#server, 'admin')")
    @Override
    void delete(@Param("server") Server server);

    @DenyAll
    @Override
    void delete(Iterable<? extends Server> iterable);

    @DenyAll
    @Override
    void deleteAll();
}
