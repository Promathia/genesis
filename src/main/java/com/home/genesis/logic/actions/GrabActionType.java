package com.home.genesis.logic.actions;

public enum GrabActionType {

    /*
    0 1 2
    7 B 3
    6 5 4
    */
    GRAB_0(-1, 1),
    GRAB_1(0, 1),
    GRAB_2(1, 1),
    GRAB_3(1, 0),
    GRAB_4(1, -1),
    GRAB_5(0, -1),
    GRAB_6(-1, -1),
    GRAB_7(-1, 0);

    private int x;
    private int y;
    private CommandType commandType = CommandType.TERMINAL;

    GrabActionType(final int x, final int y) {
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
