package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupCreate;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static kitchenpos.fixture.MenuGroupFixture.그룹1;
import static kitchenpos.fixture.MenuGroupFixture.그룹2;
import static kitchenpos.ui.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuGroupRestController.class)
@ExtendWith(MockitoExtension.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUpOrderFirst();
    }

    @Test
    void create() throws Exception {
        // given
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("Hello");

        given(menuGroupService.create(any(MenuGroupCreate.class)))
                .willReturn(그룹1);

        // when
        mockMvc.perform(
                post("/api/menu-groups")
                        .content(toJson(menuGroupCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateMenuGroup("$", 그룹1));
    }

    @Test
    void list() throws Exception {
        // given
        given(menuGroupService.list())
                .willReturn(Arrays.asList(그룹1, 그룹2));

        // when & then
        mockMvc.perform(
                get("/api/menu-groups")
        )
                .andExpect(status().isOk())
                .andExpect(validateMenuGroup("$[0]", 그룹1))
                .andExpect(validateMenuGroup("$[1]", 그룹2));
    }



    private ResultMatcher validateMenuGroup(String prefix, MenuGroup menuGroup) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(menuGroup.getId()),
                    jsonPath(prefix + ".name").value(menuGroup.getName().toString())
            ).match(result);
        };
    }
}