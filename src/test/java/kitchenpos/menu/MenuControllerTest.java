package kitchenpos.menu;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.jayway.jsonpath.JsonPath;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.MenuRestController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MenuRestController(menuService)).build();
    }

    @DisplayName("메뉴를 생성하다.")
    @Test
    void createMenu() throws Exception {

        //given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal("19000"));
        menu.setMenuProducts(Arrays.asList(menuProduct));

        when(menuService.create(any())).thenReturn(menu);

        //when
        ResultActions resultActions = post("/api/menus", menu);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void getMenus() throws Exception {

        //given
        Menu 후라이드_후라이드 = new Menu();
        MenuProduct 후라이드 = new MenuProduct();
        후라이드.setQuantity(2);
        후라이드.setProductId(1L);
        후라이드_후라이드.setMenuProducts(Arrays.asList(후라이드));
        후라이드_후라이드.setPrice(new BigDecimal("19000"));
        후라이드_후라이드.setName("후라이드_후라이드");
        when(menuService.list()).thenReturn(Arrays.asList(후라이드_후라이드));

        //when
        ResultActions resultActions = get("/api/menus", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['name']").value(후라이드_후라이드.getName()));
        resultActions.andExpect(jsonPath("$[0]['price']").value(후라이드_후라이드.getPrice()));
    }


}
