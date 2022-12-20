package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductRestController 테스트")
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    private Product 후라이드치킨;
    private Product 양념치킨;

    @BeforeEach
    public void setUp() {
        후라이드치킨 = new Product("후라이드치킨", 16000);
        양념치킨 = new Product("양념치킨", 17000);

        ReflectionTestUtils.setField(후라이드치킨, "id", 1L);
        ReflectionTestUtils.setField(양념치킨, "id", 2L);
    }

    @DisplayName("상품 등록에 실패한다.")
    @Test
    void 상품_등록에_실패한다() throws Exception {
        given(productService.create(any(ProductRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(후라이드치킨))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("상품 등록에 성공한다.")
    @Test
    void 상품_등록에_성공한다() throws Exception {
        given(productService.create(any(ProductRequest.class))).willReturn(new ProductResponse(양념치킨));

        webMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(new ProductRequest("양념치킨", 17000)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(양념치킨.getId().intValue())))
                .andExpect(jsonPath("$.name", is(양념치킨.getName())))
                .andExpect(jsonPath("$.price", is(양념치킨.getPrice().value().intValue())));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록을_조회한다() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(new ProductResponse(후라이드치킨), new ProductResponse(양념치킨)));

        webMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
