package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    MenuProductRepository menuProductRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ObjectMapper objectMapper;

    MenuProduct savedMenuProduct;
    MenuGroup savedMenuGroup;
    Product savedProduct;


    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("패스트푸드");
        savedMenuGroup = menuGroupRepository.save(menuGroup);

        Product product = new Product("빅맥", BigDecimal.valueOf(5000));
        savedProduct = productRepository.save(product);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void resetData() {

    }

    @DisplayName("메뉴등록 api 테스트")
    @Test
    public void create() throws Exception {
        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), new Quantity(1));

        MenuRequest menu = new MenuRequest("햄버거세트", BigDecimal.valueOf(5000), savedMenuGroup.getId(), Arrays.asList(menuProductRequest));

        String requestBody = objectMapper.writeValueAsString(menu);

        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
        ;

    }

    @DisplayName("메뉴 목록 Api 테스트")
    @Test
    void list() throws Exception {
        Menu newMenu = new Menu("햄버거세트", BigDecimal.valueOf(5000), savedMenuGroup);
        Menu savedNewMenu = menuRepository.save(newMenu);

        MenuProduct menuProduct = new MenuProduct(savedNewMenu, savedProduct, new Quantity(1));
        savedMenuProduct = menuProductRepository.save(menuProduct);

        long countOfMenus = menuRepository.count();

        mockMvc.perform(get("/api/menus")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize((int) countOfMenus)))
        ;
    }
}
