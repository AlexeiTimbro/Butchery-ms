package com.butchery.purchaseservice.businesslayer;

import com.butchery.purchaseservice.Utils.Exceptions.NotFoundException;
import com.butchery.purchaseservice.datalayer.*;
import com.butchery.purchaseservice.datamappinglayer.PurchaseResponseModelMapper;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherResponseModel;
import com.butchery.purchaseservice.domainclientlayer.butcher.ButcherServiceClient;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerResponseModel;
import com.butchery.purchaseservice.domainclientlayer.customer.CustomerServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatRequestModel;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatResponseModel;
import com.butchery.purchaseservice.domainclientlayer.meat.MeatServiceClient;
import com.butchery.purchaseservice.domainclientlayer.meat.Status;
import com.butchery.purchaseservice.presentationlayer.PurchaseRequestModel;
import com.butchery.purchaseservice.presentationlayer.PurchaseResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseResponseModelMapper purchaseResponseModelMapper;
    private final ButcherServiceClient butcherServiceClient;
    private final CustomerServiceClient customerServiceClient;
    private final MeatServiceClient meatServiceClient;


    @Override
    public List<PurchaseResponseModel> getAllPurchaseAggregate() {
        List<Purchase> purchases = purchaseRepository.findAll();
        return purchaseResponseModelMapper.entityListToResponseModelList(purchases);
    }

    @Override
    public PurchaseResponseModel getPurchaseByPurchaseIdAggregate(String purchaseId) {
        Purchase purchase = purchaseRepository.findPurchaseByPurchaseIdentifier_PurchaseId(purchaseId);

        if(purchase == null) {
            throw new NotFoundException("Unknown purchaseId provided");
        }

        return purchaseResponseModelMapper.entityToResponseModel(purchase);

    }


    @Override
    public PurchaseResponseModel processCustomerPurchase(PurchaseRequestModel purchaseRequestModel, String customerId) {

        //validate the customerId by getting its data from customers-service
        CustomerResponseModel customerResponseModel = customerServiceClient.getCustomerByCustomerId(customerId);
        if(customerResponseModel==null){
            throw new NotFoundException("Unknown customerId provided: "+ customerId);

        }

        //validate the meatId by getting its data from the meats-service
        MeatResponseModel meatResponseModel = meatServiceClient.getMeatByMeatId(purchaseRequestModel.getMeatId());
        if(meatResponseModel==null){
            throw new NotFoundException("Unknown employeeId provided: "+ purchaseRequestModel.getMeatId());
        }

        //validate that the vehicle exists in the inventory by getting the vehicle data from inventory-service
        ButcherResponseModel butcherResponseModel = butcherServiceClient.getButcherByButcherId(purchaseRequestModel.getButcherId());
        if(butcherResponseModel==null){
            throw new NotFoundException("Vehicle with vehicleId: "+ purchaseRequestModel.getButcherId());
        }

        //build the purchase order
        Purchase purchase =Purchase.builder()
                .purchaseIdentifier(new PurchaseIdentifier())
                .customerIdentifier(new CustomerIdentifier(customerResponseModel.getCustomerId()))
                .meatIdentifier(new MeatIdentifier(meatResponseModel.getMeatId()))
                .butcherIdentifier(new ButcherIdentifier(butcherResponseModel.getButcherId()))
                .butcherFirstName(butcherResponseModel.getFirstName())
                .butcherLastName(butcherResponseModel.getLastName())
                .customerFirstName(customerResponseModel.getFirstName())
                .customerLastName(customerResponseModel.getLastName())
                .paymentMethod(PaymentMethod.CREDIT)
                .salePrice(purchaseRequestModel.getSalePrice())
                .purchaseStatus(purchaseRequestModel.getPurchaseStatus())
                .animal(meatResponseModel.getAnimal())
                .environment(meatResponseModel.getEnvironment())
                .texture(meatResponseModel.getTexture())
                .expirationDate(meatResponseModel.getExpirationDate())
                .paymentMethod(purchaseRequestModel.getPaymentMethod())
                .purchaseDate(purchaseRequestModel.getPurchaseDate())
                .build();

        //save the purchase order
        Purchase saved=purchaseRepository.save(purchase);

        //todo: implement try/catch

        //aggregate invariant - update the vehicle status based on the purchase order status
        //if purchase status is PURCHASE OFFER or PURCHASE NEGOTIATION, then vehicle status is SALE_PENDING
        //if purchase status is PURCHASE COMPLETED, then vehicle status is SOLD
        //if purchase status is PURCHASE CANCELED, then vehicle status is AVAILABLE

        Status meatStatus=Status.AVAILABLE;

        switch(saved.getPurchaseStatus()){
            case PURCHASE_COMPLETED -> meatStatus= Status.SOLD;
            case PURCHASE_CANCELLED -> meatStatus= Status.AVAILABLE;
        }

        //convert our meat response model into a meat request model
        MeatRequestModel meatRequestModel =MeatRequestModel.builder()
                .animal(meatResponseModel.getAnimal())
                .status(meatStatus)
                .environment(meatResponseModel.getEnvironment())
                .texture(meatResponseModel.getTexture())
                .expirationDate(meatResponseModel.getExpirationDate())
                .price(meatResponseModel.getPrice())
                .build();

        meatServiceClient.updateMeatStatus(meatRequestModel, meatResponseModel.getMeatId());

        return purchaseResponseModelMapper.entityToResponseModel(saved);

    }

    @Override
    public PurchaseResponseModel updateCustomerPurchase(PurchaseRequestModel purchaseRequestModel,String customerId, String purchaseId) {

        CustomerResponseModel customerResponseModel = customerServiceClient.getCustomerByCustomerId(customerId);
        if(customerResponseModel == null) {
            throw new NotFoundException("Unknown customerId provided: "+ customerId);
        }

        MeatResponseModel meatResponseModel = meatServiceClient.getMeatByMeatId(purchaseRequestModel.getMeatId());
        if(meatResponseModel == null) {
            throw new NotFoundException("Unknown employeeId provided: "+ purchaseRequestModel.getMeatId());
        }

        ButcherResponseModel butcherResponseModel = butcherServiceClient.getButcherByButcherId(purchaseRequestModel.getButcherId());
        if(butcherResponseModel == null) {
            throw new NotFoundException("Vehicle with vehicleId: "+ purchaseRequestModel.getButcherId());
        }

        Purchase existingPurchase = purchaseRepository.findPurchaseByPurchaseIdentifier_PurchaseId(purchaseId);



        existingPurchase.setCustomerIdentifier(new CustomerIdentifier(customerResponseModel.getCustomerId()));
        existingPurchase.setMeatIdentifier(new MeatIdentifier(meatResponseModel.getMeatId()));
        existingPurchase.setButcherIdentifier(new ButcherIdentifier(butcherResponseModel.getButcherId()));
        existingPurchase.setButcherFirstName(butcherResponseModel.getFirstName());
        existingPurchase.setButcherLastName(butcherResponseModel.getLastName());
        existingPurchase.setCustomerFirstName(customerResponseModel.getFirstName());
        existingPurchase.setCustomerLastName(customerResponseModel.getLastName());
        existingPurchase.setSalePrice(purchaseRequestModel.getSalePrice());
        existingPurchase.setPurchaseStatus(purchaseRequestModel.getPurchaseStatus());
        existingPurchase.setAnimal(meatResponseModel.getAnimal());
        existingPurchase.setEnvironment(meatResponseModel.getEnvironment());
        existingPurchase.setTexture(meatResponseModel.getTexture());
        existingPurchase.setExpirationDate(meatResponseModel.getExpirationDate());
        existingPurchase.setPaymentMethod(purchaseRequestModel.getPaymentMethod());
        existingPurchase.setPurchaseDate(purchaseRequestModel.getPurchaseDate());


        Purchase updatedPurchase = purchaseRepository.save(existingPurchase);

        Status meatStatus = Status.AVAILABLE;
        switch(updatedPurchase.getPurchaseStatus()) {
            case PURCHASE_COMPLETED -> meatStatus = Status.SOLD;
            case PURCHASE_CANCELLED -> meatStatus = Status.AVAILABLE;
        }

        MeatRequestModel meatRequestModel = MeatRequestModel.builder()
                .animal(meatResponseModel.getAnimal())
                .status(meatStatus)
                .environment(meatResponseModel.getEnvironment())
                .texture(meatResponseModel.getTexture())
                .expirationDate(meatResponseModel.getExpirationDate())
                .price(meatResponseModel.getPrice())
                .build();

        meatServiceClient.updateMeatStatus(meatRequestModel, meatResponseModel.getMeatId());

        return purchaseResponseModelMapper.entityToResponseModel(updatedPurchase);

    }

    @Override
    public void deletePurchaseAggregate(String purchaseId) {

            Purchase existingPurchase = purchaseRepository.findPurchaseByPurchaseIdentifier_PurchaseId(purchaseId);


        if(existingPurchase == null){

                throw new NotFoundException("No SupervisorConfirmation assigned to this supervisorConfirmationId");
            }else{
                purchaseRepository.delete(existingPurchase);
            }
    }

}

