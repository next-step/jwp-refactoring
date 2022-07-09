package kitchenpos.menu.domain.repository;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.repository.ProductRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public
class MenuRepositoryTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuProductRepository menuProductRepository;

    public Menu 햄버거세트_메뉴;
    public List<MenuProduct> 메뉴상품_리스트;

    @BeforeEach
    void setUp() {
        final Product 불고기버거 = productRepository.save(ProductRepositoryTest.불고기버거.toEntity());
        final Product 새우버거 = productRepository.save(ProductRepositoryTest.새우버거.toEntity());
        final MenuGroup 햄버거_메뉴 = menuGroupRepository.save(MenuGroupRepositoryTest.햄버거_메뉴.toEntity());

        메뉴상품_리스트 = Arrays.asList(MenuProduct.of(불고기버거.getId(), 5L), MenuProduct.of(새우버거.getId(), 1L));
        햄버거세트_메뉴 = Menu.of("햄버거세트메뉴", BigDecimal.valueOf(8_000), 햄버거_메뉴.getId());
    }

    @Test
    @DisplayName("메뉴 추가")
    public void create() {
        final Menu menu = menuRepository.save(햄버거세트_메뉴);
        menu.addMenuProducts(메뉴상품_리스트);
        // then
        assertThat(menu).isInstanceOf(Menu.class);
        assertThat(menu.getMenuProducts().stream().anyMatch(it -> menu.getId() == it.getMenuId())).isTrue();
    }

    @Test
    @DisplayName("메뉴 조회")
    public void findAll() {
        // given
        final Menu menu = menuRepository.save(햄버거세트_메뉴);
        menu.addMenuProducts(메뉴상품_리스트);
        // when
        final List<Menu> menus = menuRepository.findAll();
        // then
        assertThat(menus.stream().map(Menu::getName)
                .anyMatch(it -> it.equals(menu.getName()))).isTrue();
    }
}
