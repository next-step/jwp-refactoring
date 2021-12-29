package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.ui.MenuGroupRestController;
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

import java.util.Collections;
import java.util.List;

import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.한마리메뉴그룹요청;
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
@DisplayName("메뉴그룹 컨트롤러 테스트")
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    private MenuGroupRequest request;
    private MenuGroupResponse response;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        request = 한마리메뉴그룹요청();
        response = MenuGroupResponse.of(request.toEntity());
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다.")
    public void getMenuGroup() throws Exception {
        // given
        List<MenuGroupResponse> menuGroups = Collections.singletonList(response);
        given(menuGroupService.list()).willReturn(menuGroups);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(request.getName())))
                .andDo(print());
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    public void postMenuGroup() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(menuGroupService.create(any(MenuGroupRequest.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", CoreMatchers.is(request.getName())))
                .andDo(print());
    }
}
