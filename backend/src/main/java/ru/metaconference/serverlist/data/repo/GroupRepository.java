package ru.metaconference.serverlist.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.metaconference.serverlist.data.entity.Group;

import javax.annotation.security.DenyAll;

/**
 * Created by user on 2017-09-03.
 */
public interface GroupRepository extends CrudRepository<Group, Long> {
    @PreAuthorize("hasPermission(#s, 'admin')")
    @Override
    <S extends Group> S save(@Param("s") S s);

    @DenyAll
    @Override
    <S extends Group> Iterable<S> save(Iterable<S> entities);

    @DenyAll
    @Override
    void delete(Long aLong);

    @PreAuthorize("hasPermission(#group, 'admin')")
    @Override
    void delete(@Param("group") Group group);

    @DenyAll
    @Override
    void delete(Iterable<? extends Group> entities);

    @DenyAll
    @Override
    void deleteAll();

    Group findByName(String name);
}
