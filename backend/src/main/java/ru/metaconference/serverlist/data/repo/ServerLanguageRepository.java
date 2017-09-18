package ru.metaconference.serverlist.data.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.metaconference.serverlist.data.entity.ServerLanguage;

import javax.annotation.security.DenyAll;

/**
 * Created by user on 2017-09-03.
 */
public interface ServerLanguageRepository extends CrudRepository<ServerLanguage, Long> {
    @PreAuthorize("hasPermission(#s, 'admin')")
    @Override
    <S extends ServerLanguage> S save(@Param("s") S s);

    @DenyAll
    @Override
    <S extends ServerLanguage> Iterable<S> save(Iterable<S> iterable);

    @DenyAll
    @Override
    void delete(Long aLong);

    @PreAuthorize("hasPermission(#serverLanguage, 'admin')")
    @Override
    void delete(@Param("serverLanguage") ServerLanguage serverLanguage);

    @DenyAll
    @Override
    void delete(Iterable<? extends ServerLanguage> iterable);

    @DenyAll
    @Override
    void deleteAll();
}
