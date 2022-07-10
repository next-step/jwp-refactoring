package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {
    private static Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));
    private static Product 새우버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));
    public static List<MenuProduct> 메뉴상품 = Arrays.asList(MenuProduct.of(불고기버거.getId(), 5L),
            MenuProduct.of(새우버거.getId(), 5L));
    public static Menu 햄버거메뉴 = new Menu(1L, "불고기버거", BigDecimal.valueOf(5_000), MenuGroupTest.햄버거_메뉴.getId());

    @Test
    @DisplayName("메뉴 생성")
    void create() {
        // then
        assertThat(햄버거메뉴).isInstanceOf(Menu.class);
    }

    @Test
    @DisplayName("메뉴 상품 추가")
    void addMenuProducts() {
        // when
        햄버거메뉴.addMenuProducts(메뉴상품);
        // then
        assertThat(햄버거메뉴.getMenuProducts()).hasSize(2);
    }

}
