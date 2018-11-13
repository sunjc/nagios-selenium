package org.itrunner.tests.utils;

public class MailInfo {
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String message;

    public MailInfo() {
    }

    public MailInfo(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public MailInfo(String to, String cc, String subject, String message) {
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.message = message;
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

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
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
}