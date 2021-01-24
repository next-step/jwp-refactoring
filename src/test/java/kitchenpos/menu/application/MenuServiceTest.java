package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuProductRequest;
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
    MenuGroupRepository menuGroupRepository;
    @Autowired
    ProductRepository productRepository;

    MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);
    }

    @DisplayName("1 개 이상의 메뉴를 등록할 수 있다.")
    @Test
    void createOneMenu() {
        Product 상품 = productRepository.save(new Product("상품1", new BigDecimal(1000)));

        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(상품.getId(), 1L));
        MenuRequest request = new MenuRequest("메뉴1", 1000, 1L, menuProductsRequest);

        MenuResponse saved = menuService.create(request);
        assertThat(request.getName()).isEqualTo(saved.getName());
        assertThat(menuProductsRequest.get(0).getProductId()).isEqualTo(상품.getId());
        assertThat(request.getPrice()).isEqualByComparingTo(saved.getPrice());
    }

    @DisplayName("등록된 상품이 없으면 메뉴도 등록 할 수 없다.")
    @Test
    void cantCreateOneMenuWhenNoProduct() {
        MenuGroup group = menuGroupRepository.save(new MenuGroup("그룹1"));
        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(-1L, 1L));
        MenuRequest request = new MenuRequest("메뉴1", 10, group.getId(), menuProductsRequest);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0원보다 작으면 등록할 수 없다.")
    @Test
    void cantCrateOneMenuWhenPriceUnderZero() {
        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(1L, 1L));

        MenuRequest request = new MenuRequest("메뉴1", -1, 1L, menuProductsRequest);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품금액의 합보다 크면 exception")
    @Test
    void menuPriceTest() {
        MenuGroup group = menuGroupRepository.save(new MenuGroup("그룹1"));
        Product 상품 = productRepository.save(new Product("상품1", new BigDecimal(100)));

        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(상품.getId(), 1L));
        MenuRequest request = new MenuRequest("메뉴1",5000, group.getId(), menuProductsRequest);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 특정 메뉴 그룹에 속해야 한다.")
    @Test
    void menuIncludedGroupTest() {
        Product product = productRepository.save(new Product("상품1", new BigDecimal(1000)));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(product, 2L));

        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(1L, 1L));
        MenuRequest request = new MenuRequest("메뉴1", 5000, -1L, menuProductsRequest);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    @Transactional
    void listMenus() {
        Product 상품1 = new Product(1L, "상품1", new BigDecimal(10000));
        Product 상품2 = new Product(2L, "상품2", new BigDecimal(20000));

        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(상품1.getId(), 1L), new MenuProductRequest(상품2.getId(), 1L));
        MenuRequest request = new MenuRequest("메뉴1", 28000, 1L, menuProductsRequest);
        MenuResponse saved = menuService.create(request);

        List<MenuResponse> results = menuService.list();
        assertThat(results.size()).isGreaterThan(1);
        assertThat(results).contains(saved);
    }
}