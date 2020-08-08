package com.assure.dto;

import com.assure.model.form.*;
import com.assure.model.response.BinSkuData;
import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import com.commons.enums.ClientType;
import com.commons.form.ClientForm;
import com.commons.form.ProductCsvForm;
import com.commons.form.ProductForm;
import com.commons.response.ClientData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BinSkuDtoTest extends AbstractUnitTest {
    private ClientForm clientForm1,clientForm2;
    private ProductForm productForm1,productForm2;
    private BinForm binForm1,binForm2;
    private final ProductCsvForm productCsvForm = new ProductCsvForm();
    private final BinSkuCsvForm binSkuCsvForm = new BinSkuCsvForm();
    private BinSkuForm binSkuForm1,binSkuForm2;
    private BinSkuSearchForm binSkuSearchForm;
    private BinSkuUpdateForm binSkuUpdateForm;


    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BinDto binDto;
    @Autowired
    private BinSkuDto binSkuDto;

    @Before
    public void setUp(){
        clientForm1 = createClientForm("viram", ClientType.CLIENT);
        clientForm2 = createClientForm("viram308",ClientType.CUSTOMER);
        productForm1 = createProductForm("munch",10.00,"excellent","prod1","nestle");
        productForm2 = createProductForm("kitkat",15.00,"nice","prod2","britannia");
        binForm1 = createBinForm();
        binForm2 = createBinForm();
        binSkuForm1 = createBinSkuForm(20L);
        binSkuForm2 = createBinSkuForm(30L);
        binSkuSearchForm = createBinSkuSearchForm();
        binSkuUpdateForm = createBinSkuUpdateForm();
    }

    private BinSkuUpdateForm createBinSkuUpdateForm() {
        BinSkuUpdateForm binSkuUpdateForm = new BinSkuUpdateForm();
        binSkuUpdateForm.setOriginalQuantity(binSkuForm1.getQuantity());
        binSkuUpdateForm.setUpdateQuantity(30L);
        return binSkuUpdateForm;
    }

    private BinSkuSearchForm createBinSkuSearchForm() {
        BinSkuSearchForm binSkuSearchForm = new BinSkuSearchForm();
        binSkuSearchForm.setBinId(1000L);
        binSkuSearchForm.setClientId(0L);
        binSkuSearchForm.setClientSkuId("");
        return binSkuSearchForm;
    }

    private BinSkuForm createBinSkuForm(Long quantity) {
        BinSkuForm binSkuForm = new BinSkuForm();
        binSkuForm.setQuantity(quantity);
        return binSkuForm;
    }

    private BinForm createBinForm(){
        BinForm binForm = new BinForm();
        binForm.setNoOfBins(1L);
        return binForm;
    }
    private ProductForm createProductForm(String productName,double mrp,String description,String clientSkuId,String brandId) {
        ProductForm productForm = new ProductForm();
        productForm.setProductName(productName);
        productForm.setMrp(mrp);
        productForm.setDescription(description);
        productForm.setClientSkuId(clientSkuId);
        productForm.setBrandId(brandId);
        return productForm;
    }

    private ClientForm createClientForm(String name, ClientType type) {
        ClientForm clientForm = new ClientForm();
        clientForm.setName(name);
        clientForm.setType(type);
        return clientForm;
    }

    private void addDetails(){
        ClientData clientData1 = clientDto.addClient(clientForm1);
        ClientData clientData2 = clientDto.addClient(clientForm2);

        productForm1.setClientId(clientData1.getId());
        productForm2.setClientId(clientData2.getId());
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm1);
        productFormList.add(productForm2);
        productCsvForm.setProductFormList(productFormList);
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        productDto.addProducts(productCsvForm,result);
        binDto.addBins(binForm1);
        binDto.addBins(binForm2);
        binSkuForm1.setClientId(clientData1.getId());
        binSkuForm1.setClientSkuId(productForm1.getClientSkuId());
        binSkuForm1.setBinId(1000L);
        binSkuForm2.setClientId(clientData1.getId());
        binSkuForm2.setClientSkuId(productForm1.getClientSkuId());
        binSkuForm2.setBinId(1001L);
        List<BinSkuForm> binSkuFormList = new ArrayList<>();
        binSkuFormList.add(binSkuForm1);
        binSkuFormList.add(binSkuForm2);
        binSkuCsvForm.setBinSkuFormList(binSkuFormList);
    }

    @Test(expected = CustomValidationException.class)
    public void testAddBinSku(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        binSkuDto.addBinSku(binSkuCsvForm,result);
        binSkuDto.addBinSku(binSkuCsvForm,result);
        when(result.hasErrors()).thenReturn(true);
        binSkuCsvForm.getBinSkuFormList().get(0).setBinId(1003L);
        binSkuDto.addBinSku(binSkuCsvForm,result);
    }

    @Test
    public void testGetAll(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        binSkuDto.addBinSku(binSkuCsvForm,result);
        List<BinSkuData> binSkuDataList = binSkuDto.getAllBinSku();
        assertEquals(2,binSkuDataList.size());
    }

    @Test
    public void testGetBinSku(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        binSkuDto.addBinSku(binSkuCsvForm,result);
        List<BinSkuData> binSkuDataList = binSkuDto.searchBinSku(binSkuSearchForm);
        BinSkuData binSkuData = binSkuDto.getBinSku(binSkuDataList.get(0).getBinSkuId());
        assertNotNull(binSkuData);
        assertEquals(binSkuForm1.getBinId(),binSkuData.getBinId());
        assertEquals(binSkuForm1.getQuantity(),binSkuData.getQuantity());
    }

    @Test
    public void testSearchBinSku(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        binSkuDto.addBinSku(binSkuCsvForm,result);
        List<BinSkuData> binSkuDataList = binSkuDto.searchBinSku(binSkuSearchForm);
        assertEquals(1,binSkuDataList.size());
        binSkuSearchForm.setClientSkuId("hii");
        binSkuDataList = binSkuDto.searchBinSku(binSkuSearchForm);
        assertNull(binSkuDataList);
        binSkuSearchForm.setClientSkuId("");
        binSkuSearchForm.setClientId(binSkuForm1.getClientId());
        binSkuDataList = binSkuDto.searchBinSku(binSkuSearchForm);
        assertEquals(1,binSkuDataList.size());
        binSkuSearchForm.setBinId(0L);
        binSkuSearchForm.setClientId(0L);
        binSkuDataList = binSkuDto.searchBinSku(binSkuSearchForm);
        assertEquals(2,binSkuDataList.size());
    }

    @Test(expected = ApiException.class)
    public void testUpdateBinSku(){
        addDetails();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        binSkuDto.addBinSku(binSkuCsvForm,result);
        List<BinSkuData> binSkuDataList = binSkuDto.searchBinSku(binSkuSearchForm);
        BinSkuData binSkuData = binSkuDto.getBinSku(binSkuDataList.get(0).getBinSkuId());
        binSkuUpdateForm.setGlobalSkuId(binSkuData.getGlobalSkuId());
        binSkuDto.updateBinSkuInventory(binSkuData.getBinSkuId(),binSkuUpdateForm);
        binSkuData = binSkuDto.getBinSku(binSkuDataList.get(0).getBinSkuId());
        assertEquals(binSkuUpdateForm.getUpdateQuantity(),binSkuData.getQuantity());
        binSkuUpdateForm.setUpdateQuantity(-1L);
        // throw exception
        binSkuDto.updateBinSkuInventory(binSkuData.getBinSkuId(),binSkuUpdateForm);
    }



}
