package com.birariro.dailydevblogassemble.adapter.email;

import com.birariro.dailydevblogassemble.domain.library.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailAdapter {

    private final EmailAuthCode emailAuthCode;
    private final EmailToDayDocuments emailToDayDocuments;


    public void authenticationCodeSend(String email, String authCode){

        emailAuthCode.execute(email,authCode);
    }

    public void toDayDocumentsSend(List<Document> documentList){
        emailToDayDocuments.execute(documentList);
    }

}
