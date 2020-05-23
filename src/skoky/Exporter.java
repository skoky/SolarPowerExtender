package skoky;

import cn.com.voltronic.solar.data.bean.WorkInfo;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Exporter {

    DatagramSocket socket;
    InetAddress address;

    public Exporter() {

        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            address = InetAddress.getLocalHost();
        } catch (Exception e) {
            try {
                Files.write(File.createTempFile("sp1-", "txt").toPath(), e.getMessage().getBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void exportData(WorkInfo workInfo) {
        if (workInfo != null) {
            String data = String.format("SPV1;getWorkMode:%s;getBatteryVoltage:%s;getPvInputPower1:%s;" +
                            "at:%d;getChargingCurrent:%s",
                    workInfo.getWorkMode(), workInfo.getPBatteryVoltage(),
                    workInfo.getPvInputPower1(), System.currentTimeMillis(),
                    workInfo.getChargingCurrent());

            broadcast(data);
        }
    }

    private void broadcast(String broadcastMessage) {

        try {

            byte[] buffer = broadcastMessage.getBytes(StandardCharsets.UTF_8);

            DatagramPacket packet
                    = new DatagramPacket(buffer, buffer.length, address, 5555);
            try {
                socket.send(packet);
            } catch (IOException e) {
                try {
                    Files.write(File.createTempFile("sp2-", "txt").toPath(), e.getMessage().getBytes());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        } catch (Exception e) {
            try {
                Files.write(File.createTempFile("sp3-", "txt").toPath(), e.getMessage().getBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
