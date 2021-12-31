package kitchenpos.infra.menu;

import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menu.domain.MenuProduct;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.product.domain.Product;
import kitchenpos.infra.menugroup.JpaMenuGroupRepository;
import kitchenpos.infra.product.JpaProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@DataJpaTest
@SpringBootTest
class MenuTest {
    @Autowired
    private JpaMenuRepository menuRepository;
    @Autowired
    private JpaMenuGroupRepository menuGroupRepository;
    @Autowired
    private JpaProductRepository productRepository;

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
}
