package kitchenpos.menu.application;

import kitchenpos.common.ControllerTest;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MenuRestController.class)
public class MenuControllerTest extends ControllerTest<MenuRequest> {

    private static final String BASE_URI = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    private Product 후라이드 = new Product("후라이드", Price.valueOf(15000));
    private Product 콜라 = new Product("콜라", Price.valueOf(2000));
    private MenuGroup 인기메뉴 = new MenuGroup("인기메뉴");
    private MenuProduct 후라이드_한마리 = new MenuProduct(후라이드, Quantity.of(1L));
    private MenuProduct 콜라_한개 = new MenuProduct(콜라, Quantity.of(1L));
    private List<MenuProduct> 메뉴상품목록 = Arrays.asList(후라이드_한마리, 콜라_한개);
    private Menu 후라이드세트 = new Menu(1L, "후라이드세트", Price.valueOf(15000),
            인기메뉴, 메뉴상품목록);

    private MenuResponse 메뉴_첫번째_응답 = new MenuResponse(1L, 후라이드세트.getName(), 후라이드세트.getPrice().value(),
            후라이드세트.getMenuGroup().getId(), new ArrayList<>());
    private MenuResponse 메뉴_두번째_응답 = new MenuResponse(2L, 후라이드세트.getName(), 후라이드세트.getPrice().value(),
            후라이드세트.getMenuGroup().getId(), new ArrayList<>());

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @DisplayName("메뉴 생성요청")
    @Test
    void 메뉴_생성요청() throws Exception {
        //Given
        when(menuService.create(any())).thenReturn(메뉴_첫번째_응답);

        //When
        ResultActions 결과 = postRequest(BASE_URI, MenuRequest.of(후라이드세트));

        //Then
        생성성공(결과);
    }

    @DisplayName("메뉴 목록 조회요청")
    @Test
    void 메뉴_목록_조회요청() throws Exception {
        //Given
        when(menuService.list()).thenReturn(Arrays.asList(메뉴_첫번째_응답, 메뉴_두번째_응답));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }
}
