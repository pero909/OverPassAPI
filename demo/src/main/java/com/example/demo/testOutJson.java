package com.example.demo;

import com.example.demo.model.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONTokener;
import org.json.simple.parser.ParseException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class testOutJson {
    public static void main(String[] args) throws IOException, ParseException {
        String jsonString = "PUT_YOUR_JSON_STRING_HERE";

        FileReader reader = new FileReader("D:\\Upwork\\OverpassProject\\demo\\src\\main\\java\\com\\example\\demo\\hiking_trails(relation).json");


        JSONParser parser =new JSONParser();
        Object object= parser.parse(reader);
        JSONObject jsonObject = (JSONObject) object;
        JSONArray array = (JSONArray) jsonObject.get("elements");



        for (int i = 0; i < 1; i++) {
            JSONObject element =(JSONObject) array.get(i);
            if(element.get("type").equals("node")){
              Node node =  Node.builder()
                      .id((long) element.get("id"))
                      .lat((long) element.get("lat"))
                      .lon((long) element.get("lon"))
                      .build();

            }


            }
        }
   }

