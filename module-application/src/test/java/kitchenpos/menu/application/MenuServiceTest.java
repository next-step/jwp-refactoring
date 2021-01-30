package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    ProductService productService;

    MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productService);
    }

    @DisplayName("1 개 이상의 메뉴를 등록할 수 있다.")
    @Test
    void createOneMenu() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
        Product product1 = productRepository.save(new Product("상품", Price.of(1000)));
        Product product2 = productRepository.save(new Product("상품2", Price.of(2000)));
        MenuProductRequest productRequest1 = new MenuProductRequest(product1.getId(), 2L);
        MenuProductRequest productRequest2 = new MenuProductRequest(product2.getId(), 3L);
        MenuRequest request = new MenuRequest("메뉴", 1000, menuGroup.getId(), Arrays.asList(productRequest1, productRequest2));
        MenuResponse saved = menuService.create(request);
        
        assertThat(request.getName()).isEqualTo(saved.getName());
    }

    @DisplayName("등록된 상품이 없으면 메뉴도 등록 할 수 없다.")
    @Test
    void cantCreateOneMenuWhenNoProduct() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹"));
        MenuRequest request = new MenuRequest("메뉴", 20000, menuGroup.getId(), Arrays.asList(new MenuProductRequest(menuGroup.getId(), 2L)));

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
        Product 상품 = productRepository.save(new Product("상품1", Price.of(100)));

        List<MenuProductRequest> menuProductsRequest = Arrays.asList(new MenuProductRequest(상품.getId(), 1L));
        MenuRequest request = new MenuRequest("메뉴1",5000, group.getId(), menuProductsRequest);
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 특정 메뉴 그룹에 속해야 한다.")
    @Test
    void menuIncludedGroupTest() {
        Product product = productRepository.save(new Product("상품1", Price.of(1000)));
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
        List<MenuResponse> results = menuService.list();
        assertThat(results.size()).isGreaterThan(1);
    }
}