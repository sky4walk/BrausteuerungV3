/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbrausteuerungv3;

import java.util.Vector;

/**
 *
 * @author andre
 */
public class JBrausteuerungV3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BluetoothDiscovery  bt = new BluetoothDiscovery();
        Vector vec = bt.getDevices();

    }
    
}
