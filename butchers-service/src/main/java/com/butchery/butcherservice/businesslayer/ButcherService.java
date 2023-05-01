package com.butchery.butcherservice.businesslayer;

import com.butchery.butcherservice.presentationlayer.ButcherRequestModel;
import com.butchery.butcherservice.presentationlayer.ButcherResponseModel;

import java.util.List;

public interface ButcherService {

    List<ButcherResponseModel> getButchers();
    ButcherResponseModel getButcherByButcherId(String butcherId);
    ButcherResponseModel addButcher(ButcherRequestModel butcherRequestModel);
    ButcherResponseModel updateButcher(ButcherRequestModel butcherRequestModel, String butcherId);
    void removeButcher(String butcherId);
}
