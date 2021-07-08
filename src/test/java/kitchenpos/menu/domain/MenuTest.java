package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MenuTest {

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    public void setup() {
        menuGroup = new MenuGroup("새로운 메뉴");
        menuProducts = new ArrayList<>();
    }

    @Test
    @DisplayName("메뉴를 생성 한다")
    public void createMenu() {
        //given
        String name = "후라이드치킨";
        BigDecimal price = new BigDecimal(16000);

        //when
        Menu createMenu = new Menu(name, price, menuGroup);

        //then
        assertThat(createMenu).isEqualTo(new Menu(name, price, menuGroup));
    }


    @Test
    @DisplayName("메뉴 생성 실패 - 가격이 음수")
    public void createMenuFailByPriceMinus() {
        //given
        String name = "불고기피자";
        BigDecimal price = new BigDecimal(-10000);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new Menu(name, price, menuGroup));
    }

    @Test
    @DisplayName("메뉴 생성 실패 - 메뉴 상품 가격의 합과 메뉴 가격이 맞지 않음")
    public void createMenuFailByPriceMissMatch() {
        //given
        String name = "불고기피자";
        BigDecimal price = new BigDecimal(20000);
        Menu menu = new Menu(name, price, menuGroup);
        menuProducts.add(new MenuProduct(menu, new Product(name, new BigDecimal(15000)), 1L));
        menu.mappingProducts(new MenuProducts(menuProducts));

        //when
        //then
        assertThrows(IllegalArgumentException.class, menu::validateMenuProductsPrice);
    }
}
