package com.sample.services;

import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.List;

/**
 * Author: alexey.ivlev
 * Date: 05.09.15
 */
public interface FriendsService {
    List<TwitterProfile> getFriends(Twitter twitter, TwitterProfile profile, int friendsCount);
}
