package kitchenpos.domain.menu;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.menugroup.domain.MenuGroup;
import kitchenpos.domain.menugroup.domain.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
class MenuTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;

    private Long 추천메뉴_아이디;
    private Long 짬뽕_아이디;

    @BeforeEach
    void setUp() {
        추천메뉴_아이디 = menuGroupRepository.save(MenuGroup.of("추천메뉴")).getId();
        짬뽕_아이디 = productRepository.save(Product.of("짬뽕", 3000)).getId();
    }

    @DisplayName("메뉴는 이름, 가격, 메뉴 그룹의 아이디, 메뉴상품그룹으로 구성되어 있다.")
    @Test
    void create() {
        // given
        final List<MenuProduct> menuProducts = Collections.singletonList(MenuProduct.of(짬뽕_아이디, 10));
        final Menu menu = Menu.of("짬뽕", 30000, 추천메뉴_아이디, menuProducts);
        // when
        final Menu actual = menuRepository.save(menu);
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertTrue(actual.matchPrice(30000)),
                () -> assertTrue(actual.matchName("짬뽕")),
                () -> assertThat(actual.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("가격은 0 이상이어야 한다.")
    @Test
    void name() {
        // given
        final List<MenuProduct> menuProducts = Collections.singletonList(MenuProduct.of(짬뽕_아이디, 10));
        // when
        ThrowableAssert.ThrowingCallable createCall = () -> Menu.of("짬뽕", -190, 추천메뉴_아이디, menuProducts);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
