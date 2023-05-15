package com.butchery.apigateway.businesslayer;

import com.butchery.apigateway.domainclientlayer.ButcherServiceClient;
import com.butchery.apigateway.presentationlayer.ButcherRequestModel;
import com.butchery.apigateway.presentationlayer.ButcherResponseModel;
import com.butchery.apigateway.presentationlayer.MeatRequestModel;
import com.butchery.apigateway.presentationlayer.MeatResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ButchersServiceImpl implements ButchersService{

    private ButcherServiceClient butcherServiceClient;

    public ButchersServiceImpl(ButcherServiceClient butcherServiceClient) {
        this.butcherServiceClient = butcherServiceClient;
    }

    @Override
    public List<ButcherResponseModel> getAllButchers() {
        return  butcherServiceClient.getAllButchers();
    }

    @Override
    public ButcherResponseModel getButcherByButcherId(String butcherId) {
        return butcherServiceClient.getButcherByButcherId(butcherId);
    }

    @Override
    public ButcherResponseModel addButcher(ButcherRequestModel butcherRequestModel) {
        return butcherServiceClient.addButcher(butcherRequestModel);
    }

    @Override
    public ButcherResponseModel updateButcher(ButcherRequestModel butcherRequestModel, String butcherId) {
        return butcherServiceClient.updateButcher(butcherRequestModel,butcherId);
    }

    @Override
    public void deleteButcher(String butcherId) {
        butcherServiceClient.deleteButcher(butcherId);
    }
}
