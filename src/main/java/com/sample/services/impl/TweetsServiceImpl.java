package com.sample.services.impl;

import com.sample.services.TweetsService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: alexey.ivlev
 * Date: 01.10.15
 */
@Service
public class TweetsServiceImpl implements TweetsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TweetsServiceImpl.class);

    @Override
    public List<Tweet> getTweets(Twitter twitter, String screenName) {
        List<Tweet> result = twitter.timelineOperations().getUserTimeline(screenName);
        LOGGER.info("Getting tweets was executed successfully");
        return result;
    }

    @Override
    public String tweetsToJson(List<Tweet> tweets) {
        JSONArray json = new JSONArray();
        if(tweets.size() != 0){
            for(Tweet tweet : tweets){
                JSONObject jsonTweet = new JSONObject();
                jsonTweet.put("retweetCount", tweet.getRetweetCount());
                jsonTweet.put("text", tweet.getText());
                if(tweet.hasTags()){
                    jsonTweet.put("tags", tweet.getEntities().getHashTags().get(0).getText());
                } else {
                    jsonTweet.put("tags", tweet.getText().split("\\s+")[0]);
                }
                json.add(jsonTweet);
            }
        }
        LOGGER.info("Tweets were successfully converted to JSON format");
        return json.toJSONString();
    }
}
