package kitchenpos.Menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.Menu.domain.MenuGroupTest.메뉴_그룹_생성;
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
        menuGroup =메뉴_그룹_생성(1L, "뿌링클시리즈");

        메뉴_상품_목록_생성();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        Menu menu = 메뉴_생성(1L, new Name("뿌링클치즈볼"), new Price(new BigDecimal(18000)), menuGroup, menuProducts);
        메뉴_생성됨(menu, "뿌링클치즈볼", new BigDecimal(18000));
    }

    @DisplayName("메뉴 가격 0원 이하일 경우 예외 발생한다.")
    @Test
    void createMenu_가격_예외() {
        BigDecimal price = new BigDecimal(-1);
        가격_0원_이하일경우_예외_발생함(price);
    }

    @DisplayName("메뉴 가격보다 메뉴에 속한 상품 가격의 합이 작으면 예외 발생한다.")
    @Test
    void createMenu_메뉴상품_가격합_예외() {
        BigDecimal price = new BigDecimal(30000);
        메뉴_가격이_비쌀경우_예외_발생(price);
    }

    private void 메뉴_가격이_비쌀경우_예외_발생(BigDecimal price) {
        assertThatThrownBy(() -> {
            메뉴_생성(1L, new Name("뿌링클치즈볼"), new Price(price), menuGroup, menuProducts);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴가격은 메뉴에 등록된 상품 가격의 합보다 작거나 같아야합니다.");
    }

    public static Menu 메뉴_생성(long id, Name name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    private void 메뉴_상품_목록_생성() {
        뿌링클 = new Product(new Name("뿌링클"), new Price(new BigDecimal("18000")));
        치즈볼 = new Product(new Name("치즈볼"), new Price(new BigDecimal("5000")));
        menuProducts.add(new MenuProduct(1L, 뿌링클, 1));
        menuProducts.add(new MenuProduct(2L, 치즈볼, 1));
    }

    private void 메뉴_생성됨(Menu createMenu, String menuName, BigDecimal price) {
        assertThat(createMenu.getName()).isEqualTo(menuName);
        assertThat(createMenu.getPrice()).isEqualTo(price);
        assertThat(createMenu.getMenuGroup().getId()).isEqualTo(menuGroup.getId());
        assertThat(createMenu.getMenuProducts().get(0).getProduct().getName()).isEqualTo(뿌링클.getName());
        assertThat(createMenu.getMenuProducts().get(1).getProduct().getName()).isEqualTo(치즈볼.getName());
    }

    private void 가격_0원_이하일경우_예외_발생함(BigDecimal price) {
        assertThatThrownBy(() -> {
            메뉴_생성(1L, new Name("뿌링클치즈볼"), new Price(price), menuGroup, menuProducts);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0원 이상이어야 합니다.");
    }
}