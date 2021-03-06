package com.ssafy.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameStatusRes2 {
    String keyword;
    String question;
    int round;
    Long gameStatus;
}
