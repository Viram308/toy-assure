package com.assure.validator;

import com.assure.dto.ClientDto;
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
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ProductCsvFormValidatorTest extends AbstractUnitTest {

    private ClientForm clientForm1;
    private ProductForm productForm1,productForm2;
    private final ProductCsvForm productCsvForm = new ProductCsvForm();

    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductCsvFormValidator productCsvFormValidator;

    @Before
    public void setUp(){
        clientForm1 = createClientForm();
        productForm1 = createProductForm("munch",10.00,"excellent","prod1","nestle");
        productForm2 = createProductForm("kitkat",15.00,"nice","prod2","britannia");
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

    private ClientForm createClientForm() {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("viram");
        clientForm.setType(ClientType.CLIENT);
        return clientForm;
    }

    private void addProduct(){
        ClientData clientData = clientDto.addClient(clientForm1);
        productForm1.setClientId(clientData.getId());
        productForm2.setClientId(clientData.getId());
    }

    @Test
    public void testValidate(){
        addProduct();
        productForm1.setProductName("");
        productForm1.setBrandId("");
        productForm1.setClientId(0L);
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm1);
        productFormList.add(productForm2);
        productCsvForm.setProductFormList(productFormList);
        Errors errors = new BeanPropertyBindingResult(productCsvForm,"Invalid Csv");
        productCsvFormValidator.validate(productCsvForm,errors);
        assertTrue(errors.hasErrors());
        assertEquals(3,errors.getErrorCount());
    }

}
