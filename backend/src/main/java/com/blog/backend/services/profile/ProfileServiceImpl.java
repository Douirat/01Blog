package com.blog.backend.services.profile;

import com.blog.backend.services.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import com.blog.backend.dtos.user.PaginatedUsersDTO;
// import com.blog.backend.dtos.
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<PaginatedUsersDTO> fetchUsers(int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        Page<> posts = userRepository.findAll(pageable);
    }

}