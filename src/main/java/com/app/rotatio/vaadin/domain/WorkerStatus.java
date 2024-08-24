package com.app.rotatio.vaadin.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkerStatus {
    PRESENT(1),
    ABSENT(2),
    UNEMPLOYED(3);

    private final int value;

    public static WorkerStatus fromValue(int value) {
        return switch (value) {
            case 1 -> PRESENT;
            case 2 -> ABSENT;
            case 3 -> UNEMPLOYED;
            default -> null;
        };
    }

    public static WorkerStatus[] getPossibleForUsers() {
        WorkerStatus[] values = new WorkerStatus[WorkerStatus.values().length - 1];
        values[0] = PRESENT;
        values[1] = ABSENT;
        return values;
    }
}
