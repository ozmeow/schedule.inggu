package ru.wzrdmhm.schedule_inggu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wzrdmhm.schedule_inggu.dto.BotResponse;
import ru.wzrdmhm.schedule_inggu.dto.CommandRequest;
import ru.wzrdmhm.schedule_inggu.service.CommandService;

@RestController
@RequestMapping("/api")
public class CommandController {

    @Autowired
    private CommandService commandProcessorService;

    //Single Responsibility Principle
    @PostMapping("/command")
    public BotResponse handleCommand(@RequestBody CommandRequest request) {
        BotResponse response = commandProcessorService.processCommand(request);
        return response;
    }
}
