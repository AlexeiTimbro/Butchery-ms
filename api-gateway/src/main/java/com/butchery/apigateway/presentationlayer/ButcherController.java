package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.businesslayer.ButchersService;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/butchers")
public class ButcherController {

    private final Integer UUID_SIZE = 36;

    private ButchersService butchersService;

    public ButcherController(ButchersService butchersService) {
        this.butchersService = butchersService;
    }

    @GetMapping(produces ="application/json")
    ResponseEntity<List<ButcherResponseModel>> getAllButchers(){

        //Keep track
        //log.debug("1. Received in API-Gateway butcher Controller getAllButcher.");

        return ResponseEntity.ok().body(butchersService.getAllButchers());
    }

    @GetMapping(value = "/{butcherId}", produces ="application/json")
    ResponseEntity<ButcherResponseModel> getButcherByButcherId(@PathVariable String butcherId){
        if(butcherId.length() != UUID_SIZE){
            throw new NotFoundException("Butcher id is invalid: " + butcherId);
        }

        //Keep track
        //log.debug("1. Received in API-Gateway Butchers Controller getButcherByButcherId with butcherId: " + butcherId);
        return ResponseEntity.ok().body(butchersService.getButcherByButcherId(butcherId));
    }

    @PostMapping()
    ResponseEntity<ButcherResponseModel> addButcher(@RequestBody ButcherRequestModel butcherRequestModel){

        //Keep track
        //log.debug("1. Received in API-Gateway Butchers Controller addButcher.");
        return ResponseEntity.status(HttpStatus.CREATED).body(butchersService.addButcher(butcherRequestModel));
    }

    @PutMapping(value = "/{butcherId}", produces ="application/json")
    ResponseEntity<ButcherResponseModel> updateButcher(@RequestBody ButcherRequestModel butcherRequestModel, @PathVariable String butcherId){
        if(butcherId.length() != UUID_SIZE){
            throw new NotFoundException("Butcher id is invalid: " + butcherId);
        }

        //Keep track
        //log.debug("1. Received in API-Gateway Butchers Controller updateButcher with butcherId: " + butcherId);
        return ResponseEntity.ok().body(butchersService.updateButcher(butcherRequestModel,butcherId));
    }

    @DeleteMapping(value = "/{butcherId}", produces ="application/json")
    ResponseEntity<Void> deleteButcher(@PathVariable String butcherId){
        if(butcherId.length() != UUID_SIZE){
            throw new NotFoundException("Butcher id is invalid: " + butcherId);
        }

        //Keep track
        //log.debug("1. Received in API-Gateway Butcher Controller deleteButcher with butcherId: " + butcherId);
        butchersService.deleteButcher(butcherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
