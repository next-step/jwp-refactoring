package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {

    @Test
    @DisplayName("MenuRequest 를 받아서 메뉴 인스턴스를 생성한다")
    void of() {
        // given
        Name name = new Name("라면메뉴");
        Price price = new Price(30_000);
        Long menuGroupId = 1L;
        Long productId = 1L;
        Quantity quantity = new Quantity(1L);
        Product product = new Product("진매", new Price(30_000));

        List<MenuProductRequest> menuProductRequests = Collections.singletonList(new MenuProductRequest(productId, quantity.value()));
        MenuRequest menuRequest = new MenuRequest(name.value(), price.value(), menuGroupId, menuProductRequests);
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(new MenuProduct(product, quantity.value())));

        //when
        Menu menu = Menu.of(menuRequest, menuProducts);

        //then
        assertAll(
                () -> assertThat(menu).isNotNull(),
                () -> assertThat(menu.getName()).isEqualTo(name),
                () -> assertThat(menu.getPrice()).isEqualTo(price),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroupId),
                () -> assertThat(menu.getMenuProducts()).isEqualTo(menuProducts)
        );
    }

}
