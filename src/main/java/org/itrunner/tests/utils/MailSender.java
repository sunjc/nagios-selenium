package org.itrunner.tests.utils;

public class MailSender {
    private MailSender() {
    }

    public static void send(MailInfo mailInfo) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", getCommand(mailInfo));
        Process process = processBuilder.start();
        process.waitFor();
    }

    private static String getCommand(MailInfo mail) {
        String sendEmailCommand = "echo \"" + mail.getMessage() + "\" | mail";

        if (mail.getCc() != null) {
            sendEmailCommand += " -c" + mail.getCc();
        }

        if (mail.getBcc() != null) {
            sendEmailCommand += " -b" + mail.getBcc();
        }

        sendEmailCommand += " -s \"" + mail.getSubject() + "\" " + mail.getTo();
        return sendEmailCommand;
    }
}