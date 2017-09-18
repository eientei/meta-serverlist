package ru.metaconference.serverlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.metaconference.serverlist.controller.data.UserCreateRequest;
import ru.metaconference.serverlist.data.entity.Group;
import ru.metaconference.serverlist.data.entity.User;
import ru.metaconference.serverlist.data.repo.GroupRepository;
import ru.metaconference.serverlist.data.repo.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 2017-09-02.
 */
@RestController
@RequestMapping("/api/users")
public class Users {
    private final DelegatingSecurityContextExecutorService elevatedExecutor;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Users(UserRepository userRepository, PasswordEncoder passwordEncoder, DelegatingSecurityContextExecutorService elevatedExecutor, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.elevatedExecutor = elevatedExecutor;
        this.groupRepository = groupRepository;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public User create(@RequestBody @Valid UserCreateRequest userCreateRequest) throws ExecutionException, InterruptedException {
        User user = new User();
        user.setName(userCreateRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userCreateRequest.getPassword()));
        user.setEmail(userCreateRequest.getEmail());

        elevatedExecutor.submit(() -> {
            userRepository.save(user);
        }).get();

        return user;
    }

    @RequestMapping(path = "/elevate/{user}")
    public void elevate(@PathVariable("user") String username, HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (!request.getRemoteAddr().equals("127.0.0.1")) {
            throw new AccessDeniedException("elevation prohibited");
        }

        elevatedExecutor.submit(() -> {
            User user = userRepository.findByName(username);
            if (user != null) {
                Group group = groupRepository.findByName("admin");
                if (group == null) {
                    group = new Group();
                    group.setName("admin");
                    groupRepository.save(group);
                }
                user.getGroups().add(group);
                userRepository.save(user);
            }
        }).get();
    }

}
