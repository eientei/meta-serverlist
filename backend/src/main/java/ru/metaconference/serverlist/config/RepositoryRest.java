package ru.metaconference.serverlist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

/**
 * Created by user on 2017-09-02.
 */
@Configuration
public class RepositoryRest extends RepositoryRestConfigurerAdapter {
    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/api").setMaxPageSize(100);
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
    }
}
