package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.presentationlayer.ButcherRequestModel;
import com.butchery.apigateway.presentationlayer.ButcherResponseModel;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;

import java.util.List;

public interface ButchersService {

    List<ButcherResponseModel> getAllButchers();
    ButcherResponseModel getButcherByButcherId(String butcherId);
    ButcherResponseModel addButcher(ButcherRequestModel butcherRequestModel);
    ButcherResponseModel updateButcher(ButcherRequestModel butcherRequestModel, String butcherId);
    void deleteButcher(String butcherId);
}
