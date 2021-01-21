package kitchenpos.api.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.menu.MenuService;
import kitchenpos.common.BaseTest;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.dto.MenuProductRequest;
import kitchenpos.domain.menu.dto.MenuRequest;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 컨트롤러 테스트")
class MenuRequestRestControllerTest extends BaseTest {
    private static final String DEFAULT_MENU_URI = "/api/menus";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        productRepository.save(Product.of("후라이드", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("양념치킨", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("반반치킨", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("통구이", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("간장치킨", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("순살치킨", Price.of(new BigDecimal(16_000L))));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() throws Exception {
        final List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 2));
        menuProductRequests.add(new MenuProductRequest(2L, 1));

        final MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setName("순살파닭두마리메뉴");

        final MenuGroup savedMenuGroup = menuGroupService.create(newMenuGroup);

        final MenuRequest menuRequest = new MenuRequest("후라이드치킨", 29_000L, savedMenuGroup.getId(), menuProductRequests);

        final String jsonTypeMenuGroup = objectMapper.writeValueAsString(menuRequest);

        mockMvc.perform(post(DEFAULT_MENU_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeMenuGroup))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(menuRequest.getName()));
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void 메뉴_조회() throws Exception {
        final List<MenuProductRequest> firstMenuProductRequests = new ArrayList<>();
        firstMenuProductRequests.add(new MenuProductRequest(1L, 2));
        firstMenuProductRequests.add(new MenuProductRequest(2L, 1));
        final MenuGroup firstMenuGroup = new MenuGroup();
        firstMenuGroup.setName("순살파닭두마리메뉴");
        final MenuGroup savedFirstMenuGroup = menuGroupService.create(firstMenuGroup);
        final MenuRequest firstMenuRequest = new MenuRequest("후라이드치킨", 29_000L, savedFirstMenuGroup.getId(), firstMenuProductRequests);
        menuService.create(firstMenuRequest);

        final List<MenuProductRequest> secondMenuProductRequests = new ArrayList<>();
        secondMenuProductRequests.add(new MenuProductRequest(3L, 1));
        secondMenuProductRequests.add(new MenuProductRequest(4L, 1));
        final MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setName("신메뉴");
        final MenuGroup savedSecondMenuGroup = menuGroupService.create(secondMenuGroup);
        final MenuRequest secondMenuRequest = new MenuRequest("간장치킨", 28_000L, savedSecondMenuGroup.getId(), secondMenuProductRequests);
        menuService.create(secondMenuRequest);

        mockMvc.perform(get(DEFAULT_MENU_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].name").value(firstMenuRequest.getName()))
            .andExpect(jsonPath("$[1].id").exists())
            .andExpect(jsonPath("$[1].name").value(secondMenuRequest.getName()));
    }
}
