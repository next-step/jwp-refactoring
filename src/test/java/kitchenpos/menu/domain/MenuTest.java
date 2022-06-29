package kitchenpos.menu.domain;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuTest {
    @DisplayName("메뉴를 생성한다")
    @Test
    void Menu_생성() {
        MenuGroup menuGroup_한식 = new MenuGroup(1L, "한식");
        Product product_김치찌개 = new Product(1L, "김치찌개", 8000);
        Menu menu = new Menu(1L, "김치찌개", 8000, menuGroup_한식);
        MenuProduct menuProduct_김치찌개 = new MenuProduct(1L, menu, product_김치찌개, 1);
        menu.setMenuProducts(Arrays.asList(menuProduct_김치찌개));

        Assertions.assertThat(menu.getName()).isEqualTo("김치찌개");
    }

    @DisplayName("메뉴의 가격은 0 이상이다")
    @Test
    void Menu_가격_0이상_검증(){
        MenuGroup menuGroup_한식 = new MenuGroup(1L, "한식");

        assertThrows(IllegalArgumentException.class,
                () -> new Menu(1L, "김치찌개", -8000, menuGroup_한식));
    }

    @DisplayName("메뉴그룹이 존재해야 한다")
    @Test
    void Menu_MenuGroup_검증(){
        assertThrows(IllegalArgumentException.class,
                () -> new Menu(1L, "김치찌개", -8000, null));
    }

    @DisplayName("메뉴의 가격은, 메뉴상품의 정가의 합보다 클 수 없다")
    @Test
    void Menu_가격_정가이하_검증(){
        MenuGroup menuGroup_한식 = new MenuGroup(1L, "한식");
        Product product_김치찌개 = new Product(1L, "김치찌개", 8000);
        Menu menu = new Menu(1L, "김치찌개", 10000, menuGroup_한식);
        MenuProduct menuProduct_김치찌개 = new MenuProduct(1L, menu, product_김치찌개, 1);

        assertThrows(IllegalArgumentException.class,
                () -> menu.setMenuProducts(Arrays.asList(menuProduct_김치찌개)));
    }
}