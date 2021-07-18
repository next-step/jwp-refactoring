package kitchenpos.menu.event;

import kitchenpos.exception.MenuException;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 이벤트 핸들어 테스트")
@ExtendWith(MockitoExtension.class)
class MenuEventHandlerTest {

    @Mock
    private ProductService productService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuEventHandler menuEventHandler;

    private Product 짜장면;
    private Product 탕수육;
    private MenuProduct 짜장면_메뉴;
    private MenuProduct 탕수육_메뉴;

    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        짜장면 = new Product(1L, "짜장면", new BigDecimal(7000));
        탕수육 = new Product(2L, "탕수육", new BigDecimal(12000));
        짜장면_메뉴 = new MenuProduct(1L, 1);
        탕수육_메뉴 = new MenuProduct(2L, 1);

        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                new MenuProductRequest(1L, 1),
                new MenuProductRequest(2L, 1));
        menuRequest = new MenuRequest("짜장면 탕수육 메뉴", new BigDecimal(20000), 1L, menuProductRequests);
    }


    @Test
    void 존재하지않는_메뉴그룹_아이디_등록_요청_시_에러_발생() {
        when(menuGroupRepository.existsById(1L)).thenReturn(false);
        CreateMenuEvent createMenuEvent = new CreateMenuEvent(menuRequest);
        assertThatThrownBy(() -> menuEventHandler.createMenuValidEvent(createMenuEvent)).isInstanceOf(MenuException.class);
    }

    @Test
    void 조회한_메뉴_상품_갯수와_요청_메뉴_상품_의_갯수가_불일치할_경우_에러_발생() {
        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productService.findProductsByIds(Arrays.asList(1L, 2L))).thenReturn(new Products(Arrays.asList(짜장면)));
        CreateMenuEvent createMenuEvent = new CreateMenuEvent(menuRequest);
        assertThatThrownBy(() -> menuEventHandler.createMenuValidEvent(createMenuEvent)).isInstanceOf(MenuException.class);
    }

    @Test
    void 메뉴_가격이_메뉴의_금액_의_총합보다_큰_경우_에러_발생() {
        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productService.findProductsByIds(Arrays.asList(1L, 2L))).thenReturn(new Products(Arrays.asList(짜장면, 탕수육)));
        CreateMenuEvent createMenuEvent = new CreateMenuEvent(menuRequest);
        assertThatThrownBy(() -> menuEventHandler.createMenuValidEvent(createMenuEvent)).isInstanceOf(MenuException.class);
    }
}
