package com.home.genesis.logic.actions;

public enum TurnActionType {

    /*
    0 1 2
    7 B 3
    6 5 4
    */
    TURN_0(0),
    TURN_1(1),
    TURN_2(2),
    TURN_3(3),
    TURN_4(4),
    TURN_5(5),
    TURN_6(6),
    TURN_7(7);

    private int turnDirection;
    private CommandType commandType = CommandType.NON_TERMINAL;

    TurnActionType(final int turnDirection) {
        this.turnDirection = turnDirection;
    }

    public int getTurnDirection() {
        return turnDirection;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
