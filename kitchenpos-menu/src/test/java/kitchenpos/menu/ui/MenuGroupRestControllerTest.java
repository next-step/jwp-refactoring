package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class MenuGroupRestControllerTest extends ControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹생성을 요청하면 생성된 메뉴그룹를 응답")
    @Test
    public void returnMenu() throws Exception {
        MenuGroupResponse menuGroup = MenuGroupResponse.of(MenuGroup.builder()
                .id(2l)
                .name("group1")
                .build());
        doReturn(menuGroup).when(menuGroupService).create(any(MenuGroupRequest.class));

        webMvc.perform(post("/api/menu-groups")
                        .content(mapper.writeValueAsString(new MenuGroupRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(menuGroup.getId().intValue())))
                .andExpect(jsonPath("$.name", is(menuGroup.getName())))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴그룹목록을 요청하면 메뉴그룹목록을 응답")
    @Test
    public void returnMenus() throws Exception {
        MenuGroup menuGroup1 = MenuGroup.builder()
                .id(2l)
                .name("group1")
                .build();
        MenuGroup menuGroup2 = MenuGroup.builder()
                .id(3l)
                .name("group2")
                .build();
        List<MenuGroupResponse> menuGroups = Arrays.asList(MenuGroupResponse.of(menuGroup1), MenuGroupResponse.of(menuGroup2));
        doReturn(menuGroups).when(menuGroupService).list();

        webMvc.perform(get("/api/menu-groups"))
                .andExpect(jsonPath("$", hasSize(menuGroups.size())))
                .andExpect(status().isOk());
    }
}
