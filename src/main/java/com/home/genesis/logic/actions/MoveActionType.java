package com.home.genesis.logic.actions;

public enum MoveActionType {

    /*
    7 0 1
    6 B 2
    5 4 3
    */
    MAKE_STEP_0(0, 1),
    MAKE_STEP_1(1, 1),
    MAKE_STEP_2(1, 0),
    MAKE_STEP_3(1, -1),
    MAKE_STEP_4(0, -1),
    MAKE_STEP_5(-1, -1),
    MAKE_STEP_6(-1, 0),
    MAKE_STEP_7(-1, 1);

    private int x;
    private int y;
    private CommandType commandType = CommandType.TERMINAL;

    MoveActionType(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
