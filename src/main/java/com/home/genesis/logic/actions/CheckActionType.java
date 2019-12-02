package com.home.genesis.logic.actions;

public enum CheckActionType {

    /*
    0 1 2
    7 B 3
    6 5 4
    */
    CHECK_0(-1, 1),
    CHECK_1(0, 1),
    CHECK_2(1, 1),
    CHECK_3(1, 0),
    CHECK_4(1, -1),
    CHECK_5(0, -1),
    CHECK_6(-1, -1),
    CHECK_7(-1, 0);

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
