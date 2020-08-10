package com.assure.dto;

import com.assure.spring.AbstractUnitTest;
import com.commons.api.ApiException;
import com.commons.api.CustomValidationException;
import com.commons.enums.ClientType;
import com.commons.form.ClientForm;
import com.commons.form.ProductCsvForm;
import com.commons.form.ProductForm;
import com.commons.form.ProductSearchForm;
import com.commons.response.ClientData;
import com.commons.response.ProductData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductDtoTest extends AbstractUnitTest {
    private ClientForm clientForm1;
    private ProductForm productForm1,productForm2,productForm3;
    private final ProductCsvForm productCsvForm = new ProductCsvForm();
    private ProductSearchForm productSearchForm;

    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductDto productDto;

    @Before
    public void setUp(){
        clientForm1 = createClientForm();
        productForm1 = createProductForm("munch",10.00,"excellent","prod1","nestle");
        productForm2 = createProductForm("kitkat",15.00,"nice","prod2","britannia");
        productForm3 = createProductForm("munch",10.50,"nice","prod3","nestle");
        productSearchForm = createProductSearchForm();
    }

    private ProductSearchForm createProductSearchForm() {
        ProductSearchForm productSearchForm = new ProductSearchForm();
        productSearchForm.setClientSkuId("");
        productSearchForm.setClientId(0L);
        return productSearchForm;
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
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm1);
        productFormList.add(productForm2);
        productCsvForm.setProductFormList(productFormList);

    }

    // test for adding product
    @Test(expected = CustomValidationException.class)
    public void testAddProduct(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        productDto.addProducts(productCsvForm,result);
        when(result.hasErrors()).thenReturn(true);
        productDto.addProducts(productCsvForm,result);
    }

    // test for getting product by id
    @Test
    public void testGetProduct(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        List<ProductData> productDataList = productDto.addProducts(productCsvForm,result);
        Long globalSkuId = productDataList.get(0).getGlobalSkuId();
        ProductData productData = productDto.getProduct(globalSkuId);
        assertNotNull(productData);
        // test added data
        assertEquals(productDataList.get(0).getProductName(),productData.getProductName());
        assertEquals(productDataList.get(0).getClientSkuId(),productData.getClientSkuId());
        assertEquals(productDataList.get(0).getDescription(),productData.getDescription());
        assertEquals(productDataList.get(0).getClientName(),productData.getClientName());
        assertEquals(productDataList.get(0).getBrandId(),productData.getBrandId());
        assertEquals(productDataList.get(0).getMrp(),productData.getMrp(),0.01);
    }

    // test for search product
    @Test
    public void testSearch(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        productDto.addProducts(productCsvForm,result);
        List<ProductData> productDataList = productDto.searchProducts(productSearchForm);
        assertEquals(2,productDataList.size());
        // update search form
        productSearchForm.setClientSkuId("prod1");
        productSearchForm.setClientId(productForm1.getClientId());
        productDataList = productDto.searchProducts(productSearchForm);
        assertEquals(1,productDataList.size());
    }

    // test for updating product
    @Test
    public void testUpdateProduct(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        List<ProductData> productDataList = productDto.addProducts(productCsvForm,result);
        Long globalSkuId = productDataList.get(0).getGlobalSkuId();
        // update product
        ProductData productData = productDto.updateProduct(globalSkuId,productForm3);
        assertNotNull(productData);
        assertEquals(productForm3.getMrp(),productData.getMrp(),0.01);
        assertEquals(productForm3.getDescription(),productData.getDescription());
    }

    // test for getting all product
    @Test
    public void testGetAll(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        productDto.addProducts(productCsvForm,result);
        List<ProductData> productDataList = productDto.getAllProducts();
        assertEquals(2,productDataList.size());
    }

    // test for getting data by clientId and clientSkuId
    @Test
    public void testGetProductByClientDetails(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        productDto.addProducts(productCsvForm,result);
        ProductData productData = productDto.getProductByClientIdAndClientSkuId(productForm1.getClientId(),productForm1.getClientSkuId());
        assertNotNull(productData);
        // test data
        assertEquals(productForm1.getProductName(),productData.getProductName());
        assertEquals(productForm1.getClientSkuId(),productData.getClientSkuId());
        assertEquals(productForm1.getDescription(),productData.getDescription());
        assertEquals(productForm1.getBrandId(),productData.getBrandId());
        assertEquals(productForm1.getMrp(),productData.getMrp(),0.01);
        productData = productDto.getProductByClientIdAndClientSkuId(productForm1.getClientId()+1,productForm2.getClientSkuId());
        assertNull(productData);
    }

    // test for validation
    @Test(expected = ApiException.class)
    public void testValidate(){
        productDto.validate(productForm1);
        productForm1.setMrp(0);
        // throw exception
        productDto.validate(productForm1);
    }

    // test for getting clientSku by clientId
    @Test
    public void testGetClientSkuByClientId(){
        addProduct();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        productDto.addProducts(productCsvForm,result);
        List<String> clientSkuIdList = productDto.getClientSkuIds(productForm1.getClientId());
        assertEquals(2,clientSkuIdList.size());
        assertEquals(productForm1.getClientSkuId(),clientSkuIdList.get(0));
        assertEquals(productForm2.getClientSkuId(),clientSkuIdList.get(1));

    }



}
