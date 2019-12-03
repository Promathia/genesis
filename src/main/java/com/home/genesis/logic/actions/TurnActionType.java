package com.home.genesis.logic.actions;

public enum TurnActionType {

    /*
    7 0 1
    6 B 2
    5 4 3
    */
    TURN_0(0, "\u2191"),
    TURN_1(1, "\u2197"),
    TURN_2(2, "\u2192"),
    TURN_3(3, "\u2198"),
    TURN_4(4, "\u2193"),
    TURN_5(5, "\u2199"),
    TURN_6(6, "\u2190"),
    TURN_7(7, "\u2196");

    private int turnDirection;
    private String arrowSign;
    private CommandType commandType = CommandType.NON_TERMINAL;

    TurnActionType(final int turnDirection, final String arrowSign) {
        this.turnDirection = turnDirection;
        this.arrowSign = arrowSign;
    }

    public int getTurnDirection() {
        return turnDirection;
    }

    public String getArrowSign() {
        return arrowSign;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
