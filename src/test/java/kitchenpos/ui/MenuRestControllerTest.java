package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
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
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {
    private static final String MENU_API_URI = "/api/menus";

    @Autowired
    private MenuRestController menuRestController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    private MockMvc mockMvc;
    private Menu 강정치킨plus강정치킨;
    private Menu 후라이드plus후라이드;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        강정치킨plus강정치킨 = new Menu(1L, "강정치킨+강정치킨", BigDecimal.valueOf(20000), 1L, new ArrayList<>());
        후라이드plus후라이드 = new Menu(2L, "후라이드+후라이드", BigDecimal.valueOf(19000), 1L, new ArrayList<>());
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(menuService.create(any())).willReturn(강정치킨plus강정치킨);

        // when
        ResultActions actions = mockMvc.perform(post(MENU_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(강정치킨plus강정치킨)));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", MENU_API_URI + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("강정치킨+강정치킨")))
                .andExpect(content().string(containsString("20000")))
                .andExpect(content().string(containsString("1")));
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        // given
        given(menuService.list()).willReturn(Arrays.asList(강정치킨plus강정치킨, 후라이드plus후라이드));

        // when
        ResultActions actions = mockMvc.perform(get(MENU_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(강정치킨plus강정치킨)));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("강정치킨+강정치킨")))
                .andExpect(content().string(containsString("20000")))
                .andExpect(content().string(containsString("후라이드+후라이드")))
                .andExpect(content().string(containsString("19000")));
    }
}
