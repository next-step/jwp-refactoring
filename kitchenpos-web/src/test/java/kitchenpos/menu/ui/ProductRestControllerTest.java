package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.fixtures.ProductFixtures.후라이드요청;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : ProductRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@DisplayName("상품 컨트롤러 테스트")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    private ProductRequest request;
    private ProductResponse response;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        request = 후라이드요청();
        response = ProductResponse.of(request.toEntity());
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    public void findProducts() throws Exception {
        // given
        List<ProductResponse> products = Arrays.asList(response);
        given(productService.list()).willReturn(products);

        // when
        ResultActions actions = mockMvc.perform(
            get("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(request.getName())))
                .andDo(print());
    }

    @Test
    @DisplayName("상품을 등록한다.")
    public void saveProduct() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(productService.create(any(ProductRequest.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andDo(print());
    }
}
