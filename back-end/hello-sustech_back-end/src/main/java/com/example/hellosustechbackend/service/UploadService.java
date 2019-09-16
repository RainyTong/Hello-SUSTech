package com.example.hellosustechbackend.service;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class UploadService {

    /**
     * Execute one cmd using a new thread
     * @param cmd the command to be executed
     * @return {@code String} the output of the command
     * @throws IOException IOException
     */
    public String readConsole(String cmd) throws IOException {
        StringBuilder cmdout = new StringBuilder();
        Process process = Runtime.getRuntime().exec(cmd);     //执行一个系统命令
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
            cmdout.append(line).append(System.getProperty("line.separator"));
        }
        in.close();
        try {
            System.err.println(process.waitFor());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return cmdout.toString();
//
//        String command = "cmd /c python <command to execute or script to run>";
//        Process p = Runtime.getRuntime().exec(command);
//        p.waitFor();
//        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//        String line;
//        while ((line = bri.readLine()) != null) {
//            System.out.println(line);
//        }
//        bri.close();
//        while ((line = bre.readLine()) != null) {
//            System.out.println(line);
//        }
//        bre.close();
//        p.waitFor();
//        System.out.println("Done.");
//
//        p.destroy();
//        return ret;
    }
}
