package com.sample.services;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.List;

/**
 * Author: alexey.ivlev
 * Date: 01.10.15
 */
public interface TweetsService {
    List<Tweet> getTweets(Twitter twitter, String screenName);
    String tweetsToJson(List<Tweet> tweets);
}
