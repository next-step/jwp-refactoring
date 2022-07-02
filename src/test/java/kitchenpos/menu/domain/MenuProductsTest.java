package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {

    @Test
    void calculateTotalPrice() {
        // given
        List<MenuProduct> list = 두마리치킨_메뉴상품_리스트();
        MenuProducts 메뉴상품_리스트 = new MenuProducts(list);

        // when
        BigDecimal totalPrice = 메뉴상품_리스트.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(new BigDecimal(30000));
    }

    public static Product 후라이드치킨_상품() {
        return new Product("후라이드치킨", new BigDecimal(15000));
    }

    public static Product 양념치킨_상품() {
        return new Product("양념치킨", new BigDecimal(15000));
    }

    public static Product 불고기버거_상품() {
        return new Product("불고기버거", new BigDecimal(3000));
    }

    public static Product 치즈버거_상품() {
        return new Product("치즈버거", new BigDecimal(3500));
    }

    public static MenuProduct 후라이드치킨_메뉴상품() {
        return new MenuProduct(후라이드치킨_상품(), 1);
    }

    public static MenuProduct 양념치킨_메뉴상품() {
        return new MenuProduct(양념치킨_상품(), 1);
    }

    public static MenuProduct 불고기버거_메뉴상품() {
        return new MenuProduct(불고기버거_상품(), 1);
    }

    public static MenuProduct 치즈버거_메뉴상품() {
        return new MenuProduct(치즈버거_상품(), 1);
    }

    public static List<MenuProduct> 두마리치킨_메뉴상품_리스트() {
        return Arrays.asList(후라이드치킨_메뉴상품(), 양념치킨_메뉴상품());
    }

    public static List<MenuProduct> 불고기치즈버거_메뉴상품_리스트() {
        return Arrays.asList(불고기버거_메뉴상품(), 치즈버거_메뉴상품());
    }

    public static MenuGroup 패스트푸드_메뉴그룹() {
        return new MenuGroup(1L, "패스트푸드");
    }
}
