package kitchenpos.product.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.ui.ProductRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends ControllerTest {
    @MockBean
    private ProductService productService;

    private Product 스테이크;
    private Product 스파게티;

    @BeforeEach
    public void setUp() {
        super.setUp();

        스테이크 = new Product("스테이크", new BigDecimal(25000));
        스파게티 = new Product("스파게티", new BigDecimal(18000));

        ReflectionTestUtils.setField(스테이크, "id", 1L);
        ReflectionTestUtils.setField(스파게티, "id", 2L);
    }

    @Test
    void 상품_등록() throws Exception {
        ProductRequest request = new ProductRequest(스테이크.getName(), 스테이크.getPrice());
        given(productService.create(any(ProductRequest.class))).willReturn(ProductResponse.of(스테이크));

        webMvc.perform(post("/api/products")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(스테이크.getId().intValue())))
                .andExpect(jsonPath("$.name", is(스테이크.getName())))
                .andExpect(jsonPath("$.price", is(스테이크.getPrice().intValue())));
    }

    @Test
    void 상품_등록_실패() throws Exception {
        ProductRequest request = new ProductRequest(스테이크.getName(), 스테이크.getPrice());
        given(productService.create(any(ProductRequest.class))).willThrow(IllegalArgumentException.class);

        webMvc.perform(post("/api/products")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 상품_목록_조회() throws Exception {
        given(productService.list())
                .willReturn(Arrays.asList(ProductResponse.of(스테이크), ProductResponse.of(스파게티)));

        webMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
