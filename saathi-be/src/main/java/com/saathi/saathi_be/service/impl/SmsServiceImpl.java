package com.saathi.saathi_be.service.impl;

import com.saathi.saathi_be.configuration.TwilioConfig;
import com.saathi.saathi_be.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final TwilioConfig twilioConfig;

    @Override
    public void sendSms(String to, String message) {
        Message response = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioConfig.getPhoneNumber()),
                message
        ).create();

        System.out.println("SMS sent to " + to + " with SID: " + response.getSid());
    }
}
