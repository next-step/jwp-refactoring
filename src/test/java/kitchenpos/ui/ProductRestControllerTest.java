package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    private static final String URI = "/api/products";

    @InjectMocks
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductRestController productRestController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;
    private Product 진라면_매운맛;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
        진라면_매운맛 = new Product();
        진라면_매운맛.setName("진라면_매운맛");
    }

    @Test
    void post() throws Exception {
        // given
        given(productService.create(any())).willReturn(진라면_매운맛);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(진라면_매운맛)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("진라면_매운맛"))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        // given
        given(productService.list()).willReturn(Collections.singletonList(진라면_매운맛));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(진라면_매운맛)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("진라면_매운맛"))
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }
}
