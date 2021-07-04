package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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

    @MockBean
    private MenuService menuService;

    private ObjectMapper objectMapper = new ObjectMapper();

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
        Menu menu = new Menu(7L, "쓰리라차치킨", BigDecimal.valueOf(20000), 2L, menuProducts);
        String jsonString = objectMapper.writeValueAsString(menu);

        given(menuService.create(any())).willReturn(menu);

        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void list() throws Exception {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(7L, 7L, 7L, 1));
        Menu menu = new Menu(7L, "쓰리라차치킨", BigDecimal.valueOf(20000), 2L, menuProducts);

        given(menuService.list()).willReturn(Arrays.asList(menu));

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쓰리라차치킨")));
    }
}
