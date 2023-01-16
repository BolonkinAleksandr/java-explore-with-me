package ru.practicum.explorewithmemain.controller.admincontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.dto.NewUserRequest;
import ru.practicum.explorewithmemain.dto.UserDto;
import ru.practicum.explorewithmemain.service.adminservice.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("get users ids {}, from={}, size={}", ids, from, size);
        return adminUserService.getUsersByIds(ids, from, size);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest userRequest) {
        log.info("create user userDto {}", userRequest);
        return adminUserService.addUser(userRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("delete user userId={}", userId);
        adminUserService.deleteUserById(userId);
    }
}

