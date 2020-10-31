package eu.mitrial.backendchallenge.controller;

import eu.mitrial.backendchallenge.beans.LiveResponse;
import eu.mitrial.backendchallenge.service.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final LiveService liveService;

    @Autowired
    public RestController(LiveService liveService) {
        this.liveService = liveService;
    }

    @GetMapping("/api/v1/live")
    public LiveResponse getLiveSearch(
            @RequestParam(defaultValue = "2020-10-01") String startDate,
            @RequestParam(required = false) String language,
            @RequestParam(defaultValue = "10") int limit) {
        return liveService.search(startDate, language, limit);
    }

}
