package org.itrunner.tests.utils;

public class MailInfo {
    private String from;
    private String to;
    private String cc;
    private String subject;
    private String message;
    private String server;

    public MailInfo(String to, String cc, String subject, String message) {
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.message = message;
        this.from = "sjc-925@163.com";
        this.server = "smtp.office365.com";
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}