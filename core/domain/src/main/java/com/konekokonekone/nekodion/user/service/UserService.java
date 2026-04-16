package com.konekokonekone.nekodion.user.service;

import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.user.dto.UserDto;
import com.konekokonekone.nekodion.user.entity.User;
import com.konekokonekone.nekodion.user.mapper.UserMapper;
import com.konekokonekone.nekodion.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    /**
     * 初回ログインユーザーをDBに保存
     * @param auth0Id Auth0のユーザーID
     * @param email email
     */
    public void saveUserIfNotExists(String auth0Id, String email) {
        userRepository.findByAuth0Id(auth0Id)
                .orElseGet(() -> {
                    var user = new User();
                    user.setAuth0Id(auth0Id);
                    user.setEmail(email);
                    return userRepository.save(user);
                });
    }

    /**
     * Auth0のユーザーIDからユーザーを取得
     * @param auth0Id Auth0のユーザーID
     * @return ユーザー
     */
    public UserDto findByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません。"));
    }
}
