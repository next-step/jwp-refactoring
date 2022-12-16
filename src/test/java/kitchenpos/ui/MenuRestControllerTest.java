package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductIdQuantityPair;

class MenuRestControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        this.menuGroup = menuGroupRepository.save(new MenuGroup(new Name("test")));
        this.product = productRepository.save(new Product(new Name("test"), new Price(BigDecimal.valueOf(100L))));
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void test1() throws Exception {
        Long id = 메뉴_등록();
        assertThat(menuRepository.findById(id)).isNotEmpty();
    }

    @DisplayName("전체 메뉴를 조회한다")
    @Test
    void test2() throws Exception {
        메뉴_등록();

        mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..id").exists())
            .andExpect(jsonPath("$..name").exists())
            .andExpect(jsonPath("$..price").exists())
            .andExpect(jsonPath("$..menuGroupId").exists())
            .andExpect(jsonPath("$..menuProducts").exists());
    }

    private Long 메뉴_등록() throws Exception {
        List<ProductIdQuantityPair> menuProducts = Collections.singletonList(
            new ProductIdQuantityPair(this.product.getId(), new Quantity(2L)));
        MenuRequest request = new MenuRequest(new Name("test"), new Price(BigDecimal.valueOf(100L)), menuGroup.getId(),
            menuProducts);

        MvcResult result = mockMvc.perform(post("/api/menus")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.getName().value()))
            .andExpect(jsonPath("$.price").value(request.getPrice().value()))
            .andExpect(jsonPath("$.menuGroupId").value(request.getMenuGroupId()))
            .andExpect(jsonPath("$.menuProducts.length()").value(menuProducts.size()))
            .andReturn();

        return getId(result);
    }

    private Long getId(MvcResult result) throws
        com.fasterxml.jackson.core.JsonProcessingException,
        UnsupportedEncodingException {
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, MenuResponse.class).getId();
    }

}
