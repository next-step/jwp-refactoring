package kitchenpos.menu.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.utils.domain.MenuObjects;
import kitchenpos.utils.domain.MenuProductObjects;
import kitchenpos.ui.MenuRestController;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("메뉴 관리 기능")
@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest extends MockMvcControllerTest {
    private static final String REQUEST_URL = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    private MenuObjects menuObjects;
    private MenuProductObjects menuProductObjects;

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @BeforeEach
    void setUp() {
        menuObjects = new MenuObjects();
        menuProductObjects = new MenuProductObjects();
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void retrieve_menuList() throws Exception {
        // given
        when(menuService.list()).thenReturn(menuObjects.getMenus());

        // then
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(menuObjects.getMenu1().getId()))
                .andExpect(jsonPath("[0].name").value(menuObjects.getMenu1().getName()))
                .andExpect(jsonPath("[0].price").value(menuObjects.getMenu1().getPrice()))
                .andExpect(jsonPath("[5].id").value(menuObjects.getMenu6().getId()))
                .andExpect(jsonPath("[5].name").value(menuObjects.getMenu6().getName()))
                .andExpect(jsonPath("[5].price").value(menuObjects.getMenu6().getPrice()))
        ;
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void save_menu() throws Exception {
        // given
        menuObjects.getMenu1().setMenuProducts(new ArrayList<>(Arrays.asList(menuProductObjects.getMenuProduct1(), menuProductObjects.getMenuProduct2())));
        menuProductObjects.getMenuProduct1().setMenuId(menuObjects.getMenu1().getId());
        menuProductObjects.getMenuProduct2().setMenuId(menuObjects.getMenu1().getId());

        when(menuService.create(any(Menu.class))).thenReturn(menuObjects.getMenu1());

        // then
        mockMvc.perform(post(REQUEST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(menuObjects.getMenu1())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(menuObjects.getMenu1().getId()))
                .andExpect(jsonPath("name").value(menuObjects.getMenu1().getName()))
                .andExpect(jsonPath("price").value(menuObjects.getMenu1().getPrice()))
                .andExpect(jsonPath("menuProducts.[1].seq").value(menuObjects.getMenu1().getMenuProducts().get(1).getSeq()))
        ;
    }
}
