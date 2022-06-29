package kitchenpos.menu.ui;

import kitchenpos.common.ui.BaseRestControllerTest;
import kitchenpos.menu.application.MenuGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static kitchenpos.common.fixture.MenuGroupFixture.메뉴묶음_요청데이터_생성;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴묶음_응답_데이터_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuGroupRestControllerTest extends BaseRestControllerTest {

    @Mock
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuGroupRestController(menuGroupService)).build();
    }

    @DisplayName("메뉴 묶음을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        String name = "menuGroup";
        String requestBody = objectMapper.writeValueAsString(메뉴묶음_요청데이터_생성(name));

        given(menuGroupService.create(any())).willReturn(메뉴묶음_응답_데이터_생성(1L, name));

        //when //then
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(name));
    }

    @DisplayName("메뉴 묶음을 전체 조회한다.")
    @Test
    void list() throws Exception {
        //given
        String name = "menuGroup";
        given(menuGroupService.list()).willReturn(Arrays.asList(메뉴묶음_응답_데이터_생성(1L, name)));

        //when //then
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value(name));
    }
}