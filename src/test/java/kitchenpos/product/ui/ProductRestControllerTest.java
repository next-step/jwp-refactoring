package kitchenpos.product.ui;

import kitchenpos.ControllerTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.exception.ProductPriceException;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ProductRestControllerTest extends ControllerTest {
    @MockBean
    private ProductService productService;

    @DisplayName("상품생성을 요청하면 생성된 상품응답")
    @Test
    public void returnProduct() throws Exception {
        ProductResponse product = getProductResponse();
        doReturn(product).when(productService).create(any(ProductRequest.class));

        webMvc.perform(post("/api/products")
                .content(mapper.writeValueAsString(new ProductRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice().intValue())))
                .andExpect(status().isCreated());
    }

    @DisplayName("상품생성을 요청하면 상품생성 실패응답")
    @Test
    public void throwsExceptionWhenProductCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(productService).create(any(ProductRequest.class));

        webMvc.perform(post("/api/products")
                .content(mapper.writeValueAsString(new ProductRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("상품생성중 상품가격이 0미만이면 실패응답")
    @Test
    public void throwsExceptionWhenNegativePrice() throws Exception {
        doThrow(new ProductPriceException("상품가격은 0이상 이어야합니다")).when(productService).create(any(ProductRequest.class));

        webMvc.perform(post("/api/products")
                .content(mapper.writeValueAsString(new ProductRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is("상품가격은 0이상 이어야합니다")))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("상품목록을 요청하면 상품목록을 응답")
    @Test
    public void returnProducts() throws Exception {
        Product product1 = Product.builder()
                .id(Arbitraries.longs().between(1, 5).sample())
                .price(ProductPrice.of(Arbitraries.bigDecimals().between(BigDecimal.valueOf(1000), BigDecimal.valueOf(1500)).sample()))
                .build();
        Product product2 = Product.builder()
                .id(Arbitraries.longs().between(1, 5).sample())
                .price(ProductPrice.of(Arbitraries.bigDecimals().between(BigDecimal.valueOf(1000), BigDecimal.valueOf(1500)).sample()))
                .build();
        List<Product> products = Arrays.asList(product1, product2);
        doReturn(products).when(productService).list();

        webMvc.perform(get("/api/products"))
                .andExpect(jsonPath("$", hasSize(products.size())))
                .andExpect(status().isOk());
    }

    private ProductResponse getProductResponse() {
        return ProductResponse.of(Product.builder()
                .id(Arbitraries.longs().between(1, 100).sample())
                .name(Arbitraries.strings().ofMinLength(5).ofMaxLength(15).sample())
                .price(ProductPrice.of(BigDecimal.valueOf(20000)))
                .build());
    }
}
