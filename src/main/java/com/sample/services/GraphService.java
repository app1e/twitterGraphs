package com.sample.services;

import com.sample.structure.Graph;
import org.springframework.social.twitter.api.TwitterProfile;

/**
 * Author: alexey.ivlev
 * Date: 07.09.15
 */
public interface GraphService {
    void addEdge(Graph graph, TwitterProfile v, TwitterProfile w);
    String graphToJson(Graph graph);
    void writeJsonGraphToFile(String jsonString);
}
