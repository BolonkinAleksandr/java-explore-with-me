package ru.practicum.explorewithmemain.service.adminservice;

import ru.practicum.explorewithmemain.dto.NewUserRequest;
import ru.practicum.explorewithmemain.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    List<UserDto> getUsersByIds(List<Long> ids, Integer from, Integer size);

    UserDto addUser(NewUserRequest userDto);

    void deleteUserById(Long userId);
}
