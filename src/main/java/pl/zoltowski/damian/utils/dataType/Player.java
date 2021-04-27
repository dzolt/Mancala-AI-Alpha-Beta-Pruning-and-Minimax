package pl.zoltowski.damian.utils.dataType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Player {
    PLAYER_A(0), PLAYER_B(1), PLAYER_AI_1(0), PLAYER_AI_2(1);

    private int number;
}
