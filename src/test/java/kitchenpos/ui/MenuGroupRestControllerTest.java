package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupCreate;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static kitchenpos.ui.JsonUtil.toJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuGroupRestController.class)
@ExtendWith(MockitoExtension.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    void create() throws Exception {
        // given
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("Hello");
        MenuGroup menuGroup = new MenuGroup(1L, "Hello");

        given(menuGroupService.create(any(MenuGroupCreate.class)))
                .willReturn(menuGroup);

        // when
        mockMvc.perform(
                post("/api/menu-groups")
                        .content(toJson(menuGroupCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateMenuGroup("$", menuGroup));
    }

    @Test
    void list() throws Exception {
        // given
        MenuGroup menuGroup1 = new MenuGroup(1L, "Hello");
        MenuGroup menuGroup2 = new MenuGroup(2L, "Bello");

        given(menuGroupService.list())
                .willReturn(Arrays.asList(menuGroup1, menuGroup2));

        // when & then
        mockMvc.perform(
                get("/api/menu-groups")
        )
                .andExpect(status().isOk())
                .andExpect(validateMenuGroup("$[0]", menuGroup1))
                .andExpect(validateMenuGroup("$[1]", menuGroup2));
    }



    private ResultMatcher validateMenuGroup(String prefix, MenuGroup menuGroup) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(menuGroup.getId()),
                    jsonPath(prefix + ".name").value(menuGroup.getName())
            ).match(result);
        };
    }
}