package com.assure.validator;

import com.assure.dto.BinDto;
import com.assure.dto.BinSkuDto;
import com.assure.dto.ClientDto;
import com.assure.dto.ProductDto;
import com.assure.model.form.BinForm;
import com.assure.model.form.BinSkuCsvForm;
import com.assure.model.form.BinSkuForm;
import com.assure.spring.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.form.ClientForm;
import com.commons.form.ProductCsvForm;
import com.commons.form.ProductForm;
import com.commons.response.ClientData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BinSkuCsvFormValidatorTest extends AbstractUnitTest {

    private ClientForm clientForm1,clientForm2;
    private ProductForm productForm1,productForm2,productForm3;
    private BinForm binForm1,binForm2;
    private ProductCsvForm productCsvForm = new ProductCsvForm();
    private BinSkuCsvForm binSkuCsvForm = new BinSkuCsvForm();
    private BinSkuForm binSkuForm1,binSkuForm2;
    @Autowired
    private BinSkuCsvFormValidator binSkuCsvFormValidator;
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
        productForm3 = createProductForm("munch",10.50,"nice","prod1","nestle");
        binForm1 = createBinForm(1L);
        binForm2 = createBinForm(1L);
        binSkuForm1 = createBinSkuForm(20L);
        binSkuForm2 = createBinSkuForm(30L);
    }

    private BinSkuForm createBinSkuForm(Long quantity) {
        BinSkuForm binSkuForm = new BinSkuForm();
        binSkuForm.setQuantity(quantity);
        return binSkuForm;
    }

    private BinForm createBinForm(Long noOfBins){
        BinForm binForm = new BinForm();
        binForm.setNoOfBins(noOfBins);
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

    private void addDetails() {
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
        productDto.addProducts(productCsvForm, result);
        List<Long> binIdList1 = binDto.addBins(binForm1);
        List<Long> binIdList2 = binDto.addBins(binForm2);
        binSkuForm1.setClientId(clientData1.getId());
        binSkuForm1.setClientSkuId(productForm1.getClientSkuId());
        binSkuForm1.setBinId(binIdList1.get(0));
        binSkuForm2.setClientId(clientData1.getId());
        binSkuForm2.setClientSkuId(productForm1.getClientSkuId());
        binSkuForm2.setBinId(binIdList2.get(0));
    }
    @Test
    public void testValidate(){
        addDetails();
        binSkuForm1.setClientId(0L);
        binSkuForm2.setClientId(0L);
        List<BinSkuForm> binSkuFormList = new ArrayList<>();
        binSkuFormList.add(binSkuForm1);
        binSkuFormList.add(binSkuForm2);
        binSkuCsvForm.setBinSkuFormList(binSkuFormList);
        Errors errors = new BeanPropertyBindingResult(binSkuCsvForm,"Invalid Csv");
        binSkuCsvFormValidator.validate(binSkuCsvForm,errors);
        assertTrue(errors.hasErrors());
        assertEquals(4,errors.getErrorCount());
    }
}
