package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.domainclientlayer.MeatServiceClient;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MeatsServiceImpl implements MeatsService{

    private MeatServiceClient meatServiceClient;

    public MeatsServiceImpl(MeatServiceClient meatServiceClient) {
        this.meatServiceClient = meatServiceClient;
    }

    @Override
    public List<MeatResponseModel> getAllMeats() {
        return meatServiceClient.getAllMeats();
    }

    @Override
    public MeatResponseModel getMeatByMeatId(String meatId) {
        return meatServiceClient.getMeatByMeatId(meatId);
    }

    @Override
    public MeatResponseModel addMeat(MeatRequestModel meatRequestModel) {
        return meatServiceClient.addMeat(meatRequestModel);
    }

    @Override
    public MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId) {
        return meatServiceClient.updateMeat(meatRequestModel,meatId);
    }

    @Override
    public void deleteMeat(String meatId) {
        meatServiceClient.deleteMeat(meatId);
    }
}
