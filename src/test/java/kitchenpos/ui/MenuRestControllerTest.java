package kitchenpos.ui;

import kitchenpos.ApiTest;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ApiTest {

    @MockBean
    private MenuService menuService;
    private Menu menu;


    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(10);
        menuProduct1.setSeq(1L);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(2L);
        menu.setMenuProducts(Collections.singletonList(menuProduct1));
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void createTest() throws Exception {

        // given
        when(menuService.create(any())).thenReturn(menu);

        // then
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menu)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다")
    void listTest() throws Exception {

        // given
        when(menuService.list()).thenReturn(Collections.singletonList(menu));

        // then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("후라이드치킨")));
    }
}