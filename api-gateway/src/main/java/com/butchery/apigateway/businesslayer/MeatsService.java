package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;

import java.util.List;

public interface MeatsService {

    List<MeatResponseModel> getAllMeats();
    MeatResponseModel getMeatByMeatId(String meatId);
    MeatResponseModel addMeat(MeatRequestModel meatRequestModel);
    MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId);
    void deleteMeat(String meatId);
}
