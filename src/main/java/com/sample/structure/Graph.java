package com.sample.structure;

import org.springframework.social.twitter.api.TwitterProfile;

import java.util.*;

/**
 * Created by alexey.ivlev on 27.08.2015.
 */
public class Graph<V> {

    public Map<V, Set<V>> getVertices() {
        return vertices;
    }

    private Map<V, Set<V>> vertices;

    public Graph() {
        this.vertices = new TreeMap<>(new Comparator<V>() {
            @Override
            public int compare(V v1, V v2) {
                if(((TwitterProfile)v1).getId() == ((TwitterProfile)v2).getId()){
                    return 0;
                }else {
                    return ((TwitterProfile)v1).getId() > ((TwitterProfile)v2).getId() ? 1 : -1;
                }

            }
        });
    }

    public boolean hasVertex(V v) {
        return vertices.containsKey(v);
    }

    public void addVertex(V v){
        if(!hasVertex(v)){
            vertices.put(v, new HashSet<V>());
        }
    }

    public void addEdge(V v, V w){
        if(!hasVertex(v)) addVertex(v);
        if(!hasVertex(w)) addVertex(w);
        vertices.get(v).add(w);
        vertices.get(w).add(v);
    }

    private void validateVertex(V v) {
        if (!hasVertex(v)) throw new IllegalArgumentException(v + " is not a vertex");
    }

    public boolean hasEdge(V v, V w) {
        validateVertex(v);
        validateVertex(w);
        return vertices.get(v).contains(w);
    }

    public Set<V> getNeighbors(V v){
        if(!hasVertex(v)) throw new  IllegalArgumentException("There is no vertex - " + v);
        else  return vertices.get(v);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for(Map.Entry<V, Set<V>> entry : vertices.entrySet()){
            s.append(entry.getKey().toString() + ": ");
            for(V w : entry.getValue()){
                vertices.get(entry);
                s.append(w.toString() + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}
