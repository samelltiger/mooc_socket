package net.qiujuer.lesson.socketdemo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP 提供者，用于提供服务
 */
public class UDPProvider {


    public static class Provider extends Thread {
        private final String sn;
        private boolean done = false;
        private DatagramSocket ds = null;

        Provider(String sn) {
            super();
            this.sn = sn;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("UDPProvider Started.");

            try {
                // 作为接受者，需要指定接收数据的端口
                ds = new DatagramSocket(20000);

                while (!done) {

                    // 构建接收实体
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

                    // 开始接收
                    ds.receive(receivePack);

                    // 打印接收到的信息与发送者信息
                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(), 0, dataLen);
                    System.out.println("UDPProvider receive from IP:" + ip + "\tPort:" + port + "\tData:" + data);

                    int responsePort = MessageCreator.parsePort(data);
                    if (responsePort != -1) {
                        // 构建一份回送数据
                        String responseData = MessageCreator.buildWithSn(sn);
                        byte[] responseDataBytes = responseData.getBytes();
                        // 直接回送到发送者上，发送者的ip与端口
                        DatagramPacket responsePack = new DatagramPacket(responseDataBytes,
                                responseDataBytes.length,
                                receivePack.getAddress(),
                                responsePort);

                        // 回送
                        ds.send(responsePack);
                    }
                }
            } catch (Exception ignored) {
            } finally {
                close();
            }

            // 结束
            System.out.println("UDPProvider Finished.");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        void exit() {
            done = true;
            close();
        }
    }
}
