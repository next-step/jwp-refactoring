package kitchenpos.menugroup.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.common.CommonTestFixtures;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import java.util.Arrays;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    private static final String BASE_PATH = "/api/menu-groups";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService service;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() throws Exception {
        //given
        String menuGroupName = "추천메뉴";
        MenuGroupRequest requestMenuGroup = new MenuGroupRequest(menuGroupName);
        MenuGroupResponse expectedMenuGroup = MenuGroupResponse.from(
            new MenuGroup(1L, menuGroupName));
        given(service.create(any(MenuGroupRequest.class)))
            .willReturn(expectedMenuGroup);

        //when, then
        mockMvc.perform(post(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonTestFixtures.asJsonString(requestMenuGroup)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(expectedMenuGroup.getId()))
            .andExpect(jsonPath("$.name").value(expectedMenuGroup.getName()));
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void list() throws Exception {
        //given

        List<MenuGroupResponse> expectedMenuGroups = MenuGroupResponse.fromList(Arrays.asList(
            new MenuGroup(1L, "추천메뉴"),
            new MenuGroup(2L, "베스트메뉴")));
        given(service.list())
            .willReturn(expectedMenuGroups);

        //when, then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*]['id']",
                Matchers.containsInAnyOrder(
                    expectedMenuGroups.stream()
                        .mapToInt(menuGroup -> menuGroup.getId().intValue()).boxed()
                        .toArray(Integer[]::new))));
    }
}
