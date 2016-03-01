package ru;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by eljah32 on 2/29/2016.
 */
public class BTLoopSender implements Runnable {

    BluetoothSPP bluetoothSPP;
    TransferObject to;

    BTLoopSender(BluetoothSPP bluetoothSPP, TransferObject to) {
        this.bluetoothSPP = bluetoothSPP;
        this.to = to;
    }

    public void run() {

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bluetoothSPP.send(new String(to.getBytes()), true);
        }

    }
}
