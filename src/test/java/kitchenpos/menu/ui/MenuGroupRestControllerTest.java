package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.MenuGroupRestController;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MenuGroupRestController ui 테스트")
@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends ControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;
    private MenuGroup 한마리치킨;
    private MenuGroup 두마리치킨;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        한마리치킨 = new MenuGroup(1L, "한마리치킨");
        두마리치킨 = new MenuGroup(2L, "두마리치킨");
    }

    @DisplayName("메뉴그룹 생성 api")
    @Test
    void createMenuGroup() throws Exception {
        given(menuGroupService.create(any(MenuGroup.class))).willReturn(한마리치킨);

        mockMvc.perform(post("/api/menu-groups")
                        .content(mapper.writeValueAsString(한마리치킨))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴그룹 조회 api")
    @Test
    void findMenuGroups() throws Exception {
        given(menuGroupService.list()).willReturn(Lists.newArrayList(한마리치킨, 두마리치킨));

        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk());
    }
}
