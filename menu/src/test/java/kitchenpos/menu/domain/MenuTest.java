package kitchenpos.menu.domain;

import kitchenpos.menu.application.MenuGroupServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 관련 기능 테스트")
class MenuTest {
    private MenuGroup menuGroup;
    private Product 뿌링클;
    private Product 치즈볼;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupServiceTest.메뉴_그룹_생성(1L, "뿌링클시리즈");

        메뉴_상품_목록_생성();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        Menu menu = 메뉴_생성(1L, "뿌링클치즈볼", new BigDecimal(18000), menuGroup);
        메뉴_생성됨(menu, "뿌링클치즈볼", new BigDecimal(18000));
    }

    @DisplayName("메뉴 가격 0원 이하일 경우 예외 발생한다.")
    @Test
    void createMenu_가격_예외() {
        BigDecimal price = new BigDecimal(-1);
        가격_0원_이하일경우_예외_발생함(price);
    }

    public static Menu 메뉴_생성(long id, String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }

    private void 메뉴_상품_목록_생성() {
        뿌링클 = new Product("뿌링클", new BigDecimal("18000"));
        치즈볼 = new Product("치즈볼", new BigDecimal("5000"));
        menuProducts.add(new MenuProduct(1L, 뿌링클.getId(), 1));
        menuProducts.add(new MenuProduct(2L, 치즈볼.getId(), 1));
    }

    private void 메뉴_생성됨(Menu createMenu, String menuName, BigDecimal price) {
        assertThat(createMenu.getName()).isEqualTo(menuName);
        assertThat(createMenu.getPrice()).isEqualTo(price);
        assertThat(createMenu.getMenuGroup().getId()).isEqualTo(menuGroup.getId());
    }

    private void 가격_0원_이하일경우_예외_발생함(BigDecimal price) {
        assertThatThrownBy(() -> {
            메뉴_생성(1L, "뿌링클치즈볼", price, menuGroup);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 합니다.");
    }
}