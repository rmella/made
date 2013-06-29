/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.velonuboso.basicmade;

/**
 *
 * @author Ruben
 */
class Position {
    
    public static Position NULL_POSITION;
    static{
        NULL_POSITION = new Position(-1,-1);
    }
    
    public int x, y;

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    
}
