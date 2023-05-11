package com.butchery.meatservice.busineeslayer;

import com.butchery.meatservice.datalayer.Meat;
import com.butchery.meatservice.datalayer.MeatIdentifier;
import com.butchery.meatservice.datalayer.MeatRepository;
import com.butchery.meatservice.datamapperlayer.MeatRequestMapper;
import com.butchery.meatservice.datamapperlayer.MeatResponseMapper;
import com.butchery.meatservice.presentationlayer.MeatRequestModel;
import com.butchery.meatservice.presentationlayer.MeatResponseModel;
import com.butchery.meatservice.utils.exceptions.NotFoundException;
import com.butchery.meatservice.utils.exceptions.PriceLessOrEqualToZeroException;
import com.butchery.meatservice.utils.exceptions.ThisFieldIsRequiredException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeatServiceImpl implements MeatService {

    private MeatRepository meatRepository;
    private MeatRequestMapper meatRequestMapper;
    private MeatResponseMapper meatResponseMapper;

    public MeatServiceImpl(MeatRepository meatRepository, MeatRequestMapper meatRequestMapper, MeatResponseMapper meatResponseMapper) {
        this.meatRepository = meatRepository;
        this.meatRequestMapper = meatRequestMapper;
        this.meatResponseMapper = meatResponseMapper;
    }

    @Override
    public List<MeatResponseModel> getMeats() {
        return meatResponseMapper.entityListToResponseModelList(meatRepository.findAll());
    }

    @Override
    public MeatResponseModel getMeatByMeatId(String meatId) {

        Meat existingMeat = meatRepository.findMeatByMeatIdentifier_MeatId(meatId);
        if (existingMeat == null) {
            throw new NotFoundException("Unknown Meat id.");
        }

        return meatResponseMapper.entityToResponseModel(existingMeat);
    }

    @Override
    public MeatResponseModel addMeat(MeatRequestModel meatRequestModel) {

        String animal = meatRequestModel.getAnimal();
        String environment = meatRequestModel.getEnvironment();
        String texture = meatRequestModel.getTexture();
        String expirationDate = meatRequestModel.getExpirationDate();
        Double price = meatRequestModel.getPrice();

        if (animal == null || animal.isBlank()) {
            throw new ThisFieldIsRequiredException("The animal field is required.");
        }
        if (environment == null || environment.isBlank()) {
            throw new ThisFieldIsRequiredException("The Environment field is required.");
        }
        if (texture == null || texture.isBlank()) {
            throw new ThisFieldIsRequiredException("The Texture field is required.");
        }
        if (expirationDate == null || expirationDate.isBlank()) {
            throw new ThisFieldIsRequiredException("The Expiration date field is required.");
        }
        if (price == null) {
            throw new ThisFieldIsRequiredException("The price field is required.");
        }

        if(price <= 0){
            throw new PriceLessOrEqualToZeroException("The price needs to be higher then 0.");
        }

        Meat meat = meatRequestMapper.requestModelToEntity1(meatRequestModel);
        meat.setMeatIdentifier(new MeatIdentifier());

        return meatResponseMapper.entityToResponseModel(meatRepository.save(meat));
    }

    @Override
    public MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId) {

        Meat existingMeat = meatRepository.findMeatByMeatIdentifier_MeatId(meatId);
        if (existingMeat == null) {
            throw new NotFoundException("Unknown Meat id.");
        }

        Meat meat = meatRequestMapper.requestModelToEntity1(meatRequestModel);
        meat.setId(existingMeat.getId());
        meat.setMeatIdentifier(existingMeat.getMeatIdentifier());

        return meatResponseMapper.entityToResponseModel(meatRepository.save(meat));
    }

    @Override
    public void removeMeat(String meatId) {

        Meat existingMeat = meatRepository.findMeatByMeatIdentifier_MeatId(meatId);

        if (existingMeat == null) {
            throw new NotFoundException("Unknown Meat id.");
        }
        meatRepository.delete(existingMeat);
    }
}
