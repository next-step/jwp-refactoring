package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("생성 테스트")
    @Test
    public void createTest() {
        MenuGroup menuGroup = new MenuGroup();
        Menu menu = new Menu("이름", BigDecimal.ONE, menuGroup);
        assertThat(menu.getName()).isEqualTo("이름");
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.ONE);
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 생성 불가능 케이스 - 메뉴의 가격이 상품의 가격의 합보다 큰 경우")
    @Test
    public void invalidCase1() {
        Menu menu = new Menu("name", BigDecimal.valueOf(1201), new ArrayList<MenuProduct>() {{
            add(new MenuProduct(new Product("상품1", BigDecimal.valueOf(600)), 1));
            add(new MenuProduct(new Product("상품2", BigDecimal.valueOf(200)), 2));
        }});
        assertThatThrownBy(menu::checkValidation).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 불가능 케이스 - 메뉴 자체의 가격이 정당하지 않은 경우")
    @Test
    public void invalidCase2() {
        MenuGroup menuGroup = new MenuGroup();
        assertThatThrownBy(() -> {
            new Menu("name", BigDecimal.valueOf(-1), menuGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
