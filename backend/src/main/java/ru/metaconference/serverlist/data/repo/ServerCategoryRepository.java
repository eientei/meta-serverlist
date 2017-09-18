package ru.metaconference.serverlist.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.metaconference.serverlist.data.entity.ServerCategory;

import javax.annotation.security.DenyAll;

/**
 * Created by user on 2017-09-03.
 */
public interface ServerCategoryRepository extends CrudRepository<ServerCategory, Long> {
    @PreAuthorize("hasPermission(#s, 'admin')")
    @Override
    <S extends ServerCategory> S save(@Param("s") S s);

    @DenyAll
    @Override
    <S extends ServerCategory> Iterable<S> save(Iterable<S> iterable);

    @DenyAll
    @Override
    void delete(Long aLong);

    @PreAuthorize("hasPermission(#serverCategory, 'admin')")
    @Override
    void delete(@Param("serverCategory") ServerCategory serverCategory);

    @DenyAll
    @Override
    void delete(Iterable<? extends ServerCategory> iterable);

    @DenyAll
    @Override
    void deleteAll();
}
