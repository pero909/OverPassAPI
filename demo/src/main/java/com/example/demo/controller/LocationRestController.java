package com.example.demo.controller;

import com.example.demo.model.Location;
import com.example.demo.model.Node;
import com.example.demo.repository.LocationJpaRepository;
import com.example.demo.repository.NodeJpaRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LocationRestController {
   private final NodeJpaRepository nodeJpaRepository;
   private final LocationJpaRepository locationJpaRepository;

    public LocationRestController(NodeJpaRepository nodeJpaRepository
            ,LocationJpaRepository locationJpaRepository){
        this.locationJpaRepository = locationJpaRepository;
        this.nodeJpaRepository=nodeJpaRepository;
    }


    @GetMapping("/test")
    public void Test() throws IOException {
        // Coordinates of the bounding box of the area of interest
        String bbox = "41.2351,22.3477,44.2178,28.6079"; //(-90,-180,90,180)-> this is for the whole world


        String query = "[out:json][timeout:25];" +              // You can modify this query however you like
                "(" +
                "  way[\"type\"=\"route\"][\"route\"=\"hiking\"]({{bbox}});" +
                ");" +
                "out body;" +
                ">;" +
                "out skel qt;";

        // Replace "{{bbox}}" in the query with the bounding box coordinates
        query = query.replace("{{bbox}}", bbox);

        try {
            // Create URL object with the Overpass API endpoint and the query
            URL url = new URL("https://overpass-api.de/api/interpreter?data=" + query);
            String url1= "https://overpass-api.de/api/interpreter?data=" + query;

            // Read the response from the URL into a string
            RestTemplate restTemplate=  new RestTemplate();
            String response = restTemplate.getForObject(url1,String.class);

            // Parse the response string as a JSONObject
            JSONParser parser = new JSONParser();
            Object object= parser.parse(response);
            JSONObject jsonObject = (JSONObject) object;

            // Call helper methods to add nodes and locations to the database
            AddNodes(jsonObject);
            AddLocations(jsonObject);


            // Write the string to a new JSON file in the current directory        This was to test it locally on my pc
//            FileWriter fileWriter = new FileWriter("hiking_trails(test).json");
//            PrintWriter printWriter = new PrintWriter(fileWriter);
//            printWriter.print(response);
//            printWriter.close();
            System.out.println("JSON file returned successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    // Helper method to add nodes to the database
    public void AddNodes(JSONObject jsonObject) throws IOException, ParseException {

//        FileReader reader = new FileReader("D:\\Upwork\\OverpassProject\\hiking_trails(test).json");
//
//
//        JSONParser parser =new JSONParser();
//        Object object= parser.parse(reader);
//        JSONObject jsonObject = (JSONObject) object;

        // Get the "elements" array from the parsed JSONObject
        JSONArray array = (JSONArray) jsonObject.get("elements");


        // Iterates through the "elements" array and adds nodes to the database
        for (int i = 0; i < array.size(); i++) {

            JSONObject element =(JSONObject) array.get(i);
            if(element.get("type").equals("node")){
                Node node =  Node.builder()
                        .id((long) element.get("id"))
                        .lat((double) element.get("lat"))
                        .lon((double) element.get("lon"))
                        .build();
                this.nodeJpaRepository.save(node);
            }

        }

    }

    public void AddLocations(JSONObject jsonObject) throws IOException, ParseException {

//        FileReader reader = new FileReader("D:\\Upwork\\OverpassProject\\hiking_trails(test).json");
//
//
//        JSONParser parser =new JSONParser();
//        Object object= parser.parse(reader);
//        JSONObject jsonObject = (JSONObject) object;

        JSONArray array = (JSONArray) jsonObject.get("elements");


        //Iterate through the "elements" array and add Locations to the database
        //Each location has "type" : "way"
        for (int i = 0; i < array.size(); i++) {
            JSONObject element = (JSONObject) array.get(i);
            if (element.get("type").equals("way")) {
                Location location = Location
                        .builder()
                        .id((long) element.get("id"))
                        .nodes(new HashSet<>())
                        .build();
                this.locationJpaRepository.save(location);
            }
        }
            //
            for (int i = 0; i < array.size(); i++) {
                JSONObject location = (JSONObject) array.get(i);
                if (location.get("type").equals("way")) {
                    JSONArray nodeList = (JSONArray) location.get("nodes");
                    for (int ii = 0; ii < nodeList.size(); ii++) {
                        long nodeId = (long) nodeList.get(ii);
                        long locationId = (long) location.get("id");
                        Node node = this.nodeJpaRepository.findById(nodeId).orElseThrow();
                        Location location1 = this.locationJpaRepository.findById(locationId).orElseThrow();

                        Set<Node> nodes = this.locationJpaRepository.findById(locationId).get().getNodes();
                        nodes.add(node);

                        location1.setNodes(nodes);
                        node.setLocation(location1);

                        this.nodeJpaRepository.save(node);
                        this.locationJpaRepository.save(location1);

                    }
                }

            }


        }

    }


