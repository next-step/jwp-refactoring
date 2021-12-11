package kitchenpos.menu.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 서비스 테스트")
public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴을 등록한다.")
    void create() {
        // given
        Product savedProduct = 상품_저장();
        MenuGroup savedMenuGroup = 메뉴_그룹_저장();

        MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
        Menu menu = new Menu(
                savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isZero(),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(savedMenuGroup.getId()),
                () -> assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isNotNull(),
                () -> assertThat(savedMenu.getMenuProducts().get(0).getProductId()).isEqualTo(savedProduct.getId()),
                () -> assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(menuProduct.getQuantity())
        );
    }

    @Test
    @DisplayName("0원 이하의 가격으로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException1() {
        // given
        Product savedProduct = 상품_저장();
        MenuGroup savedMenuGroup = 메뉴_그룹_저장();

        MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
        Menu menu = new Menu(savedProduct.getName(), -1, savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 ID로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException2() {
        // given
        Product savedProduct = 상품_저장();

        MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
        Menu menu = new Menu(savedProduct.getName(), savedProduct.getPrice(), 0L, Collections.singletonList(menuProduct));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException3() {
        // given
        Product savedProduct = 상품_저장();
        MenuGroup savedMenuGroup = 메뉴_그룹_저장();

        MenuProduct menuProduct = new MenuProduct(0L, 1);
        Menu menu = new Menu(
                savedProduct.getName(), savedProduct.getPrice(), savedMenuGroup.getId(), Collections.singletonList(menuProduct));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("상품들의 합계 금액과 일치하지 않는 가격으로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException4() {
        // given
        Product savedProduct = 상품_저장();
        MenuGroup savedMenuGroup = 메뉴_그룹_저장();

        MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1);
        Menu menu = new Menu(
                savedProduct.getName(), savedProduct.getPrice().add(BigDecimal.ONE), savedMenuGroup.getId(),
                Collections.singletonList(menuProduct));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴의 목록을 조회한다.")
    void list() {
        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.size()).isPositive();
    }
}
