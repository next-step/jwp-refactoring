package kitchenpos.menu.ui;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.ControllerTest;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuRestController;
import kitchenpos.menu.MenuService;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends ControllerTest {
    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴생성을 요청하면 생성된 메뉴를 응답")
    @Test
    public void returnMenu() throws Exception {
        Menu menu = getMenu();
        doReturn(menu).when(menuService).create(any(Menu.class));

        webMvc.perform(post("/api/menus")
                        .content(mapper.writeValueAsString(new Menu()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(menu.getId().intValue())))
                .andExpect(jsonPath("$.name", is(menu.getName())))
                .andExpect(jsonPath("$.price", is(menu.getPrice().intValue())))
                .andExpect(jsonPath("$.menuGroupId", is(menu.getMenuGroupId().intValue())))
                .andExpect(jsonPath("$.menuProducts", hasSize(menu.getMenuProducts().size())))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴생성을 요청하면 메뉴생성 실패응답")
    @Test
    public void throwsExceptionWhenMenuCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(menuService).create(any(Menu.class));

        webMvc.perform(post("/api/menus")
                        .content(mapper.writeValueAsString(new Menu()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴목록을 요청하면 메뉴목록을 응답")
    @Test
    public void returnMenus() throws Exception {
        List<Menu> menus = FixtureMonkey.create()
                .giveMeBuilder(Menu.class)
                .sampleList(Arbitraries.integers().between(1, 50).sample());
        doReturn(menus).when(menuService).list();

        webMvc.perform(get("/api/menus"))
                .andExpect(jsonPath("$", hasSize(menus.size())))
                .andExpect(status().isOk());
    }

    private Menu getMenu() {
        return FixtureMonkey.create()
                .giveMeBuilder(Menu.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("price", BigDecimal.valueOf(15000))
                .set("name", Arbitraries.strings().ofMinLength(5).ofMaxLength(15).sample())
                .set("menuGroupId", Arbitraries.longs().between(1, 50))
                .set("menuProducts", getMenuProducts())
                .sample();
    }

    private List<MenuProduct> getMenuProducts() {
        return FixtureMonkey.create()
                .giveMeBuilder(MenuProduct.class)
                .set("id", Arbitraries.longs().between(1, 20))
                .sampleList(10);
    }
}
