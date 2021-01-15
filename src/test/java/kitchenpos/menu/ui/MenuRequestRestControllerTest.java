package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/db/test_data.sql")
class MenuRequestRestControllerTest {
    private static final String DEFAULT_MENU_URI = "/api/menus";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() throws Exception {
        final List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 2));
        menuProductRequests.add(new MenuProductRequest(2L, 1));

        final MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setName("순살파닭두마리메뉴");

        final MenuGroup savedMenuGroup = menuGroupService.create(newMenuGroup);

        final MenuRequest menuRequest = new MenuRequest("후라이드치킨", 29_000L, savedMenuGroup.getId(), menuProductRequests);

        final String jsonTypeMenuGroup = objectMapper.writeValueAsString(menuRequest);

        mockMvc.perform(post(DEFAULT_MENU_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeMenuGroup))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(menuRequest.getName()));
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void 메뉴_조회() throws Exception {
        final List<MenuProductRequest> firstMenuProductRequests = new ArrayList<>();
        firstMenuProductRequests.add(new MenuProductRequest(1L, 2));
        firstMenuProductRequests.add(new MenuProductRequest(2L, 1));
        final MenuGroup firstMenuGroup = new MenuGroup();
        firstMenuGroup.setName("순살파닭두마리메뉴");
        final MenuGroup savedFirstMenuGroup = menuGroupService.create(firstMenuGroup);
        final MenuRequest firstMenuRequest = new MenuRequest("후라이드치킨", 29_000L, savedFirstMenuGroup.getId(), firstMenuProductRequests);
        menuService.create(firstMenuRequest);

        final List<MenuProductRequest> secondMenuProductRequests = new ArrayList<>();
        secondMenuProductRequests.add(new MenuProductRequest(3L, 1));
        secondMenuProductRequests.add(new MenuProductRequest(4L, 1));
        final MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setName("신메뉴");
        final MenuGroup savedSecondMenuGroup = menuGroupService.create(secondMenuGroup);
        final MenuRequest secondMenuRequest = new MenuRequest("간장치킨", 28_000L, savedSecondMenuGroup.getId(), secondMenuProductRequests);
        menuService.create(secondMenuRequest);

        mockMvc.perform(get(DEFAULT_MENU_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(8)))
            .andExpect(jsonPath("$[6].id").exists())
            .andExpect(jsonPath("$[6].name").value(firstMenuRequest.getName()))
            .andExpect(jsonPath("$[7].id").exists())
            .andExpect(jsonPath("$[7].name").value(secondMenuRequest.getName()));
    }
}
