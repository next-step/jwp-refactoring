package kitchenpos.menugroup.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("메뉴그룹 관리 기능")
@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest extends MockMvcControllerTest {
    public static final String DEFAULT_REQUEST_URL = "/api/menu-groups";
    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @Override
    protected Object controller() {
        return this.menuGroupRestController;
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("메뉴그룹을 등록할 수 있다.")
    void save_menuGroup() throws Exception {
        // given
        MenuGroupResponse menuGroupResponse = MenuGroupResponse.of(1L, "A");
        given(menuGroupService.create(any(MenuGroupRequest.class))).willReturn(menuGroupResponse);

        // then
        mockMvc.perform(post( DEFAULT_REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(new MenuGroupRequest("A"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value(menuGroupResponse.getName()))
        ;
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회할 수 있다.")
    void retrieve_menuGroupList() throws Exception {
        // given
        List<MenuGroupResponse> menuGroupResponses = Arrays.asList(MenuGroupResponse.of(1L, "A"), MenuGroupResponse.of(2L, "B"));
        given(menuGroupService.findAllMenuGroups()).willReturn(menuGroupResponses);

        // then
        mockMvc.perform(get(DEFAULT_REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(menuGroupResponses.get(0).getId()))
                .andExpect(jsonPath("[0].name").value(menuGroupResponses.get(0).getName()))
                .andExpect(jsonPath("[1].id").value(menuGroupResponses.get(1).getId()))
                .andExpect(jsonPath("[1].name").value(menuGroupResponses.get(1).getName()))
        ;
    }
}
