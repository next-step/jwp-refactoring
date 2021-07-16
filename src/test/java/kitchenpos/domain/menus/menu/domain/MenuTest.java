package kitchenpos.domain.menus.menu.domain;

import kitchenpos.domain.menus.Price;
import kitchenpos.domain.menus.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import static kitchenpos.domain.menus.product.domain.ProductTest.상품_생성;
import static kitchenpos.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴")
public class MenuTest {

    @Test
    @DisplayName("메뉴를 생성한다.")
    public void initMenu() {
        // given
        Long menuGroupId = getRandomId();

        // when
        Menu menu = 치킨세트_메뉴_생성(menuGroupId);

        // then
        assertThat(menu).isNotNull();
        assertThat(menu.getProductIds()).isEmpty();
    }

    @Test
    @DisplayName("메뉴상품을 생성한다. 메뉴상품의 가격은 수량의 곱이다.")
    public void initMenuProduct() {
        // given
        Menu menu = 치킨세트_메뉴_생성(getRandomId());
        Product product = 상품_생성("치킨", 10_000);

        // when
        MenuProduct menuProduct = new MenuProduct(getRandomId(), menu, product.getId(), 1);

        // then
        assertThat(menuProduct).isNotNull();
        assertThat(menuProduct.calculate(Price.THOUSAND)).isEqualTo(Price.THOUSAND);
    }

    @Test
    @DisplayName("메뉴상품을 등록한다. 등록시 메뉴에는 메뉴상품이 들어간다.")
    public void initMenuProducts() {
        // given
        Menu menu = 치킨세트_메뉴_생성(getRandomId());
        Long productId1 = 상품_생성("치킨", 10_000).getId();
        Long productId2 = 상품_생성("콜라", 1000).getId();

        // when
        MenuProduct menuProduct1 = new MenuProduct(getRandomId(), menu, productId1, 1);
        MenuProduct menuProduct2 = new MenuProduct(getRandomId(), menu, productId2, 1);

        // then
        assertThat(menuProduct1).isNotNull();
        assertThat(menuProduct2).isNotNull();
        assertThat(menu.getProductIds().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메뉴의 가격은 `[메뉴의 수량] X [상품의 가격]` 보다 비쌀 수 없다.")
    public void exceptionMenuPrice() {
        // given
        Menu menu = 치킨세트_메뉴_생성(getRandomId());

        // when
        Product 치킨 = 상품_생성("치킨", 6900);
        Product 콜라 = 상품_생성("콜라", 1000);
        new MenuProduct(getRandomId(), menu, 치킨.getId(), 1);
        new MenuProduct(getRandomId(), menu, 콜라.getId(), 1);

        // then
        assertThatThrownBy(() -> menu.validationByPrice(getProductPriceMap(치킨, 콜라)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 `[메뉴의 수량] X [상품의 가격]` 보다 비쌀 수 없다.");
    }


    private static Menu 치킨세트_메뉴_생성(final Long menuGroupId) {
        return new Menu(getRandomId(), "치킨세트", Price.TEN_THOUSAND.getPrice(), menuGroupId);
    }

    private Map<Long, Price> getProductPriceMap(Product... products) {
        return Arrays.asList(products)
            .stream()
            .collect(Collectors.toMap(x -> x.getId(), x -> x.getPrice()));
    }
}
