package kitchenpos.menu.group;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.MenuGroupRestController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MenuGroupControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MenuGroupRestController(menuGroupService)).build();
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() throws Exception {

        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중화요리");
        when(menuGroupService.create(any())).thenReturn(menuGroup);

        //when
        ResultActions resultActions = post("/api/menu-groups", menuGroup);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void getMenuGroups() throws Exception {

        //given
        MenuGroup 중화요리 = new MenuGroup();
        중화요리.setName("중화요리");
        MenuGroup 일식 = new MenuGroup();
        일식.setName("일식");
        List<MenuGroup> 메뉴리스트 = Arrays.asList(중화요리, 일식);
        when(menuGroupService.list()).thenReturn(메뉴리스트);

        //when
        ResultActions resultActions = get("/api/menu-groups", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['name']").isString());
        resultActions.andExpect(jsonPath("$[1]['name']").isString());
    }
}
