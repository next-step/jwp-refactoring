package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) //한글 깨짐 처리
                .build();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() throws Exception {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(7L, 7L, 7L, 1));
        MenuRequest menuRequest = new MenuRequest("쓰리라차치킨", 20000L, 1L, menuProducts);
        Menu menu = new Menu("쓰리라차치킨", BigDecimal.valueOf(20000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts);
        String jsonString = objectMapper.writeValueAsString(menuRequest);

        given(menuService.create(any())).willReturn(MenuResponse.from(menu));

        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void list() throws Exception {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(7L, 7L, 7L, 1));
        Menu menu = new Menu("쓰리라차치킨", BigDecimal.valueOf(20000), new MenuGroup(1L, "그룹1"), 30000L, menuProducts);
        List<MenuResponse> menus = Arrays.asList(MenuResponse.from(menu));
        given(menuService.list()).willReturn(menus);

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쓰리라차치킨")));
    }
}
