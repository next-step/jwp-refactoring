package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@SpringBootTest
@AutoConfigureMockMvc
class ProductRestControllerTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService mockProductService;

    Product 짜장면, 짬뽕;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8"); // this is crucial
                chain.doFilter(request, response);
            }, "/*").build();
        짜장면 = new Product();
        짜장면.setId(1L);
        짜장면.setName("짜장면");
        짜장면.setPrice(new BigDecimal(50L));

        짬뽕 = new Product();
        짬뽕.setId(2L);
        짬뽕.setName("짬뽕");
        짬뽕.setPrice(new BigDecimal(60L));
    }
    
    @DisplayName("상품 생성")
    @Test
    void create() throws Exception {
        String jsonString = objectMapper.writeValueAsString(짜장면);
        when(mockProductService.create(any())).thenReturn(짜장면);

        mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonString)
        ).andDo(print())
        .andExpect(status().isCreated());
    }
    
    @DisplayName("상품 조회")
    @Test
    void list() throws Exception {
        when(mockProductService.list()).thenReturn(Arrays.asList(짜장면, 짬뽕));

        mockMvc.perform(get("/api/products"))
            .andDo(print()) // 요청과 응답을 출력이 가능
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]").isArray());
    }
}