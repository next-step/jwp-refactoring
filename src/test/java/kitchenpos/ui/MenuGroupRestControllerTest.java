package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.menuGroup.MenuGroupRequest;
import kitchenpos.ui.dto.menuGroup.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MenuGroupRestControllerTest {
    private MenuGroupRestController menuGroupRestController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setup() {
        menuGroupRestController = new MenuGroupRestController(menuGroupService);

        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController).build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroupTest() throws Exception {
        // given
        String url = "/api/menu-groups";
        String menuName = "테스트 메뉴 그룹";
        Long menuGroupId = 1L;

        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(menuName);
        MenuGroupResponse response = new MenuGroupResponse(menuGroupId, "menuName");

        given(menuGroupService.create(any())).willReturn(response);

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + menuGroupId))
        ;
    }

    @DisplayName("메뉴 그룹 목록을 요청할 수 있다.")
    @Test
    void getMenuGroupsTest() throws Exception {
        // given
        String url = "/api/menu-groups";

        MenuGroupResponse menuGroupResponse1 = new MenuGroupResponse(1L, "1");
        MenuGroupResponse menuGroupResponse2 = new MenuGroupResponse(2L, "2");
        given(menuGroupService.list()).willReturn(Arrays.asList(menuGroupResponse1, menuGroupResponse2));

        // when, then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
        ;
    }
}