package com.butchery.meatservice.presentationlayer;

import com.butchery.meatservice.busineeslayer.MeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/meats")
public class MeatController {

    private MeatService meatService;

    public MeatController(MeatService meatService) {
        this.meatService = meatService;
    }

    @GetMapping()
    public ResponseEntity <List<MeatResponseModel>> getMeats() {
        return ResponseEntity.ok().body(meatService.getMeats());
    }

    @GetMapping("/{meatId}")
    public ResponseEntity<MeatResponseModel> getMeatByMeatId(@PathVariable String meatId) {
        return ResponseEntity.ok().body(meatService.getMeatByMeatId(meatId));
    }

    @PostMapping()
    public ResponseEntity<MeatResponseModel> addMeat(@RequestBody MeatRequestModel meatRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(meatService.addMeat(meatRequestModel));
    }

    @PutMapping("/{meatId}")
    public ResponseEntity<MeatResponseModel> updateMeat(@RequestBody MeatRequestModel meatRequestModel, @PathVariable String meatId) {
        return ResponseEntity.ok().body(meatService.updateMeat(meatRequestModel,meatId));
    }

    @DeleteMapping("/{meatId}")
    public ResponseEntity <Void> removeMeat(@PathVariable String meatId) {
        meatService.removeMeat(meatId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
