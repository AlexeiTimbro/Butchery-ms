package com.butchery.apigateway.presentationlayer;

import com.butchery.apigateway.businesslayer.MeatsService;
import com.butchery.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/meats")
public class MeatsController {

    private final Integer UUID_SIZE = 36;

    private MeatsService meatsService;

    public MeatsController(MeatsService meatsService) {
        this.meatsService = meatsService;
    }

    @GetMapping(produces ="application/json")
    ResponseEntity<List<MeatResponseModel>> getAllMeats(){
        return ResponseEntity.ok().body(meatsService.getAllMeats());
    }

    @GetMapping(value = "/{meatId}", produces ="application/json")
    ResponseEntity<MeatResponseModel> getMeatByMeatId(@PathVariable String meatId){
        return ResponseEntity.ok().body(meatsService.getMeatByMeatId(meatId));
    }

    @PostMapping()
    ResponseEntity<MeatResponseModel> addMeat(@RequestBody MeatRequestModel meatRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(meatsService.addMeat(meatRequestModel));
    }

    @PutMapping(value = "/{meatId}", produces ="application/json")
    ResponseEntity<MeatResponseModel> updateMeat(@RequestBody MeatRequestModel meatRequestModel, @PathVariable String meatId){
        return ResponseEntity.ok().body(meatsService.updateMeat(meatRequestModel,meatId));
    }

    @DeleteMapping(value = "/{meatId}", produces ="application/json")
    ResponseEntity<Void> deleteMeat(@PathVariable String meatId){
        meatsService.deleteMeat(meatId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
