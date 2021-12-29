package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroupRequest 메뉴그룹_요청;
    private MenuGroupResponse 메뉴그룹_응답;
    private MenuGroupResponse 메뉴그룹_응답2;
    private List<MenuGroupResponse> 메뉴그룹목록_응답;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        메뉴그룹_요청 = MenuGroupRequest.from("메뉴그룹");
        메뉴그룹_응답 = MenuGroupResponse.of(1L, "메뉴그룹");
        메뉴그룹_응답2 = MenuGroupResponse.of(2L, "메뉴그룹2");
        메뉴그룹목록_응답 = Lists.newArrayList(메뉴그룹_응답, 메뉴그룹_응답2);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        
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
        
        given(menuGroupService.list()).willReturn(메뉴그룹목록_응답);

        
        final ResultActions actions = mvc.perform(get("/api/menu-groups")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString("메뉴그룹")))
                .andExpect(content().string(containsString("메뉴그룹2")));
    }
}
