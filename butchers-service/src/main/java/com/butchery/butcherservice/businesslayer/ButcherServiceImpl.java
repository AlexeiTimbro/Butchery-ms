package com.butchery.butcherservice.businesslayer;



import com.butchery.butcherservice.datalayer.Butcher;
import com.butchery.butcherservice.datalayer.ButcherRepository;
import com.butchery.butcherservice.datamapperlayer.ButcherRequestMapper;
import com.butchery.butcherservice.datamapperlayer.ButcherResponseMapper;
import com.butchery.butcherservice.presentationlayer.ButcherRequestModel;
import com.butchery.butcherservice.presentationlayer.ButcherResponseModel;
import com.butchery.butcherservice.utils.exceptions.ButcherIsTooYoungException;
import com.butchery.butcherservice.utils.exceptions.DuplicatePhoneNumberException;
import com.butchery.butcherservice.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ButcherServiceImpl implements ButcherService{

    private ButcherRepository butcherRepository;
    private ButcherResponseMapper butcherResponseMapper;
    private ButcherRequestMapper butcherRequestMapper;

    public ButcherServiceImpl(ButcherRepository butcherRepository, ButcherResponseMapper butcherResponseMapper, ButcherRequestMapper butcherRequestMapper) {
        this.butcherRepository = butcherRepository;
        this.butcherResponseMapper = butcherResponseMapper;
        this.butcherRequestMapper = butcherRequestMapper;
    }

    @Override
    public List<ButcherResponseModel> getButchers() {
        return butcherResponseMapper.entityListToResponseModelList(butcherRepository.findAll());
    }

    @Override
    public ButcherResponseModel getButcherByButcherId(String butcherId) {

        Butcher existingButcher = butcherRepository.findButcherByButcherIdentifier_ButcherId(butcherId);
        if (existingButcher == null) {
            throw new NotFoundException("Unknown butcher id");
        }

        return butcherResponseMapper.entityToResponseModel(butcherRepository.findButcherByButcherIdentifier_ButcherId(butcherId));
    }

    @Override
    public ButcherResponseModel addButcher(ButcherRequestModel butcherRequestModel) {

        Butcher butcher = butcherRequestMapper.requestModelToEntity(butcherRequestModel);

        int age = butcherRequestModel.getAge();
        if (age <= 16) {
            throw new ButcherIsTooYoungException("The age of this butcher is under the minimum requirement of 16.");
        }

        String phoneNumber = butcherRequestModel.getPhoneNumber();
        List<Butcher> butchers = butcherRepository.findButcherByPhoneNumber(phoneNumber);
        if(butchers.size() > 0){
            throw new DuplicatePhoneNumberException("This phone number: " + phoneNumber + " is already in use by another butcher");
        }

        return butcherResponseMapper.entityToResponseModel(butcherRepository.save(butcher));

    }

    @Override
    public ButcherResponseModel updateButcher(ButcherRequestModel butcherRequestModel, String butcherId) {

        Butcher existingButcher = butcherRepository.findButcherByButcherIdentifier_ButcherId(butcherId);
        if (existingButcher == null) {
            throw new NotFoundException("Unknown butcher id");
        }
        int age = butcherRequestModel.getAge();
        if (age <= 16) {
            throw new ButcherIsTooYoungException("The age of this butcher is under the minimum requirement of 16.");
        }
        Butcher butcher = butcherRequestMapper.requestModelToEntity(butcherRequestModel);
        butcher.setId(existingButcher.getId());
        butcher.setButcherIdentifier(existingButcher.getButcherIdentifier());

        return butcherResponseMapper.entityToResponseModel(butcherRepository.save(butcher));

    }

    @Override
    public void removeButcher(String butcherId) {

        Butcher existingButcher = butcherRepository.findButcherByButcherIdentifier_ButcherId(butcherId);
        if (existingButcher == null) {
            throw new NotFoundException("Unknown butcher id");
        }

        butcherRepository.delete(existingButcher);

    }
}
