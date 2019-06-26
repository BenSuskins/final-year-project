package uk.co.suskins.hrvsm.api.Controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.suskins.hrvsm.service.TwitterService;

@Api(tags = "HSSM-Twitter-Rest-Api")
@RestController
@RequestMapping("/twitter")
public class HSSMTwitterRestApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(HSSMTwitterRestApi.class);

    private final TwitterService twitterService;

    @Autowired
    public HSSMTwitterRestApi(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @ApiOperation(value = "For connecting to the Twitter Realtime Stream API.")
    @PostMapping("/start")
    public void twitterStartStream() {
        LOGGER.info("twitterStartStream: post /twitter/start");
        twitterService.twitterStartStream();
    }

    @ApiOperation(value = "For disconnecting from the Twitter Realtime Stream API.")
    @PostMapping("/stop")
    public void twitterStopStream() {
        LOGGER.info("twitterStopStream: post /twitter/stop");
        twitterService.twitterStopStream();
    }

    @ApiOperation(value = "For checking connection to the Twitter Realtime Stream API.")
    @GetMapping("/status")
    public boolean twitterStatus() {
        LOGGER.info("twitterStatus: get /twitter/status");
        return twitterService.getTwitterConnectionStatus();
    }
}
