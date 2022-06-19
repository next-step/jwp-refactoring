package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuGroupRestControllerTest extends BaseRestController {

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
        MenuGroup request = new MenuGroup(name);
        String requestBody = objectMapper.writeValueAsString(request);

        given(menuGroupService.create(any())).willReturn(new MenuGroup(1L, name));

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
    void list() throws Exception {
        //given
        String name = "menuGroup";
        given(menuGroupService.list()).willReturn(Arrays.asList(new MenuGroup(1L, name)));
        //when //then
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value(name));
    }
}