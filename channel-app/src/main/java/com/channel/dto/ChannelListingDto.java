package com.channel.dto;

import com.channel.api.ChannelApi;
import com.channel.api.ChannelListingApi;
import com.channel.assure.ClientAssure;
import com.channel.assure.ProductAssure;
import com.channel.model.form.ChannelListingCsvForm;
import com.channel.model.form.ChannelListingForm;
import com.channel.model.form.ChannelListingSearchForm;
import com.channel.model.response.ChannelListingData;
import com.channel.pojo.ChannelListing;
import com.channel.util.ConverterUtil;
import com.channel.validator.ChannelListingCsvFormValidator;
import com.commons.api.CustomValidationException;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import com.commons.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelListingDto {

    private static final Logger logger = Logger.getLogger(ChannelListingDto.class);

    @Autowired
    private ChannelListingApi channelListingApi;

    @Autowired
    private ClientAssure clientAssure;

    @Autowired
    private ProductAssure productAssure;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ChannelListingCsvFormValidator channelListingCsvFormValidator;

    @Transactional(rollbackFor = CustomValidationException.class)
    public void addChannelListing(ChannelListingCsvForm channelListingCsvForm, BindingResult result) {
        // validate
        channelListingCsvFormValidator.validate(channelListingCsvForm, result);
        if (result.hasErrors()) {
            logger.info(result.getErrorCount());
            throw new CustomValidationException(result);
        }
        logger.info("No errors in channelListing csv file");
        for (ChannelListingForm channelListingForm : channelListingCsvForm.getChannelListingFormList()) {
            List<ProductData> productDataList = productAssure.getProductByClientIdAndClientSkuId(channelListingForm.getClientId());
            ProductData productData = getProductData(productDataList, channelListingForm.getClientSkuId());
            assert productData != null;
            ChannelListing channelListing = ConverterUtil.convertChannelListingFormToChannelListing(channelListingForm, productData);
            // add
            channelListingApi.add(channelListing);
        }
        logger.info("Channel Listing added");
    }

    @Transactional(readOnly = true)
    public ChannelListingData getChannelListing(Long id) {
        ChannelListing channelListing = channelListingApi.getChannelListing(id);
        ProductData productData = productAssure.getProductData(channelListing.getGlobalSkuId());
        return ConverterUtil.convertChannelListingToChannelListingData(channelListing, channelApi.get(channelListing.getChannelId()), productData);
    }

    @Transactional(readOnly = true)
    public ChannelListingData getChannelListingData(Long channelId, String channelSkuId, Long clientId) {
        ChannelListing channelListing = channelListingApi.getChannelListingByParameters(channelId, channelSkuId, clientId);
        if (channelListing != null) {
            ProductData productData = productAssure.getProductData(channelListing.getGlobalSkuId());
            return ConverterUtil.convertChannelListingToChannelListingData(channelListing, channelApi.get(channelListing.getChannelId()), productData);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ChannelListingData> searchChannelListing(ChannelListingSearchForm channelListingSearchForm) {
        List<ChannelListing> channelListings = channelListingApi.getAllChannelListing();
        // filter with channelId
        if (channelListingSearchForm.getChannelId() != 0) {
            channelListings = channelListings.stream().filter(o -> (channelListingSearchForm.getChannelId().equals(o.getChannelId()))).collect(Collectors.toList());
        }
        // filter with clientId
        if (channelListingSearchForm.getClientId() != 0) {
            channelListings = channelListings.stream().filter(o -> (channelListingSearchForm.getClientId().equals(o.getClientId()))).collect(Collectors.toList());
        }
        return channelListings.stream().map(o -> ConverterUtil.convertChannelListingToChannelListingData(o, channelApi.get(o.getChannelId()), productAssure.getProductData(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChannelListingData> getAllChannelListingByChannelClient(Long channelId, Long clientId) {
        List<ChannelListing> channelListings = channelListingApi.getByClientId(clientId);
        channelListings = channelListings.stream().filter(channelListing -> channelListing.getChannelId().equals(channelId)).collect(Collectors.toList());
        return channelListings.stream().map(o -> ConverterUtil.convertChannelListingToChannelListingData(o, channelApi.get(o.getChannelId()), productAssure.getProductData(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChannelListingData> getAllChannelListing() {
        List<ChannelListing> channelListings = channelListingApi.getAllChannelListing();
        return channelListings.stream().map(o -> ConverterUtil.convertChannelListingToChannelListingData(o, channelApi.get(o.getChannelId()), productAssure.getProductData(o.getGlobalSkuId()))).collect(Collectors.toList());
    }

    private ProductData getProductData(List<ProductData> productDataList, String clientSkuId) {
        for (ProductData productData : productDataList) {
            if (productData.getClientSkuId().equals(StringUtil.toLowerCase(clientSkuId))) {
                return productData;
            }
        }
        return null;
    }


    public ClientData getClient(Long clientId) {
        return clientAssure.getClientData(clientId);
    }

    public List<ProductData> getProductByClientIdAndClientSkuId(Long clientId) {
        return productAssure.getProductByClientIdAndClientSkuId(clientId);
    }

    public void setClientAssureRestTemplate(ClientAssure clientAssureRestTemplate) {
        clientAssure = clientAssureRestTemplate;
    }

    public void setProductAssureRestTemplate(ProductAssure productAssureRestTemplate) {
        productAssure = productAssureRestTemplate;
    }



}
