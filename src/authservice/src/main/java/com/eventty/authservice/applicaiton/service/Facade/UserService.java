package com.eventty.authservice.applicaiton.service.Facade;

import com.eventty.authservice.applicaiton.dto.TokensDTO;
import com.eventty.authservice.domain.Enum.UserRole;
import com.eventty.authservice.presentation.dto.request.FullUserCreateRequestDTO;
import com.eventty.authservice.presentation.dto.request.GetNewTokensRequestDTO;
import com.eventty.authservice.presentation.dto.request.UserLoginRequestDTO;
import com.eventty.authservice.presentation.dto.response.NewTokensResponseDTO;

public interface UserService {

    Long createUser(FullUserCreateRequestDTO fullUserCreateRequestDTO, UserRole userRole);

    void validateEmailNotDuplicated(String email);

    TokensDTO login(UserLoginRequestDTO userLoginRequestDTO);

    NewTokensResponseDTO getNewTokens(GetNewTokensRequestDTO getNewTokensRequestDTO);
}
