//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static FileInputStream xmlFile;
    public static int xmlBytesCount;
    public static byte[] xmlBytes;
    public static byte[] policyRequest = new byte[23];
    public static String pat = "/root/legendtanks/";

    public Main() {
    }

    public static void main(String[] args) {
        System.out.println("-> Security server started...");

        try {
            ServerSocket server = new ServerSocket(843);
            xmlFile = new FileInputStream(pat + "crossdomain.xml");
            xmlBytesCount = xmlFile.available();
            xmlBytes = new byte[xmlBytesCount + 1];
            xmlFile.read(xmlBytes, 0, xmlBytesCount);
            xmlBytes[xmlBytesCount] = 0;
            xmlFile.close();

            while(true) {
                Socket client = server.accept();
                (new Main.ProtocolTransfer(client)).start();
            }
        } catch (IOException var3) {
            System.out.println(var3.getMessage());
        }
    }

    static class ProtocolTransfer extends Thread {
        private Socket s;

        public ProtocolTransfer(Socket s) {
            this.s = s;
        }

        public void run() {
            try {
                try {
                    DataOutputStream out = new DataOutputStream(this.s.getOutputStream());
                    DataInputStream in = new DataInputStream(this.s.getInputStream());
                    in.read(Main.policyRequest, 0, 23);
                    out.write(Main.xmlBytes, 0, Main.xmlBytesCount + 1);
                    out.flush();
                } finally {
                    System.out.println(":: FSS :: Policy sended to " + this.s.toString());
                    this.s.close();
                }
            } catch (Exception var7) {
            }
        }
    }
}
