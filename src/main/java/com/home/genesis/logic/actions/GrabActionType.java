package com.home.genesis.logic.actions;

public enum GrabActionType {

    /*
    7 0 1
    6 B 2
    5 4 3
    */
    GRAB_0(0, 1),
    GRAB_1(1, 1),
    GRAB_2(1, 0),
    GRAB_3(1, -1),
    GRAB_4(0, -1),
    GRAB_5(-1, -1),
    GRAB_6(-1, 0),
    GRAB_7(-1, 1);

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
