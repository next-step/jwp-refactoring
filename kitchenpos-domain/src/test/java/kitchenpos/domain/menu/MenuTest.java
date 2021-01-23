package kitchenpos.domain.menu;

import kitchenpos.domain.valueobject.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {
    private MenuGroup menuGroup;
    private Product product;
    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("두마리메뉴");
        product = new Product("후라이드", Price.of(16000));
        MenuProduct menuProduct = new MenuProduct(product, 2);
        menuProducts = new MenuProducts(Collections.singletonList(menuProduct));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    public void create() throws Exception {
        // when
        Menu 후라이드치킨세트 = new Menu("후라이드치킨세트", menuProducts.calculateSumPrice(), menuGroup, menuProducts);

        // then
        assertThat(후라이드치킨세트).isNotNull();
    }

    @Test
    @DisplayName("1 개 이상의 등록된 상품이 없으면 생성할 수 없다.")
    public void createFail() throws Exception {
        // given
        MenuProducts emptyMenuProducts = new MenuProducts();
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Menu("후라이드치킨세트", Price.of(0), menuGroup, emptyMenuProducts))
                .withMessageMatching("등록된 상품으로만 메뉴를 등록할 수 있습니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴에 속한 상품 금액의 합보다 클 수 없다.")
    public void createFail2() throws Exception {
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Menu("후라이드치킨세트", menuProducts.calculateSumPrice().add(Price.of(1)), menuGroup, menuProducts))
                .withMessageMatching("메뉴의 가격이 메뉴에 속한 상품 금액의 합보다 클 수 없습니다.");
    }
}
