package com.uoc.inmo.notification.api;

import com.uoc.inmo.notification.api.request.RequestMessage;
import com.uoc.inmo.notification.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/notification")
@RequiredArgsConstructor
public class NotificationApi {
    
    @Autowired
    @NonNull
    private NotificationService notificationService;

    @PostMapping("/sendMessage")
	public Boolean sendMessage(@RequestBody RequestMessage request) {
		return notificationService.sendMessage(request);
	}
}
