package kitchenpos.menu.application;

import kitchenpos.common.BaseTest;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 비즈니스 로직을 처리하는 서비스 테스트")
class MenuRequestServiceTest extends BaseTest {
    private static final String MENU_NAME = "두마리메뉴";
    private static final long PRICE = 29_000L;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuRequest menuRequest;
    private List<MenuProductRequest> menuProductRequests;

    @BeforeEach
    void setUp() {
        productRepository.save(Product.of("후라이드", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("양념치킨", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("반반치킨", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("통구이", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("간장치킨", Price.of(new BigDecimal(16_000L))));
        productRepository.save(Product.of("순살치킨", Price.of(new BigDecimal(16_000L))));

        menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(1L, 2));
        menuProductRequests.add(new MenuProductRequest(2L, 1));

        final MenuGroup newMenuGroup = new MenuGroup();

        newMenuGroup.setName("두마리메뉴");
        final MenuGroup createdMenuGroup = menuGroupService.create(newMenuGroup);

        menuRequest = new MenuRequest(MENU_NAME, PRICE, createdMenuGroup.getId(), menuProductRequests);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void 메뉴_생성() {
        final MenuResponse savedMenu = menuService.create(menuRequest);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(MENU_NAME);
        assertThat(savedMenu.getPrice()).isEqualTo(PRICE);
    }

    @DisplayName("메뉴 가격이 음수인 경우 메뉴를 생성할 수 없다.")
    @Test
    void 가격이_음수인_경우_메뉴_생성() {
        menuRequest.setPrice(-1);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹인 경우 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_메뉴_그룹인_경우_메뉴_생성() {
        assertThatThrownBy(() -> menuService.create(new MenuRequest(MENU_NAME, PRICE, 9L, menuProductRequests)))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("존재하지 않는 상품인 경우 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_상품인_경우_메뉴_생성() {
        menuProductRequests.add(new MenuProductRequest(7L, 2));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 속한 상품의 가격과 수량을 곱하여 합산한 총액보다 큰 경우 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴_가격이_상품_총액보다_큰_경우_메뉴_생성() {
        MenuGroup newMenuGroup = new MenuGroup();

        newMenuGroup.setName("한마리메뉴");
        menuGroupRepository.save(newMenuGroup);

        newMenuGroup.setName("순살파닭두마리메뉴");
        menuGroupRepository.save(newMenuGroup);

        newMenuGroup.setName("신메뉴");
        menuGroupRepository.save(newMenuGroup);

        assertThatThrownBy(() -> menuService.create(new MenuRequest(MENU_NAME, 10_000_000L, 2L, menuProductRequests)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    @Transactional
    void 메뉴_조회() {
        menuService.create(menuRequest);

        final List<MenuResponse> responseMenus = menuService.findAllMenus();
        final List<MenuProductResponse> menuProducts = responseMenus.stream()
            .flatMap(menu -> menu.getMenuProducts().stream())
            .collect(Collectors.toList());

        assertThat(responseMenus.size()).isGreaterThan(0);
        assertThat(menuProducts.size()).isGreaterThan(0);
    }
}
