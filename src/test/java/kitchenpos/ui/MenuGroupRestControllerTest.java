package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    private static final String MENU_GROUP_API_URI = "/api/menu-groups";

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    private MockMvc mockMvc;
    private MenuGroup 추천메뉴;
    private MenuGroup 인기메뉴;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        인기메뉴 = new MenuGroup(2L, "인기메뉴");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(menuGroupService.create(any())).willReturn(추천메뉴);

        // when
        ResultActions actions = mockMvc.perform(post(MENU_GROUP_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(추천메뉴)));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", MENU_GROUP_API_URI + "/1"))
                .andExpect(content().string(containsString("추천메뉴")));
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        // given
        given(menuGroupService.list()).willReturn(Arrays.asList(추천메뉴, 인기메뉴));

        // when
        ResultActions actions = mockMvc.perform(get(MENU_GROUP_API_URI));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("추천메뉴")))
                .andExpect(content().string(containsString("인기메뉴")));
    }
}
