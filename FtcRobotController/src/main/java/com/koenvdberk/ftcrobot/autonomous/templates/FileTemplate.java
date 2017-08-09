package com.koenvdberk.ftcrobot.autonomous.templates;


import android.os.Environment;

import com.koenvdberk.ftcrobot.autonomous.AutonomousScheduler;
import com.koenvdberk.ftcrobot.autonomous.actions.Actions;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileTemplate implements Template {

    // Syntax:
    // [action] [value] ([names])
    // Actions are defines by Actions.java enum
    // Names separated by commas

    public static final String
            FILE_NAME_PREFIX = "template_",
            FILE_DIR = "ftc_templates";

    private File file;

    public FileTemplate(String fileName) {
        System.out.println("External storage directory: " + Environment.getExternalStorageDirectory().getAbsolutePath());
        File dir = new File(Environment.getExternalStorageDirectory(), FILE_DIR);
        dir.mkdirs();
        setFile(new File(dir, FILE_NAME_PREFIX + fileName + ".ftt"));
    }

    @Override
    public void addActions(AutonomousScheduler scheduler, HardwareMap hardwareMap) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(getFile()));
            String line;
            while ((line = fileReader.readLine()) != null) {

                String[] arguments = line.split(" ");
                if(arguments.length < 2) {
                    continue;
                }

                Actions action = Actions.valueOf(arguments[0]);
                if(action == null) {
                    continue;
                }

                String value = arguments[1];

                String[] names = null;
                if(arguments.length >= 3) {
                    names = arguments[2].split(",");
                }

                scheduler.add(action.getValueParser().getRunnable(hardwareMap, value, names));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
