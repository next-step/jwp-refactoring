package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuFixture.*;
import static kitchenpos.menu.domain.MenuProductFixture.*;
import static kitchenpos.product.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 유효성 검사")
@SpringBootTest
public class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @DisplayName("메뉴 유효성 검사 - 가격 없음")
    @Test
    void validate_price_null() {
        // given
        MenuProduct menuProduct = savedMenuProduct(1L, 1L, 2L);
        Menu menu = savedMenu(1L, "후라이드+후라이드", null, 1L, Collections.singletonList(menuProduct));
        List<Product> products = Collections.singletonList(savedProduct(1L, "후라이드", BigDecimal.valueOf(10000)));
        boolean menuGroupNotExists = false;

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu, products, menuGroupNotExists))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 가격 0원")
    @Test
    void validate_price_zero() {
        // given
        MenuProduct menuProduct = savedMenuProduct(1L, 1L, 2L);
        Menu menu = savedMenu(1L, "후라이드+후라이드", BigDecimal.ZERO, 1L, Collections.singletonList(menuProduct));
        List<Product> products = Arrays.asList(savedProduct(1L, "후라이드", BigDecimal.valueOf(10000)));
        boolean menuGroupNotExists = false;

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu, products, menuGroupNotExists))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 메뉴 그룹 존재 하지 않음")
    @Test
    void validate_menu_group_not_exists() {
        // given
        MenuProduct menuProduct = savedMenuProduct(1L, 1L, 2L);
        Menu menu = savedMenu(1L, "후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));
        List<Product> products = Arrays.asList(savedProduct(1L, "후라이드", BigDecimal.valueOf(10000)));
        boolean menuGroupNotExists = true;

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu, products, menuGroupNotExists))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 메뉴 상품 존재 하지 않음")
    @Test
    void validate_product_not_exists() {
        // given
        MenuProduct menuProduct = savedMenuProduct(1L, 1L, 2L);
        Menu menu = savedMenu(1L, "후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProduct));
        List<Product> products = Collections.emptyList();
        boolean menuGroupNotExists = false;

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu, products, menuGroupNotExists))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 메뉴 상품 가격, 상품 가격 합계 초과")
    @Test
    void validate_product_price_invalid() {
        // given
        MenuProduct menuProduct = savedMenuProduct(1L, 1L, 2L);
        Menu menu = savedMenu(1L, "후라이드+후라이드", new BigDecimal(22000), 1L, Collections.singletonList(menuProduct));
        List<Product> products = Collections.singletonList(savedProduct(1L, "후라이드", BigDecimal.valueOf(10000)));
        boolean menuGroupNotExists = false;

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menu, products, menuGroupNotExists))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
