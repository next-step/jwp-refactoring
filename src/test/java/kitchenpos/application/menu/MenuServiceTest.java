package kitchenpos.application.menu;

import kitchenpos.application.menu.MenuService;
import kitchenpos.dao.menu.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuGroupDao menuGroupDao;
    @Autowired
    ProductRepository productRepository;

    MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupDao, productRepository);
    }

    @DisplayName("1 개 이상의 메뉴를 등록할 수 있다.")
    @Test
    void createOneMenu() {
        menuGroupDao.save(new MenuGroup(1L, "그룹1"));
        Product 상품1 = new Product("상품1", new BigDecimal(10000));
        Product 상품2 = new Product("상품2", new BigDecimal(20000));
        productRepository.saveAll(Arrays.asList(상품1, 상품2));
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(상품1, 1L),
                new MenuProduct(상품2, 1L)
        );

        MenuRequest request = new MenuRequest("메뉴1", 28000, 1L, menuProducts);
        MenuResponse saved = menuService.create(request);
        assertThat(request.getName()).isEqualTo(saved.getName());
        assertThat(MenuProductResponse.of(request.getMenuProducts())).isEqualTo(saved.getMenuProducts());
        assertThat(request.getPrice()).isEqualByComparingTo(saved.getPrice());
    }

    @DisplayName("등록된 상품이 없으면 메뉴도 등록 할 수 없다.")
    @Test
    void cantCrateOneMenuWhenNoProduct() {
        MenuGroup group = menuGroupDao.save(new MenuGroup("그룹1"));
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(new Product(-1L, "상품", new BigDecimal(1000)), 1L)
        );

        MenuRequest request = new MenuRequest("메뉴1", 10, group.getId(), menuProducts);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0원보다 작으면 등록할 수 없다.")
    @Test
    void cantCrateOneMenuWhenPriceUnderZero() {
        Product product = new Product("상품1", new BigDecimal(1000));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(product, 10L));
        MenuRequest request = new MenuRequest("메뉴1", -1, 1L, menuProducts);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품금액의 합보다 크면 exception")
    @Test
    void menuPriceTest() {
        MenuGroup group = menuGroupDao.save(new MenuGroup("그룹1"));
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(2000)));
        List<MenuProduct> menuProducts = Arrays.asList(
                new MenuProduct(product1, 2L),
                new MenuProduct(product2, 1L)
        );

        MenuRequest request = new MenuRequest("메뉴1",5000, group.getId(), menuProducts);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 특정 메뉴 그룹에 속해야 한다.")
    @Test
    void menuIncludedGroupTest() {
        Product product = productRepository.save(new Product("상품1", new BigDecimal(1000)));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(product, 2L));

        MenuRequest request = new MenuRequest("메뉴1", 5000, 1L, menuProducts);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    @Transactional
    void listMenus() {
        Product 상품1 = new Product(1L, "상품1", new BigDecimal(10000));
        Product 상품2 = new Product(2L, "상품2", new BigDecimal(20000));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(상품1, 1L), new MenuProduct(상품2, 1L));

        MenuRequest request = new MenuRequest("메뉴1", 28000, 1L, menuProducts);
        MenuResponse saved = menuService.create(request);

        List<MenuResponse> results = menuService.list();
        assertThat(results.size()).isGreaterThan(1);
        assertThat(results).contains(saved);
    }
}