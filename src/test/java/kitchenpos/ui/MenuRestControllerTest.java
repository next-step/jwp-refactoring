package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.api.MenuRestController;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {
    private static final String URI = "/api/menus";

    @Autowired
    private MenuRestController menuRestController;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Menu 후라이드반_양념반;
    private Menu 후라이드_콜라세트;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        후라이드반_양념반 = new Menu();
        후라이드반_양념반.setId(1L);
        후라이드반_양념반.setName("후라이드반+양념반");
        후라이드반_양념반.setPrice(BigDecimal.valueOf(21000));
        후라이드반_양념반.setMenuGroupId(1L);
        후라이드반_양념반.setMenuProducts(Lists.list(new MenuProduct()));
        후라이드_콜라세트 = new Menu();
        후라이드_콜라세트.setId(2L);
        후라이드_콜라세트.setName("후라이드+콜라세트");
        후라이드_콜라세트.setPrice(BigDecimal.valueOf(15000));
        후라이드_콜라세트.setMenuGroupId(1L);
        후라이드_콜라세트.setMenuProducts(Lists.list(new MenuProduct()));
    }

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(menuService.create(any())).willReturn(후라이드반_양념반);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(후라이드반_양념반)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("후라이드반+양념반")))
                .andExpect(content().string(containsString("21000")))
                .andExpect(content().string(containsString("1")));
    }

    @DisplayName("메뉴를 모두 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(menuService.list()).willReturn(Arrays.asList(후라이드반_양념반, 후라이드_콜라세트));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("후라이드반+양념반"))
                .andExpect(jsonPath("$[0].price").value(21000))
                .andExpect(jsonPath("$[0].menuGroupId").isNotEmpty())
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].name").value("후라이드+콜라세트"))
                .andExpect(jsonPath("$[1].price").value(15000))
                .andExpect(jsonPath("$[1].menuGroupId").isNotEmpty());
    }
}
