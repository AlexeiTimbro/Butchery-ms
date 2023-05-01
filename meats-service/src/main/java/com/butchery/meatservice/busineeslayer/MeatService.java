package com.butchery.meatservice.busineeslayer;





import com.butchery.meatservice.presentationlayer.MeatRequestModel;
import com.butchery.meatservice.presentationlayer.MeatResponseModel;

import java.util.List;

public interface MeatService {

    List<MeatResponseModel> getMeats();

    MeatResponseModel getMeatByMeatId(String meatId);

    MeatResponseModel addMeat(MeatRequestModel meatRequestModel);

    MeatResponseModel updateMeat(MeatRequestModel meatRequestModel, String meatId);

    void removeMeat(String meatId);



}
