package kitchenpos.menu.ui;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.ControllerTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.ui.MenuGroupRestController;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends ControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹생성을 요청하면 생성된 메뉴그룹를 응답")
    @Test
    public void returnMenu() throws Exception {
        MenuGroupResponse menuGroup = getMenuGroupBuilder().sample();
        doReturn(menuGroup).when(menuGroupService).create(any(MenuGroupRequest.class));

        webMvc.perform(post("/api/menu-groups")
                        .content(mapper.writeValueAsString(new MenuGroupRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(menuGroup.getId().intValue())))
                .andExpect(jsonPath("$.name", is(menuGroup.getName())))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴그룹목록을 요청하면 메뉴그룹목록을 응답")
    @Test
    public void returnMenus() throws Exception {
        List<MenuGroupResponse> menuGroups = getMenuGroupBuilder()
                .sampleList(Arbitraries.integers().between(1, 50).sample());
        doReturn(menuGroups).when(menuGroupService).list();

        webMvc.perform(get("/api/menu-groups"))
                .andExpect(jsonPath("$", hasSize(menuGroups.size())))
                .andExpect(status().isOk());
    }

    private ArbitraryBuilder<MenuGroupResponse> getMenuGroupBuilder() {
        return FixtureMonkey.builder()
                .build()
                .giveMeBuilder(MenuGroupResponse.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("name", Arbitraries.strings().ofMinLength(5).ofMaxLength(15).sample());
    }
}
