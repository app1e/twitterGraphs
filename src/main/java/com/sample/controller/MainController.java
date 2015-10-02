package com.sample.controller;

import com.sample.services.FriendsService;
import com.sample.services.GraphService;
import com.sample.services.TweetsService;
import com.sample.structure.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(MainController.PAGE_URL)
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    static final String PAGE_URL = "/";
    private static final int FRIENDS_COUNT = 40;
    private static  final String PAGE_VIEW = "hello";
    private Twitter twitter;
    private ConnectionRepository connectionRepository;
    private FriendsService friendsService;
    private GraphService graphService;
    private TweetsService tweetsService;

    @Autowired
    public MainController(Twitter twitter,
                          ConnectionRepository connectionRepository,
                          FriendsService friendsService,
                          GraphService graphService,
                          TweetsService tweetsService) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
        this.friendsService = friendsService;
        this.graphService = graphService;
        this.tweetsService = tweetsService;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloTwitter(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            LOGGER.info("User not authenticated");
            return "redirect:/connect/twitter";
        }

        Graph<TwitterProfile> graph = new Graph<>();
        TwitterProfile me = twitter.userOperations().getUserProfile();
        model.addAttribute(me);
        CursoredList<TwitterProfile> myFriends = twitter.friendOperations().getFriends();
        for(TwitterProfile p: myFriends){
            graphService.addEdge(graph, me, p);
            List<TwitterProfile> fof = friendsService.getFriends(twitter, p, FRIENDS_COUNT);
            for (TwitterProfile fp : fof){
                graphService.addEdge(graph, p, fp);
            }
        }
        String result = graphService.graphToJson(graph);
        graphService.writeJsonGraphToFile(result);
        model.addAttribute("friends", myFriends);
        model.addAttribute("graph", graph);
        return PAGE_VIEW;
    }

    @RequestMapping(value = "/getTags/{screenName}", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> getTags(@PathVariable("screenName") String screenName){
        List<Tweet> tweets = tweetsService.getTweets(twitter, screenName);
        Map<String, String> map = new HashMap<>();
        String json = tweetsService.tweetsToJson(tweets);
        map.put("tweets", json);
        return map;
    }
}
