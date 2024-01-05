package com.niklas.game;

class Player {
    private String name;
    private int health;
    private int damage;

    public Player(String name, int health, int damage){
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public String getName () {
        return name;
    }

    public int getHealth () {
        return health;
    }

    public int getDamage () {
        return damage;
    }
}