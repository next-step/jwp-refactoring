package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.config.MockMvcTestConfig;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuGroupRestController.class)
@MockMvcTestConfig
class MenuGroupRestControllerTest {
    private static final String MENU_GROUP_API_URI = "/api/menu-groups";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroupResponse 추천메뉴;
    private MenuGroupResponse 인기메뉴;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroupResponse(1L, "추천메뉴");
        인기메뉴 = new MenuGroupResponse(2L, "인기메뉴");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");
        given(menuGroupService.create(any())).willReturn(추천메뉴);

        // when
        ResultActions actions = mockMvc.perform(post(MENU_GROUP_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)))
                .andDo(print());

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
        ResultActions actions = mockMvc.perform(get(MENU_GROUP_API_URI))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("추천메뉴")))
                .andExpect(content().string(containsString("인기메뉴")));
    }
}
