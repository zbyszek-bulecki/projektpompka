package com.sharks.gardenManager.controllers;

import com.sharks.gardenManager.DTO.ExampleRequest;
import com.sharks.gardenManager.DTO.ExampleResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private static int val = 0;

    @GetMapping
    public ExampleResponse get(){
        val = (val+1)%10;
        String[] arr = {"val"+val, "val"+((val+1)%10), "val"+((val+2)%10)};
        return new ExampleResponse("text", arr, true);
    }

    @PostMapping
    public ExampleResponse post(@RequestBody ExampleRequest exampleRequest){
        return new ExampleResponse("post response", exampleRequest.array(), exampleRequest.flag());
    }
}
