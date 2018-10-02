package org.itrunner.tests.utils;

import java.util.ArrayList;
import java.util.List;

public class MailSender {
    private MailSender() {
    }

    public static void send(MailInfo mailInfo) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(getCommand(mailInfo));
        Process process = processBuilder.start();
        process.waitFor();
    }

    private static List<String> getCommand(MailInfo mail) {
        List<String> command = new ArrayList<>();
        command.add("/usr/bin/sendEmail");
        command.add("-f");
        command.add(mail.getFrom());
        command.add("-t");
        command.add(mail.getTo());
        if (mail.getCc() != null) {
            command.add("-cc");
            command.add(mail.getCc());
        }
        command.add("-u");
        command.add(mail.getSubject());
        command.add("-m");
        command.add(mail.getMessage());
        command.add("-s");
        command.add(mail.getServer());
        command.add("-o");
        command.add("message-charset=UTF-8 message-content-type=text");
        return command;
    }
}