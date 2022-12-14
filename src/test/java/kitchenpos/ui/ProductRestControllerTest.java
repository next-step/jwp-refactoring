package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("상품 ui 테스트")
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() throws Exception {
        //given:
        final Product 상품 = 상품("상품", BigDecimal.ONE);
        //when:
        final Product 저장된_상품 = mapper.readValue(
                mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(상품)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), Product.class);
        //then:
        assertThat(저장된_상품).isEqualTo(상품);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() throws Exception {
        //when:
        final List<Product> 상품_목록 = Arrays.asList(mapper.readValue(
                mockMvc.perform(get("/api/products")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(), Product[].class));
        //then:
        assertThat(상품_목록).isNotEmpty();
    }
}
