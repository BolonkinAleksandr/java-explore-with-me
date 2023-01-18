package ru.practicum.explorewithmemain.service.adminservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.NewUserRequest;
import ru.practicum.explorewithmemain.dto.UserDto;
import ru.practicum.explorewithmemain.mapper.UserMapper;
import ru.practicum.explorewithmemain.model.User;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.repository.UserRepository;
import ru.practicum.explorewithmemain.service.adminservice.AdminUserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;


    @Override
    public List<UserDto> getUsersByIds(List<Long> ids, Integer from, Integer size) {
        final Pageable pageable = CustomPageable.of(from, size);
        List<User> listUsers;
        if (ids.isEmpty()) {
            listUsers = userRepository.findAll(pageable).getContent();
        } else {
            listUsers = userRepository.findById(ids, pageable).getContent();
        }
        log.info("get users ids={}", ids);
        return UserMapper.toListUserDto(listUsers);
    }


    @Override
    @Transactional
    public UserDto addUser(NewUserRequest userRequest) {
        User user = UserMapper.toUser(userRequest);
        userRepository.saveAndFlush(user);
        log.info("add user name={}", user.getName());
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        log.info("delete user userId={}", userId);
        userRepository.deleteById(userId);
    }
}
