package com.eventty.gateway.global.exception.token;

import com.eventty.gateway.global.exception.ErrorCode;
import com.eventty.gateway.global.exception.JwtException;

public class RefreshTokenIsEmptyException extends JwtException {

    public RefreshTokenIsEmptyException() { super(ErrorCode.REFRESH_TOKEN_IS_EMPTY); }
}
