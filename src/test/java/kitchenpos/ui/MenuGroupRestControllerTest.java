package kitchenpos.ui;

import kitchenpos.menuGroup.aplication.MenuGroupService;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.ui.MenuGroupRestController;
import kitchenpos.testFixture.MenuGroupTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 목록을 가져옴")
    @Test
    public void list() throws Exception {
        MenuGroup 치킨류 = MenuGroupTestFixture.메뉴그룹생성(1L,"치킨류");
        MenuGroup 피자류 = MenuGroupTestFixture.메뉴그룹생성(1L,"피자류");
        given(menuGroupService.list()).willReturn(Arrays.asList(치킨류, 피자류));

        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
