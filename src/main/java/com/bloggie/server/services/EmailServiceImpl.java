package com.bloggie.server.services;

import com.bloggie.server.domain.PasswordResetToken;
import com.bloggie.server.repositories.PrtRepository;
import com.bloggie.server.repositories.UsersRepository;
import com.bloggie.server.security.responses.StateResponse;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private final UsersRepository usersRepository;
    private final PrtRepository prtRepository;

    @Value("${app.email.service}")
    private String sgkey;
    @Value("${app.host}")
    private String domain;
    @Value("${app.email.from}")
    private String fromEmail;

    public EmailServiceImpl(UsersRepository usersRepository, PrtRepository prtRepository) {
        this.usersRepository = usersRepository;
        this.prtRepository = prtRepository;
    }


    @Override
    public StateResponse resetPasswordEmail(String email) throws IOException {

        if(usersRepository.existsByEmail(email)) {

            String resetToken = UUID.randomUUID().toString();

            PasswordResetToken resetRequest = new PasswordResetToken();
            resetRequest.setToken(resetToken);
            resetRequest.setEmail(email);

            prtRepository.save(resetRequest);

            String url = domain + "/reset-password/" + resetToken;

            Email to = new Email(email);
            Email from = new Email(fromEmail);

            String subject = "Password reset request";

            String anchor = "<a href=\"" + url + "\">" + "Reset password</a>";
            String message = "Please follow the link to reset password: " + anchor;
            Content content = new Content("text/html", message);
            Mail mail = new Mail(from, subject, to, content);


            SendGrid sg = new SendGrid(sgkey);

            Request request = new Request();
                  try {
                      request.setMethod(Method.POST);
                      request.setEndpoint("mail/send");
                      request.setBody(mail.build());
                      Response response = sg.api(request);

                    } catch (IOException ex) {
                      throw ex;
                    }
        }


        return new StateResponse(true);
    }
}
