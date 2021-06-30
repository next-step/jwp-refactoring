package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.ProductCreate;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.ui.JsonUtil.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductRestController.class)
@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("[post]/api/products - 상품의 가격이 비어 있거나, 0원보다 적을경우 BadRequest이다.")
    void 상품의_가격이_비어_있거나_0원보다_적을경우_BadRequest이다() throws Exception {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("name", BigDecimal.valueOf(-1));

        given(productService.create(any(ProductCreate.class))).willAnswer(i -> i.getArgument(0));

        // when
        MvcResult mvcResult = mockMvc.perform(
                post("/api/products")
                        .content(toJson(productCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        assertThat(mvcResult.getResolvedException()).isInstanceOf(InvalidPriceException.class);
    }
}