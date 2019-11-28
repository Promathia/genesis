package com.home.genesis.general;

public abstract class Constants {

    public static final int SIZE_X = 1800;
    public static final int SIZE_Y = 760;
    public static final int TILE_SIZE = 30;
    public static final int CELL_NUMBER_X = 50;
    public static final int CELL_NUMBER_Y = 25;

    public static final int OBSTACLE_NUMBER_ON_MAP = 4;
    public static final int OBSTACLE_SIZE = 5;

    public static final int BOT_INITIAL_HEALTH = 30;
    public static final int BOT_MAX_HEALTH = 70;
    public static final int BOT_DNA_COMMANDS = 64;
    public static final int BOT_MIN_NUMBER = 8;
    public static final int BOT_MAX_NUMBER = 64;

    public static final int FOOD_CALORIES = 10;
    public static final int FOOD_NUMBER = 30;

    public static final int POISON_NUMBER = 20;

    /*
    * this three commands include pointer transfer in DNA
    * depending on the current action result
    * if poison -> 1
    * if obstacle -> 2
    * if other bot -> 3
    * if food -> 4
    * if empty -> 5
    * 0 - 7 make step - terminal commands -> to next bot
    * 8 - 15 get food or neutralize poison - terminal commands -> to next bot
    * 16 - 23 check - can be executed 10 times then -> to next bot
    *
    * pointer moves to next DNA action
    * 24 - 31 turn - can be executed 10 times then -> to next bot
    *
    * pointer moves to number equal to the actual action
    * 32 - 63 unconditional transfer - can be executed 10 times then -> to next bot
    * */

}
