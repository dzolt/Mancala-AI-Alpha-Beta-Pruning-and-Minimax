package pl.zoltowski.damian.utils.dataType;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoveStatus {
    COMPLETED(1), REQUIRES_ANOTHER_MOVE(2), INCORRECT(3), END_GAME(4), START_GAME(5);

    private int number;
}
