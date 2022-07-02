package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.Exception.InvalidMenuPriceException;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuTest {
    private Menu menu;
    private Product product1;
    private Product product2;


    @BeforeEach
    void setUp() {
        menu = new Menu(MenuName.from("빅맥"), Price.from(5000), 1L);

        product1 = new Product(ProductName.from("토마토"), Price.from(5000));
        product2 = new Product(ProductName.from("양상추"), Price.from(1000));
    }

    @Test
    void 메뉴_상품_추가() {
        // given
        MenuProduct 토마토 = new MenuProduct(product1.getId(), Quantity.from(1));
        MenuProduct 양상추 = new MenuProduct(product2.getId(), Quantity.from(2));

        // when
        menu.addMenuProducts(Arrays.asList(토마토, 양상추));

        // then
        assertThat(
                menu.getMenuProducts().getMenuProducts()
                        .stream()
                        .map(MenuProduct::getProductId)
                        .collect(Collectors.toList())
        ).containsExactlyElementsOf(
                Arrays.asList(product1.getId(), product2.getId()));
    }
}
