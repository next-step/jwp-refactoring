package kitchenpos.menu.application;

import static kitchenpos.menu.application.MenuGroupServiceTest.*;
import static kitchenpos.menu.application.ProductServiceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @Disabled
    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_생성(1L, "한마리메뉴");
        Product product = 상품_생성(1L, "후라이드", 16000);
        MenuProduct menuProduct = 메뉴_상품_등록(product, 1);
        Menu menu = 메뉴_생성("후라이드 치킨", 16000, menuGroup,
            Collections.singletonList(menuProduct));

        MenuRequest menuRequest = new MenuRequest(
            menu.getName().getValue(), menu.getPrice().getValue(), menu.getMenuGroup().getId(),
            menu.getMenuProducts().getValue().stream().map(menuProductRequest -> new MenuProductRequest(
                    menuProductRequest.getProduct().getId(), menuProductRequest.getQuantity().getValue()))
                .collect(Collectors.toList()));

        given(menuGroupRepository.existsById(menu.getMenuGroup().getId())).willReturn(true);
        given(productRepository.findById(menuProduct.getProduct().getId())).willReturn(Optional.of(product));
        given(menuRepository.save(menuRequest.toEntity(menuGroup))).willReturn(menu);

        // when
        MenuResponse savedMenu = menuService.create(menuRequest);

        // then
        assertEquals(menu.getId(), savedMenu.getId());
    }

    @DisplayName("메뉴그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void createMenuNonExistMenuGroup() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_생성(1L, "한마리메뉴");
        Product product = 상품_생성(1L, "후라이드", 16000);
        MenuProduct menuProduct = 메뉴_상품_등록(product, 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("후라이드 치킨", 16000, menuGroup,
            menuProducts);

        MenuRequest menuRequest = new MenuRequest(menu.getName().getValue(),
            menu.getPrice().getValue(), menu.getMenuGroup().getId(),
            menuProducts.stream().map(menuProductRequest -> new MenuProductRequest(
                    menuProductRequest.getProduct().getId(), menuProductRequest.getQuantity().getValue()))
                .collect(Collectors.toList()));

        given(menuGroupRepository.findById(menu.getMenuGroup().getId())).willReturn(Optional.empty());

        // when && then
        assertThrows(NotFoundException.class, () -> menuService.create(menuRequest));
    }

    @Disabled
    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenus() {
        // given
        MenuGroup menuGroup = 메뉴_그룹_생성(1L, "한마리메뉴");
        Product product = 상품_생성(1L, "후라이드", 16000);
        MenuProduct menuProduct = 메뉴_상품_등록(product, 1);
        Menu menu = 메뉴_생성("후라이드 치킨", 16000, menuGroup,
            Collections.singletonList(menuProduct));
        List<Menu> menus = Collections.singletonList(menu);

        given(menuRepository.findAll()).willReturn(menus);
        given(menuProductRepository.findAllByMenuId(menu.getId()))
            .willReturn(Collections.singletonList(menuProduct));

        // when
        List<MenuResponse> findMenus = menuService.list();

        // then
        assertThat(findMenus)
            .extracting("id")
            .containsExactlyElementsOf(menus.stream().map(Menu::getId).collect(Collectors.toList()));
    }

    private Menu 메뉴_생성(String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    private MenuProduct 메뉴_상품_등록(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }
}
