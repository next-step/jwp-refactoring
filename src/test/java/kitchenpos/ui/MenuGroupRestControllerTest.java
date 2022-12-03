package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuGroupRestController menuGroupRestController;

    @Nested
    @DisplayName("POST /api/menu-groups 테스트")
    public class PostMethod {
        @Test
        @DisplayName("성공적으로 메뉴그룹을 등록하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final MenuGroup mockMenuGroup = setupSuccess("test menu group");

            // when
            MockHttpServletResponse response = mockMvc.perform(post("/api/menu-groups")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockMenuGroup))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final MenuGroup menuGroupResponse =
                    objectMapper.readValue(response.getContentAsString(), MenuGroup.class);
            assertThat(menuGroupResponse.getName()).isEqualTo(mockMenuGroup.getName());
        }

        private MenuGroup setupSuccess(String name) {
            final MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName(name);
            Mockito.when(menuGroupRestController.create(Mockito.any())).thenReturn(ResponseEntity.ok(menuGroup));
            return menuGroup;
        }
    }

    @Nested
    @DisplayName("GET /api/menu-groups 테스트")
    public class GetList {
        @Test
        @DisplayName("메뉴그룹 목록을 조회하는 데 성공하면 200 상태 코드를 응답받는다")
        public void success() throws Exception {
            // given
            final List<MenuGroup> mockMenuGroups = setupSuccess();

            // when
            MockHttpServletResponse response = mockMvc.perform(get("/api/menu-groups")
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

            // then
            final List<MenuGroup> menuGroupsResponse =
                    objectMapper.readValue(response.getContentAsString(), new TypeReference<List<MenuGroup>>() {
                    });
            assertThat(menuGroupsResponse.size()).isEqualTo(mockMenuGroups.size());
        }

        private List<MenuGroup> setupSuccess() {
            final List<MenuGroup> mockMenuGroups = Arrays.asList(new MenuGroup(), new MenuGroup(), new MenuGroup());
            Mockito.when(menuGroupRestController.list()).thenReturn(ResponseEntity.ok(mockMenuGroups));
            return mockMenuGroups;
        }
    }
}
