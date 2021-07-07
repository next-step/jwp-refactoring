package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.ui.MenuGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MenuGroupService menuGroupService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("메뉴그룹 생성 Api 테스트")
    @Test
    void create() throws Exception {
        MenuGroupRequest menuGroup = new MenuGroupRequest("패스트푸드");

        String requestBody = objectMapper.writeValueAsString(menuGroup);

        MenuGroupResponse responseMenuGroup = MenuGroupResponse.of(new MenuGroup("패스트푸드"));

        String responseBody = objectMapper.writeValueAsString(responseMenuGroup);

        when(menuGroupService.create(any())).thenReturn(responseMenuGroup);
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody))
        ;
    }

    @DisplayName("메뉴그룹 목록 Api 테스트")
    @Test
    void list() throws Exception {
        MenuGroupResponse responseMenuGroup = MenuGroupResponse.of(new MenuGroup("패스트푸드"));

        List<MenuGroupResponse> menuGroups = Arrays.asList(responseMenuGroup);

        String responseBody = objectMapper.writeValueAsString(menuGroups);

        when(menuGroupService.list()).thenReturn(menuGroups);
        mockMvc.perform(get("/api/menu-groups")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody))
        ;
    }

}
