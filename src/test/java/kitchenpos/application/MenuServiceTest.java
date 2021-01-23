package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuDao menuDao;
    @Autowired
    MenuGroupDao menuGroupDao;
    @Autowired
    MenuProductDao menuProductDao;
    @Autowired
    ProductRepository productRepository;

    MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao,
                menuProductDao, productRepository);
    }

    @DisplayName("1 개 이상의 메뉴를 등록할 수 있다.")
    @Test
    void createOneMenu() {
        menuGroupDao.save(new MenuGroup(1L, "그룹1"));
        Product 상품1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product 상품2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(상품1.getId(), 10L),
                new MenuProduct(상품2.getId(), 10L)
        );

        Menu expected = new Menu("메뉴1", new BigDecimal(28000), 1L, menuProducts);
        Menu saved = menuService.create(expected);
        assertThat(expected.getName()).isEqualTo(saved.getName());
        assertThat(expected.menuProductSize()).isSameAs(saved.menuProductSize());
        assertThat(expected.getPrice()).isEqualByComparingTo(saved.getPrice());
    }

    @DisplayName("등록된 상품이 없으면 메뉴도 등록 할 수 없다.")
    @Test
    void cantCrateOneMenuWhenNoProduct() {
        MenuGroup group = menuGroupDao.save(new MenuGroup("그룹1"));
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, -1L, 10L)
        );

        Menu expected = new Menu("메뉴1", new BigDecimal(10), group.getId(), menuProducts);
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0원보다 작으면 등록할 수 없다.")
    @Test
    void cantCrateOneMenuWhenPriceUnderZero() {
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, 1L, 10L),
                new MenuProduct(1L, 2L, 10L)
        );
        Menu expected = new Menu("메뉴1", new BigDecimal(-1), 1L, menuProducts);
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품금액의 합보다 크면 exception")
    @Test
    void menuPriceTest() {
        MenuGroup group = menuGroupDao.save(new MenuGroup("그룹1"));
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(2000)));
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(1L, product1.getId(), 2L),
                new MenuProduct(1L, product2.getId(), 1L)
        );
        menuProductDao.save(menuProducts.get(0));
        menuProductDao.save(menuProducts.get(1));

        Menu expected = new Menu("메뉴1", new BigDecimal(5000), group.getId(), menuProducts);
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 특정 메뉴 그룹에 속해야 한다.")
    @Test
    void menuIncludedGroupTest() {
        Product product = productRepository.save(new Product("상품1", new BigDecimal(1000)));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, product.getId(), 2L));
        menuProductDao.save(menuProducts.get(0));

        Menu expected = new Menu("메뉴1", new BigDecimal(5000), 1L, menuProducts);
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void listMenus() {
        MenuGroup group = menuGroupDao.save(new MenuGroup("그룹1"));
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(2000)));
        Menu menu = new Menu("메뉴1", new BigDecimal(10000), group.getId(), null);
        Menu save = menuDao.save(menu);
        MenuProduct menuProduct1 = menuProductDao.save(new MenuProduct(save.getId(), product1.getId(), 1));
        MenuProduct menuProduct2 = menuProductDao.save(new MenuProduct(save.getId(), product2.getId(), 2));
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        save.addMenuProducts(menuProducts);

        List<Menu> results = menuService.list();
        assertThat(results.size() > 1).isTrue();
        assertThat(results).contains(save);
    }
}