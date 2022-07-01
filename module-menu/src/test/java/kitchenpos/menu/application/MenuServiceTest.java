package kitchenpos.menu.application;


import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관련 기능")
public class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuValidator menuValidator;

    @InjectMocks
    MenuService menuService;

    private Product 강정치킨;
    private MenuGroup 치킨메뉴;
    private Menu 추천메뉴;

    @BeforeEach
    void setUp() {
        강정치킨 = 상품_등록(1L, "강정치킨", 17000);
        치킨메뉴 = 메뉴_그룹_등록(1L, "치킨메뉴");
        추천메뉴 = 메뉴_등록(1L, "추천메뉴", 강정치킨.getPriceIntValue(), 치킨메뉴.getId(),
                Arrays.asList(메뉴_상품_등록(1L, 강정치킨.getId(), 1L)));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        MenuRequest menuRequest = 메뉴_등록_요청("추천메뉴", 강정치킨.getPriceIntValue(), 치킨메뉴.getId(),
                Arrays.asList(메뉴_상품_등록_요청(강정치킨.getId(), 1L)));

        given(menuRepository.save(any())).willReturn(추천메뉴);

        // when
        MenuResponse createdMenu = menuService.create(menuRequest);

        // then
        assertThat(createdMenu).isNotNull();
    }

    @Test
    @DisplayName("메뉴등록 실패한다. - (상품 금액합이 메뉴가격보다 클때)")
    void createMenuFail() {
        // given
        MenuRequest menuRequest = 메뉴_등록_요청("추천메뉴", 1000, 치킨메뉴.getId(),
                Arrays.asList(메뉴_상품_등록_요청(강정치킨.getId(), 1L)));

        doThrow(IllegalArgumentException.class).when(menuValidator).validate(menuRequest);

        // when-then
        assertThatThrownBy(() -> menuService.create(menuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    public static Product 상품_등록(Long id, String name, Integer price) {
        return Product.of(name, price);
    }

    public static Menu 메뉴_등록(Long id, String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct 메뉴_상품_등록(Long seq, Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static MenuRequest 메뉴_등록_요청(String name, Integer price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return MenuRequest.of(name, price, menuGroupId, menuProducts);
    }

    public static MenuProductRequest 메뉴_상품_등록_요청(Long productId, long quantity) {
        return MenuProductRequest.of(productId, quantity);
    }
}
