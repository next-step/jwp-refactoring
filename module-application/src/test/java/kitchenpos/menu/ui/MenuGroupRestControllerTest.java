package kitchenpos.menu.ui;

import static kitchenpos.util.TestDataSet.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.ui.MenuGroupRestController;

@WebMvcTest(controllers = MenuGroupRestController.class)
public class MenuGroupRestControllerTest {

    public static final String BASE_URL = "/api/menu-groups";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("이름을 받아 이름 기반의 메뉴 그룹을 만들 수 있다.")
    void create() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(추천_메뉴_그륩);
        given(menuGroupService.create(any()))
            .willReturn(MenuGroupResponse.of(추천_메뉴_그륩));

        // when
        mockMvc.perform(
            post(BASE_URL)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(추천_메뉴_그륩.getName()));
    }

    @Test
    @DisplayName("등록한 메뉴 그룹 리스트를 출력 할 수 있다.")
    void list() throws Exception {
        // given
        given(menuGroupService.list())
            .willReturn(Arrays.asList(MenuGroupResponse.of(추천_메뉴_그륩), MenuGroupResponse.of(계절_메뉴_그룹)));

        // when
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(추천_메뉴_그륩.getName()))
            .andExpect(jsonPath("$[1].name").value(계절_메뉴_그룹.getName()));
    }

}
