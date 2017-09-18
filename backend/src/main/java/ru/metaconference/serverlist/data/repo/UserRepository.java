package ru.metaconference.serverlist.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.metaconference.serverlist.data.entity.User;
import ru.metaconference.serverlist.data.view.UserProjection;

import javax.annotation.security.DenyAll;

/**
 * Created by user on 2017-09-03.
 */
@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface UserRepository extends CrudRepository<User, Long> {
    @PreAuthorize("hasPermission(#s, 'write')")
    @Override
    <S extends User> S save(@Param("s") S s);

    @DenyAll
    @Override
    <S extends User> Iterable<S> save(Iterable<S> iterable);

    @DenyAll
    @Override
    void delete(Long aLong);

    @PreAuthorize("hasPermission(#user, 'admin')")
    @Override
    void delete(@Param("user") User user);

    @DenyAll
    @Override
    void delete(Iterable<? extends User> iterable);

    @DenyAll
    @Override
    void deleteAll();

    User findByName(String name);
}
