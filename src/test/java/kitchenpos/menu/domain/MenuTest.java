package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.MenuPriceEmptyException;
import kitchenpos.menu.exception.MenuPriceExceedException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 테스트")
public class MenuTest {

    @DisplayName("메뉴의 가격은 필수 입력항목이다.")
    @Test
    void price_null() {
        assertThatThrownBy(() -> new Menu("메뉴명", null, null, new ArrayList<>()))
            .isInstanceOf(MenuPriceEmptyException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴상품들의 가격의합을 초과할 수 없다.")
    @Test
    void price_exceed() {
        // Given
        Price 치즈버거_가격 = Price.wonOf(4500);
        Price 감자튀김_가격 = Price.wonOf(2000);
        Price 콜라_가격 = Price.wonOf(1000);
        Product 치즈버거 = new Product(1L, "치즈버거", Price.wonOf(4500));
        Product 감자튀김 = new Product(2L, "감자튀김", Price.wonOf(2000));
        Product 콜라 = new Product(3L, "콜라", Price.wonOf(1000));
        MenuProduct 치즈버거세트_치즈버거 = new MenuProduct(치즈버거, 1L);
        MenuProduct 치즈버거세트_감자튀김 = new MenuProduct(감자튀김, 1L);
        MenuProduct 치즈버거세트_콜라 = new MenuProduct(콜라, 1L);
        MenuGroup 패스트푸드 = new MenuGroup(1L, "패스트푸드");
        Price 치즈버거상품들의_가격합 = 치즈버거_가격.plus(감자튀김_가격).plus(콜라_가격);
        Price 치즈버거상품들의_가격합을_초과한_가격 = 치즈버거상품들의_가격합.plus(Price.wonOf(1));

        // When & Then
        assertThatThrownBy(() -> new Menu("치즈버거세트", 치즈버거상품들의_가격합을_초과한_가격, 패스트푸드.getId(), Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라)))
            .isInstanceOf(MenuPriceExceedException.class);
    }
}
