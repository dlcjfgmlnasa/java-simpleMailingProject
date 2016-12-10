package ���ϸ�������Ʈ;

import javax.mail.Authenticator;

public class MyAuthenticator extends Authenticator{
    private String id;
    private String pw;

    public MyAuthenticator(String id, String pw) {
        this.id = id;
        this.pw = pw;
   }

    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication(id, pw);
    }
}
