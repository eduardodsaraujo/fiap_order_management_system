package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.utils.ProductHelper;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        // Arrange
        CreateProductInput productInput = ProductHelper.createProductInput();

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productInput)
                .when()
                .post("/api/products")
                .then()
                .statusCode(HttpStatus.OK.value())
//                .body(matchesJsonSchemaInClasspath("./schemas/CustomerResponseSchema.json"))
                .body("code", equalTo(productInput.getCode()))
                .body("name", equalTo(productInput.getName()))
                .body("description", equalTo(productInput.getDescription()))
                .body("category", equalTo(productInput.getCategory()))
                .body("enable", is(true))
                .body("price", equalTo(productInput.getPrice()))
                .body("weight", equalTo(productInput.getWeight()));

    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        // Arrange
        long productId = 1L;

        UpdateProductInput updateProductInput = UpdateProductInput.builder()
                .code("IP16PM")
                .name("IPhone 16 Pro Max")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .price(8000.0)
                .weight(1.0)
                .build();


        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProductInput)
                .when()
                .put("/api/products/{productId}", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
//                .body(matchesJsonSchemaInClasspath("./schemas/CustomerResponseSchema.json"))
                .body("code", equalTo(updateProductInput.getCode()))
                .body("name", equalTo(updateProductInput.getName()))
                .body("description", equalTo(updateProductInput.getDescription()))
                .body("category", equalTo(updateProductInput.getCategory()))
                .body("enable", is(true))
                .body("price", equalTo(updateProductInput.getPrice()))
                .body("weight", equalTo(updateProductInput.getWeight()));
    }

//    @Test
//    public void shouldEnableProduct() throws Exception {
//        // Arrange
//        long productId = 1L;
//        Product product = ProductHelper.createProduct();
//        product.setId(productId);
//
//        when(productService.enable(anyLong())).thenReturn(product);
//
//        // Act
//        mockMvc.perform(put("/api/products/{productId}/enable", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(productId))
//                .andExpect(jsonPath("$.enable").value(true));
//
//        // Assert
//        verify(productService, times(1)).enable(anyLong());
//    }
//
//    @Test
//    public void shouldDisableProduct() throws Exception {
//        // Arrange
//        long productId = 1L;
//        Product product = ProductHelper.createProduct();
//        product.setId(productId);
//        product.setEnable(false);
//
//        when(productService.disable(anyLong())).thenReturn(product);
//
//        // Act
//        mockMvc.perform(put("/api/products/{productId}/disable", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(productId))
//                .andExpect(jsonPath("$.enable").value(false));
//
//        // Assert
//        verify(productService, times(1)).disable(anyLong());
//    }
//
//    @Test
//    public void shouldFindProductById() throws Exception {
//        // Arrange
//        long productId = 1L;
//        Product product = ProductHelper.createProduct();
//        product.setId(productId);
//        product.setEnable(false);
//
//        when(productService.findById(anyLong())).thenReturn(product);
//
//        // Act
//        mockMvc.perform(get("/api/products/{productId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(product.getId()))
//                .andExpect(jsonPath("$.code").value(product.getCode()))
//                .andExpect(jsonPath("$.name").value(product.getName()))
//                .andExpect(jsonPath("$.description").value(product.getDescription()))
//                .andExpect(jsonPath("$.category").value(product.getCategory()))
//                .andExpect(jsonPath("$.enable").value(product.isEnable()))
//                .andExpect(jsonPath("$.price").value(product.getPrice()))
//                .andExpect(jsonPath("$.weight").value(product.getWeight()));
//
//        // Assert
//        verify(productService, times(1)).findById(anyLong());
//    }
//
//    @Test
//    public void shouldFindAllProductsById() throws Exception {
//        // Arrange
//        long productId = 1L;
//        Product product = ProductHelper.createProduct();
//        product.setId(productId);
//
//        long productId2 = 1L;
//        Product product2 = ProductHelper.createProduct();
//        product2.setId(productId2);
//        var products = List.of(product, product2);
//
//        when(productService.findAllById(anyList())).thenReturn(products);
//
//        // Act
//        mockMvc.perform(get("/api/products/all/{productsId}", Arrays.asList(productId, productId2))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(productId))
//                .andExpect(jsonPath("$[0].code").value(product.getCode()))
//                .andExpect(jsonPath("$[0].name").value(product.getName()))
//                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
//                .andExpect(jsonPath("$[0].category").value(product.getCategory()))
//                .andExpect(jsonPath("$[0].enable").value(product.isEnable()))
//                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
//                .andExpect(jsonPath("$[0].weight").value(product.getWeight()));
//
//        // Assert
//        verify(productService, times(1)).findAllById(anyList());
//    }
//
//    @Test
//    public void shouldFindAllProductsByName() throws Exception {
//        long productId = 1L;
//        Product product = ProductHelper.createProduct();
//        product.setId(productId);
//
//        long productId2 = 1L;
//        Product product2 = ProductHelper.createProduct();
//        product2.setId(productId2);
//        var products = List.of(product, product2);
//
//        when(productService.findAllByName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(products));
//
//        // Act
//        mockMvc.perform(get("/api/products", Arrays.asList(productId, productId2))
//                        .queryParam("name", product.getName())
//                        .queryParam("page", "0")
//                        .queryParam("size", "10")
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(productId))
//                .andExpect(jsonPath("$[0].code").value(product.getCode()))
//                .andExpect(jsonPath("$[0].name").value(product.getName()))
//                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
//                .andExpect(jsonPath("$[0].category").value(product.getCategory()))
//                .andExpect(jsonPath("$[0].enable").value(product.isEnable()))
//                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
//                .andExpect(jsonPath("$[0].weight").value(product.getWeight()));
//
//        // Assert
//        verify(productService, times(1)).findAllByName(anyString(), any(Pageable.class));
//    }
//
//
//    @Test
//    void shouldReturnBadRequestWhenCodeMissing() throws Exception {
//        // Arrange
//        CreateProductInput productInput = ProductHelper.createProductInput();
//        productInput.setCode(null);
//
//        // Act
//        ResultActions result = mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(productInput)))
//                .andDo(print());
//
//        // Assert
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Validation error"))
//                .andExpect(jsonPath("$.fieldErrors.code").value("Code is required."));
//
//    }
//
//
//    @Test
//    void shouldReturnBadRequestWhenCodeExceededSize() throws Exception {
//        // Arrange
//        CreateProductInput productInput = ProductHelper.createProductInput();
//        productInput.setCode(RandomString.make(30));
//
//        // Act
//        ResultActions result = mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(productInput)))
//                .andDo(print());
//
//        // Assert
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Validation error"))
//                .andExpect(jsonPath("$.fieldErrors.code").value("Code must not exceed 20 characters."));
//
//    }


}