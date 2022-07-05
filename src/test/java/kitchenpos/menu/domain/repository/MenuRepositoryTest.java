package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuRepositoryTest {
    private static MenuGroup 햄버거_메뉴 = new MenuGroup(1L, "햄버거_메뉴");

    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuProductRepository menuProductRepository;

    private Menu 햄버거세트메뉴;

    @BeforeEach
    void setUp() {
        햄버거_메뉴 = menuGroupRepository.save(햄버거_메뉴);
        final Product 불고기버거 = productRepository.save(new Product(null, "불고기버거", BigDecimal.valueOf(1500)));
        final Product 새우버거 = productRepository.save(new Product(null, "새우버거", BigDecimal.valueOf(3000)));

        햄버거세트메뉴 = Menu.of("햄버거세트메뉴", BigDecimal.valueOf(8_000), 햄버거_메뉴);
        final Menu save = menuRepository.save(햄버거세트메뉴);
        menuProductRepository.save(MenuProduct.of(save.getId(), 불고기버거, 5L));
    }

    @Test
    @DisplayName("메뉴 추가")
    void create() {
        // when
        final Menu menu = menuRepository.save(햄버거세트메뉴);
        // then
        assertThat(menu).isInstanceOf(Menu.class);
    }
}
