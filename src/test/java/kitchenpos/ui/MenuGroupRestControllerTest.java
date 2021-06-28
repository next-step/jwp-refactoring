package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
@DisplayName("MenuGroupRestController 클래스")
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @Nested
    @DisplayName("POST /api/menu-group 은")
    class Describe_create {

        @Nested
        @DisplayName("등록할 메뉴 그룹이 주어지면")
        class Context_with_menu_group {
            MenuGroup givenMenuGroup = new MenuGroup();

            @BeforeEach
            void setUp() {
                givenMenuGroup.setName("추천메뉴");

                MenuGroup savedMenuGroup = new MenuGroup();
                savedMenuGroup.setId(1L);
                savedMenuGroup.setName("추천메뉴");
                when(menuGroupService.create(any(MenuGroup.class)))
                        .thenReturn(savedMenuGroup);
            }

            @DisplayName("201 Created 와 메뉴 그룹을 응답한다.")
            @Test
            void It_responds_created_with_menu_group() throws Exception {
                mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(givenMenuGroup)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(jsonPath("name").exists());
            }
        }
    }

    @Nested
    @DisplayName("GET /api/menu-groups 는")
    class Describe_list {

        @Nested
        @DisplayName("등록된 메뉴 그룹 목록이 있으면")
        class Context_with_menu_groups {
            List<MenuGroup> menuGroups;

            @BeforeEach
            void setUp() {
                MenuGroup menuGroup = new MenuGroup();
                menuGroups = Arrays.asList(menuGroup);
                when(menuGroupService.list())
                        .thenReturn(menuGroups);
            }

            @DisplayName("200 OK 와 메뉴 그룹 목록을 응답한다.")
            @Test
            void it_responds_ok_with_menu_groups() throws Exception {
                mockMvc.perform(get("/api/menu-groups"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                                objectMapper.writeValueAsString(menuGroups)
                        ));
            }
        }
    }
}
