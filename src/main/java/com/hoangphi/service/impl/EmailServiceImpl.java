package com.hoangphi.service.impl;

import com.hoangphi.constant.Constant;
import com.hoangphi.service.email.EmailService;
import com.hoangphi.utils.MailUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final MailUtils mailUtils;
    @Override
    public void sendVerificationEmail(HttpServletRequest req, String email, UUID otp) {
        String verificationLink= Constant.BASE_URL+"verify?code="+otp;
        try{
            Map<String,String> map=new HashMap<>();
            map.put("action_url",verificationLink);
            map.put("name",req.getAttribute("username").toString());
            mailUtils.sendTemplateEmail(email,"Active your account!","active",map);
        }catch(Exception e){
            e.printStackTrace();

        }


    }

    @Override
    public void confirmResetPassword(HttpServletRequest req, String email, UUID token) {

    }
}
