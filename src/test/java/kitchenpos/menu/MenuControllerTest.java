package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.ui.MenuController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MenuController(menuService)).build();
    }

    @DisplayName("메뉴를 생성하다.")
    @Test
    void createMenu() throws Exception {

        //given
        MenuProductRequest menuProduct = new MenuProductRequest();
        ReflectionTestUtils.setField(menuProduct, "productId", 1L);
        ReflectionTestUtils.setField(menuProduct, "quantity", 1);

        MenuRequest menuRequest = new MenuRequest();
        ReflectionTestUtils.setField(menuRequest, "name", "후라이드세트");
        ReflectionTestUtils.setField(menuRequest, "price", new BigDecimal("19000"));
        ReflectionTestUtils.setField(menuRequest, "products", Arrays.asList(menuProduct));

        when(menuService.create(any())).thenReturn(MenuResponse.of(menuRequest.toEntity()));

        //when
        ResultActions resultActions = post("/api/menus", menuRequest);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void getMenus() throws Exception {

        //given
        Menu 후라이드세트 = Menu.create("후라이드세트", new BigDecimal("24000"));
        MenuResponse menuResponse = MenuResponse.of(후라이드세트);

        when(menuService.list()).thenReturn(Arrays.asList(menuResponse));

        //when
        ResultActions resultActions = get("/api/menus", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['name']").value(후라이드세트.getName()));
        resultActions.andExpect(jsonPath("$[0]['price']").value(24000));
    }


}
