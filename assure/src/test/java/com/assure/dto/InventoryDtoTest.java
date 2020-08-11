package com.assure.dto;

import com.assure.model.form.BinForm;
import com.assure.model.form.BinSkuCsvForm;
import com.assure.model.form.BinSkuForm;
import com.assure.model.form.InventorySearchForm;
import com.assure.model.response.InventoryData;
import com.assure.pojo.Inventory;
import com.assure.spring.AbstractUnitTest;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InventoryDtoTest extends AbstractUnitTest {

    private ClientForm clientForm1,clientForm2;
    private ProductForm productForm1,productForm2;
    private BinForm binForm1,binForm2;
    private ProductCsvForm productCsvForm = new ProductCsvForm();
    private BinSkuCsvForm binSkuCsvForm = new BinSkuCsvForm();
    private BinSkuForm binSkuForm1,binSkuForm2;
    private InventorySearchForm inventorySearchForm;

    @Autowired
    private ClientDto clientDto;
    @Autowired
    private ProductDto productDto;
    @Autowired
    private BinDto binDto;
    @Autowired
    private BinSkuDto binSkuDto;
    @Autowired
    private InventoryDto inventoryDto;
    @Before
    public void setUp() {
        clientForm1 = createClientForm("viram", ClientType.CLIENT);
        clientForm2 = createClientForm("viram308", ClientType.CUSTOMER);
        productForm1 = createProductForm("munch", 10.00, "excellent", "prod1", "nestle");
        productForm2 = createProductForm("kitkat", 15.00, "nice", "prod2", "britannia");
        binForm1 = createBinForm(1L);
        binForm2 = createBinForm(1L);
        binSkuForm1 = createBinSkuForm(20L);
        binSkuForm2 = createBinSkuForm(30L);
        inventorySearchForm = createInventorySearchForm();
    }

    private InventorySearchForm createInventorySearchForm() {
        return new InventorySearchForm();
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

    private void addDetails(){
        ClientData clientData1 = clientDto.addClient(clientForm1);
        ClientData clientData2 = clientDto.addClient(clientForm2);
        // set client id
        productForm1.setClientId(clientData1.getId());
        productForm2.setClientId(clientData1.getId());
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm1);
        productFormList.add(productForm2);
        productCsvForm.setProductFormList(productFormList);
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);
        // add products
        productDto.addProducts(productCsvForm,result);
        // add bins
        binDto.addBins(binForm1);
        binDto.addBins(binForm2);
        // setting binSku data
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
        BindingResult result1 = mock(BindingResult.class);
        when(result1.hasErrors()).thenReturn(false);
        // add binSku
        binSkuDto.addBinSku(binSkuCsvForm,result1);
        inventorySearchForm.setClientId(clientData1.getId());
    }

    // test for getting all inventory
    @Test
    public void testGetAll(){
        addDetails();
        List<InventoryData> inventoryDataList = inventoryDto.getAllInventory();
        assertEquals(2,inventoryDataList.size());
    }

    // test for search inventory
    @Test
    public void testGetSearchInventory(){
        addDetails();
        inventorySearchForm.setClientId(0L);
        // search inventory
        List<InventoryData> inventoryDataList = inventoryDto.searchInventory(inventorySearchForm);
        assertEquals(2,inventoryDataList.size());
        // update search form
        inventorySearchForm.setClientId(productForm1.getClientId());
        inventorySearchForm.setClientSkuId("");
        inventoryDataList = inventoryDto.searchInventory(inventorySearchForm);
        assertEquals(2,inventoryDataList.size());
        // update search form
        inventorySearchForm.setClientSkuId("prod1");
        inventoryDataList = inventoryDto.searchInventory(inventorySearchForm);
        assertEquals(1,inventoryDataList.size());
        inventorySearchForm.setClientSkuId("prod22");
        inventoryDataList = inventoryDto.searchInventory(inventorySearchForm);
        assertEquals(0,inventoryDataList.size());
    }

}
