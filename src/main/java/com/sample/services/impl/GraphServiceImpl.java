package com.sample.services.impl;

import com.sample.services.GraphService;
import com.sample.structure.Graph;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Author: alexey.ivlev
 * Date: 07.09.15
 */
@Service
public class GraphServiceImpl implements GraphService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServiceImpl.class);
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String FILE_PATH = "static" + SEPARATOR + "js" + SEPARATOR + "data.json";

    @Override
    public void addEdge(Graph graph, TwitterProfile v, TwitterProfile w) {
        graph.addEdge(v, w);
        LOGGER.info("Edges were added");
    }

    @Override
    public String graphToJson(Graph graph) {
        JSONObject json = new JSONObject();
        JSONArray nodes = new JSONArray();
        JSONArray edges = new JSONArray();
        graph.getVertices();
        Set<Integer> ids = new HashSet<>();
        for(Object v: graph.getVertices().keySet()){
            JSONObject node = new JSONObject();
            Random random = new Random();
            node.put("id", "n" + ((TwitterProfile) v).getId());
            node.put("label", ((TwitterProfile)v).getName());
            node.put("screenName", ((TwitterProfile)v).getScreenName());
            node.put("x", random.nextInt(1000) - 500);
            node.put("y", random.nextInt(1000) - 500);
            node.put("size", (int)(Math.random() * 3) + 1);
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            node.put("color", "rgb(" + r + "," + g + "," + b + ")");
            nodes.add(node);
            Set<TwitterProfile> set = (Set<TwitterProfile>) graph.getVertices().get(v);
            Random rand = new Random();
            for(TwitterProfile pr : set){
                int id;
                while(!ids.contains((id = rand.nextInt(1000000) + 1))){
                    ids.add(id);
                    break;
                }
                JSONObject edge = new JSONObject();
                edge.put("id", "e" + id);
                edge.put("source", "n" + ((TwitterProfile) v).getId());
                edge.put("target", "n" + pr.getId());
                edges.add(edge);
            }
        }
        json.put("edges", edges);
        json.put("nodes", nodes);
        LOGGER.info("Graph was successfully converted to JSON format");
        return json.toJSONString();
    }

    @Override
    public void writeJsonGraphToFile(String jsonString) {
        String filePath;
        ClassLoader classLoader = getClass().getClassLoader();
        URL url  = classLoader.getResource(FILE_PATH);
        Writer out = null;
        try {
            filePath = URLDecoder.decode(String.valueOf(url.getFile()), "UTF-8");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            out.write(jsonString);
            LOGGER.info("Writing to a file was successful");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }
}
