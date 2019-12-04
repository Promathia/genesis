package com.home.genesis.logic.actions;

public enum CheckActionType {

    /*
    7 0 1
    6 B 2
    5 4 3
    */
    CHECK_0(0, 1),
    CHECK_1(1, 1),
    CHECK_2(1, 0),
    CHECK_3(1, -1),
    CHECK_4(0, -1),
    CHECK_5(-1, -1),
    CHECK_6(-1, 0),
    CHECK_7(-1, 1);

    private int x;
    private int y;
    private CommandType commandType = CommandType.NON_TERMINAL;

    CheckActionType(final int x, final int y) {
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
