package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    private Product 하와이안피자;
    private MenuGroup 피자;
    private MenuProduct 하와이안피자상품;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product("하와이안피자", BigDecimal.valueOf(15_000));
        피자 = new MenuGroup("피자");
        하와이안피자상품 = new MenuProduct(하와이안피자, 1);
    }

    @DisplayName("메뉴 상품의 가격을 확인한다.")
    @Test
    void totalPrice() {
        Price result = 하와이안피자상품.totalPrice();

        assertThat(result.value()).isEqualTo(
            BigDecimal.valueOf(하와이안피자상품.getQuantity().value()).multiply(하와이안피자.getPrice().value()));
    }
}
