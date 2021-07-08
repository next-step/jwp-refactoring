package kitchenpos.menugroup.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.MenuGroupRestController;
import kitchenpos.utils.MockMvcControllerTest;
import kitchenpos.utils.domain.MenuGroupObjects;

@DisplayName("메뉴그룹 관리 기능")
@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest extends MockMvcControllerTest {

    public static final String DEFAULT_REQUEST_URL = "/api/menu-groups";
    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRestController menuGroupRestController;
    private MenuGroupObjects menuGroupObjects;

    @Override
    protected Object controller() {
        return this.menuGroupRestController;
    }

    @BeforeEach
    void setUp() {
        menuGroupObjects = new MenuGroupObjects();
    }

    @Test
    @DisplayName("메뉴그룹을 등록할 수 있다.")
    void save_menuGroup() throws Exception {
        // given
        when(menuGroupService.create(any(MenuGroup.class))).thenReturn(menuGroupObjects.getMenuGroup1());

        // then
        mockMvc.perform(post(DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(menuGroupObjects.getMenuGroup2())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(menuGroupObjects.getMenuGroup1().getId()))
                .andExpect(jsonPath("name").value(menuGroupObjects.getMenuGroup1().getName()))
        ;
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회할 수 있다.")
    void retrieve_menuGroupList() throws Exception {
        // given
        when(menuGroupService.list()).thenReturn(menuGroupObjects.getMenuGroups());

        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(menuGroupObjects.getMenuGroup1().getId()))
                .andExpect(jsonPath("[0].name").value(menuGroupObjects.getMenuGroup1().getName()))
                .andExpect(jsonPath("[3].id").value(menuGroupObjects.getMenuGroup4().getId()))
                .andExpect(jsonPath("[3].name").value(menuGroupObjects.getMenuGroup4().getName()))
        ;
    }
}
