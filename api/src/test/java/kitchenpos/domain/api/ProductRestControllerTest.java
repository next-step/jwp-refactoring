package kitchenpos.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.api.ProductRestController;
import kitchenpos.service.product.application.ProductService;
import kitchenpos.service.product.dto.ProductRequest;
import kitchenpos.service.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductRestController productRestController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
    }

    @Test
    void test_get() throws Exception {
        //given
        given(productService.list()).willReturn(Collections.singletonList(new ProductResponse()));

        //then
        mockMvc.perform(get("/api/products")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void test_post() throws Exception {
        //given
        given(productService.create(any())).willReturn(new ProductResponse());

        //then
        mockMvc.perform(post("/api/products")
                .content(objectMapper.writeValueAsString(new ProductRequest("product", 0)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
