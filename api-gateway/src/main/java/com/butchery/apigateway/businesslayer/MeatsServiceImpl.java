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
        log.debug("2. Received in API-Gateway MeatServiceImpl getAllMeats.");
        return meatServiceClient.getAllMeats();
    }

    @Override
    public MeatResponseModel getMeatByMeatId(String meatId) {
        log.debug("2. Received in API-Gateway MeatServiceImpl getMeatByMeatId with meatId: " + meatId);
        return meatServiceClient.getMeatByMeatId(meatId);
    }

    @Override
    public MeatResponseModel addMeat(MeatRequestModel meatRequestModel) {
        log.debug("2. Received in API-Gateway MeatServiceImpl addMeat.");
        return meatServiceClient.addMeat(meatRequestModel);
    }

    @Override
    public MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId) {
        log.debug("2. Received in API-Gateway MeatServiceImpl updateMeat with meatId: " + meatId);
        return meatServiceClient.updateMeat(meatRequestModel,meatId);
    }

    @Override
    public void deleteMeat(String meatId) {
        log.debug("2. Received in API-Gateway MeatServiceImpl deleteMeat with meatId: " + meatId);
        meatServiceClient.deleteMeat(meatId);
    }
}
