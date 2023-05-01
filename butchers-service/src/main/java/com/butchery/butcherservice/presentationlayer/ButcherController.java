package com.butchery.butcherservice.presentationlayer;




import com.butchery.butcherservice.businesslayer.ButcherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/butchers")
public class ButcherController {

    private ButcherService butcherService;

    public ButcherController(ButcherService butcherService) {
        this.butcherService = butcherService;
    }

    @GetMapping()
    ResponseEntity <List<ButcherResponseModel>> getButchers() {
        return ResponseEntity.ok().body(butcherService.getButchers());
    }

    @GetMapping("/{butcherId}")
    ResponseEntity<ButcherResponseModel> getButcherByButcherId(@PathVariable String butcherId) {
        return ResponseEntity.ok().body(butcherService.getButcherByButcherId(butcherId));
    }

    @PostMapping()
    ResponseEntity<ButcherResponseModel> addButcher(@RequestBody ButcherRequestModel butcherRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(butcherService.addButcher(butcherRequestModel));
    }

    @PutMapping("/{butcherId}")
    ResponseEntity<ButcherResponseModel> updateButcher(@RequestBody ButcherRequestModel butcherRequestModel, @PathVariable String butcherId) {
        return ResponseEntity.ok().body(butcherService.updateButcher(butcherRequestModel, butcherId));
    }

    @DeleteMapping("/{butcherId}")
    ResponseEntity<Void>removeButcher(@PathVariable String butcherId) {
        butcherService.removeButcher(butcherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
