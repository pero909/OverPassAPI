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
import java.util.List;

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
        String bbox = "35.490643,19.24773,41.748917,29.652174";
        String query = "[out:json][timeout:25];" +
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

            // Write the string to a new JSON file in the current directory
            FileWriter fileWriter = new FileWriter("hiking_trails(test).json");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(response);
            printWriter.close();
            System.out.println("JSON file saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @GetMapping("/addNodes")
    public void AddNodes() throws IOException, ParseException {

        FileReader reader = new FileReader("D:\\Upwork\\OverpassProject\\hiking_trails(test).json");


        JSONParser parser =new JSONParser();
        Object object= parser.parse(reader);
        JSONObject jsonObject = (JSONObject) object;
        JSONArray array = (JSONArray) jsonObject.get("elements");



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
    @GetMapping("/addLocations")
    public void AddLocations() throws IOException, ParseException {

        FileReader reader = new FileReader("D:\\Upwork\\OverpassProject\\hiking_trails(test).json");


        JSONParser parser =new JSONParser();
        Object object= parser.parse(reader);
        JSONObject jsonObject = (JSONObject) object;
        JSONArray array = (JSONArray) jsonObject.get("elements");



        for (int i = 0; i < array.size(); i++) {
            JSONObject element = (JSONObject) array.get(i);
            if (element.get("type").equals("way")) {
                Location location = Location
                        .builder()
                        .id((long) element.get("id"))
                        .nodes(new ArrayList<>())
                        .build();
                this.locationJpaRepository.save(location);
            }
        }

            for (int i = 0; i < array.size(); i++) {
                JSONObject location = (JSONObject) array.get(i);
                if (location.get("type").equals("way")) {
                    JSONArray nodeList = (JSONArray) location.get("nodes");
                    for (int ii = 0; ii < nodeList.size(); ii++) {
                        long nodeId = (long) nodeList.get(ii);
                        long locationId = (long) location.get("id");
                        Node node = this.nodeJpaRepository.findById(nodeId).orElseThrow();
                        Location location1 = this.locationJpaRepository.findById(locationId).orElseThrow();

                        List<Node> nodes = this.locationJpaRepository.findById(locationId).get().getNodes();
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


