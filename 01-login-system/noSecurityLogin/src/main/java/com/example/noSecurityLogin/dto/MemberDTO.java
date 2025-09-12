package com.example.noSecurityLogin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

    /*
    나중에 수정하면 좋음 (개선사항)
    - LoginDTO와 JoinDTO를 분리하면 좋음
    이유 :
    1. 보안 : 불필요한 필드 노출 최소화
    2. 명확성 : 각 기능의 목적이 명확
    3. 유지보수 : 약관동의, 자동로그인 등 나중에 추가할 때 서로 영향을 주지 않는다.
     */

    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String phoneNumber;


}
