package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.fixture.TestMenuGroupFactory;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        final MenuGroupRequest 메뉴그룹_요청 = TestMenuGroupFactory.메뉴그룹_요청("메뉴그룹");
        final MenuGroupResponse 메뉴그룹_응답 = TestMenuGroupFactory.메뉴그룹_응답(1L, "메뉴그룹");

        given(menuGroupService.create(any())).willReturn(메뉴그룹_응답);

        final ResultActions actions = mvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(메뉴그룹_요청)))
                .andDo(print());
        
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(메뉴그룹_응답.getId()))
                .andExpect(jsonPath("name").value(메뉴그룹_요청.getName()));
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        final List<MenuGroupResponse> 메뉴그룹목록_응답 = TestMenuGroupFactory.메뉴그룹목록_응답(2);

        given(menuGroupService.list()).willReturn(메뉴그룹목록_응답);

        final ResultActions actions = mvc.perform(get("/api/menu-groups")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/json;charset=UTF-8")));
    }
}
