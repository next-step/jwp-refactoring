package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
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
        MenuGroup menuGroupRequest = new MenuGroup();
        MenuGroup saved = new MenuGroup();
        saved.setId(1L);

        given(menuGroupService.create(any())).willReturn(saved);

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + saved.getId()))
        ;
    }

    @DisplayName("메뉴 그룹 목록을 요청할 수 있다.")
    @Test
    void getMenuGroupsTest() throws Exception {
        // given
        String url = "/api/menu-groups";

        given(menuGroupService.list()).willReturn(Arrays.asList(new MenuGroup(), new MenuGroup()));

        // when, then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
        ;
    }
}