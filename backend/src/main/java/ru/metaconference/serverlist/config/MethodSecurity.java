package ru.metaconference.serverlist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import ru.metaconference.serverlist.data.entity.Ownable;

import java.io.Serializable;

/**
 * Created by user on 2017-09-03.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MethodSecurity extends GlobalMethodSecurityConfiguration {
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                boolean admin = authentication.getAuthorities().stream().anyMatch(x -> "admin".equals(x.getAuthority()));
                if (admin) {
                    return true;
                }

                if ("write".equals(permission)) {
                    if (targetDomainObject instanceof Ownable) {
                        return ((Ownable) targetDomainObject).getOwner().getName().equals(authentication.getName());
                    }
                }

                return false;
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                return false;
            }
        });

        return expressionHandler;
    }
}
