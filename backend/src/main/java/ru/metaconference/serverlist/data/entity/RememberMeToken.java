package ru.metaconference.serverlist.data.entity;

import lombok.Data;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by user on 2017-09-03.
 */
@Entity
@Data
public class RememberMeToken {
    @Id
    private String series;
    private String username;
    private String value;
    private Date date;

    public RememberMeToken() {
    }

    public RememberMeToken(PersistentRememberMeToken token) {
        series = token.getSeries();
        username = token.getUsername();
        value = token.getTokenValue();
        date = token.getDate();
    }
}
