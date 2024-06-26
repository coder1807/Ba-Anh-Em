package com.example.movietickets.demo.service;

import com.example.movietickets.demo.Provider;
import com.example.movietickets.demo.Role;
import com.example.movietickets.demo.model.User;
import com.example.movietickets.demo.repository.IRoleRepository;
import com.example.movietickets.demo.repository.IUserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;


    // Lưu người dùng mới vào cơ sở dữ liệu sau khi mã hóa mật khẩu.
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }


    // Gán vai trò mặc định cho người dùng dựa trên tên người dùng.
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                    userRepository.save(user);
                },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );
    }


    // Tải thông tin chi tiết người dùng để xác thực.
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }


    // Tìm kiếm người dùng dựa trên tên đăng nhập.
    public Optional<User> findByUsername(String username)  {
        return userRepository.findByUsername(username);
    }

   /* public void saveOauthUser(String email, @NotNull String username) {
        if (userRepository.findByUsername(username).isPresent())
            return;
        var user = new User();
        user.setUsername(username);

        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username)); user.setProvider(Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }*/

    // Kiểm tra sự tồn tại của username.
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Kiểm tra sự tồn tại của email.
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Kiểm tra sự tồn tại của phone.
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public void saveOauthUser(String email, @NotNull String username) {
        if (userRepository.findByUsername(username).isPresent())
            return;
        var user = new User();
        user.setUsername(username);

        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username)); user.setProvider (Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }

}