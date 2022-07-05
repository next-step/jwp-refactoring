package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MenuProductRepository menuProductRepository;
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    Product 스낵랩;
    Product 맥모닝;
    MenuProduct 스낵랩_메뉴_상품;
    MenuProduct 맥모닝_메뉴_상품;
    MenuGroup 패스트푸드류;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product("스낵랩", BigDecimal.valueOf(3000));
        스낵랩_메뉴_상품 = new MenuProduct(스낵랩, 1);

        맥모닝 = new Product("맥모닝",BigDecimal.valueOf(4000));
        맥모닝_메뉴_상품 = new MenuProduct(맥모닝, 1);

        패스트푸드류 = new MenuGroup("패스트푸드");
        menuGroupRepository.save(패스트푸드류);
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("미존재하는 메뉴 그룹으로 메뉴 등록 시 에러 반환")
    public void createNonExistsMenuGroup() {
        Menu menu = new Menu("탕수육 세트", new MenuPrice(BigDecimal.valueOf(18000)), new MenuGroup());

        assertThatThrownBy(() -> menuService.create(MenuRequest.of(menu))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미존재하는 상품으로 메뉴 구성시 에러 반황")
    public void createNonExistsProduct() {
        MenuProduct menuProduct = new MenuProduct(null, 2);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        Menu menu = new Menu(new MenuPrice(BigDecimal.valueOf(18000)), new MenuProducts(menuProducts));

        assertThatThrownBy(() -> menuService.create(MenuRequest.of(menu))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 각 상품의 합보다 메뉴의 가격이 작지 않으면 등록 시 에러 반환")
    public void createNotCheaperPrice() {
        Menu menu = new Menu("모닝세트", BigDecimal.valueOf(8000), 패스트푸드류,
                new MenuProducts(Arrays.asList(스낵랩_메뉴_상품, 맥모닝_메뉴_상품)));
        assertThatThrownBy(() -> menuService.create(MenuRequest.of(menu))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createSuccess() {
        productRepository.save(스낵랩);
        MenuProduct menuProduct = menuProductRepository.save(스낵랩_메뉴_상품);

        Menu menu = new Menu("스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류,
                new MenuProducts(Arrays.asList(menuProduct)));
        assertThat(menuService.create(MenuRequest.of(menu)).getId()).isNotNull();
    }

    @Test
    public void list() {
        스낵랩_메뉴_상품.setProduct(productRepository.save(스낵랩));
        맥모닝_메뉴_상품.setProduct(productRepository.save(맥모닝));

        MenuProduct 모닝_스낵랩 = new MenuProduct(스낵랩, 1);

        Menu 스낵랩_세트 = new Menu("스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류,
                new MenuProducts(Arrays.asList(스낵랩_메뉴_상품)));
        Menu 모닝_세트 = new Menu("모닝_세트", BigDecimal.valueOf(7000), 패스트푸드류,
                new MenuProducts(Arrays.asList(모닝_스낵랩, 맥모닝_메뉴_상품)));

        menuRepository.save(스낵랩_세트);
        menuRepository.save(모닝_세트);

        List<Menu> list = menuService.list();
        assertThat(list).hasSize(2);
    }
}