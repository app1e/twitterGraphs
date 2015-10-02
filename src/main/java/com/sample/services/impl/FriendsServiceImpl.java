package com.sample.services.impl;

import com.sample.services.FriendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: alexey.ivlev
 * Date: 05.09.15
 */
@Service
public class FriendsServiceImpl implements FriendsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendsServiceImpl.class);

    @Override
    public List<TwitterProfile> getFriends(Twitter twitter, TwitterProfile profile, int friendsCount) {
        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends(profile.getScreenName());
        List<TwitterProfile> result = new ArrayList<>(friends);
        while(friends.getNextCursor() != 0 && result.size() < friendsCount){
            friends = twitter.friendOperations().getFriendsInCursor(profile.getScreenName(), friends.getNextCursor());
            result.addAll(friends);
        }
        LOGGER.info("Getting friends was executed successfully");
        return result;
    }
}
