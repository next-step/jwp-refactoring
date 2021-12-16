package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.hamcrest.CoreMatchers;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : MenuGroupRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    private MenuGroup menuGroup;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setName("양념치킨");
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다.")
    public void getMenuGroup() throws Exception {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup);
        given(menuGroupService.list()).willReturn(menuGroups);

        // when
        ResultActions actions = mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(menuGroup.getName())))
                .andDo(print());
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    public void postMenuGroup() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(menuGroupService.create(any(MenuGroup.class))).willReturn(menuGroup);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(menuGroup))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", CoreMatchers.is(menuGroup.getName())))
                .andDo(print());
    }
}