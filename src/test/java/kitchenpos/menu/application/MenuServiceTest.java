package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.DifferentOrderAndMenuPriceException;
import kitchenpos.menu.exception.NotCreatedProductException;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menu.exception.WrongPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 테스트")
class MenuServiceTest {
    private final MenuRepository menuRepository = new FakeMenuRepository();
    private final MenuGroupRepository menuGroupRepository = new FakeMenuGroupRepository();
    private final ProductRepository productRepository = new FakeProductRepository();
    private final MenuService menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);

    @DisplayName("가격이 음수면 예외가 발생한다.")
    @Test
    void priceIsNegative() {
        Product 살치살 = productRepository.save(Product.of(1L,"살치살",10000));
        Product 부채살 = productRepository.save(Product.of(2L,"부채살",10000));
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        MenuRequest menuRequest = MenuRequest.of("소고기세트", -1, savedMenuGroup,
                Arrays.asList(
                        MenuProductRequest.of(살치살, 2),
                        MenuProductRequest.of(부채살, 1)
                )
        );
        assertThatThrownBy( () -> menuService.create(menuRequest))
                .isInstanceOf(WrongPriceException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistsMenuGroup() {
        Product 살치살 = productRepository.save(Product.of(1L,"살치살",10000));
        Product 부채살 = productRepository.save(Product.of(2L,"부채살",10000));
        MenuGroup menuGroup = MenuGroup.of("추천메뉴");
        MenuRequest menuRequest = MenuRequest.of("소고기세트", 50000, menuGroup,
                Arrays.asList(
                        MenuProductRequest.of(살치살, 2),
                        MenuProductRequest.of(부채살, 1)
                )
        );

        assertThatThrownBy( () -> menuService.create(menuRequest))
                .isInstanceOf(DifferentOrderAndMenuPriceException.class);
    }

    @DisplayName("상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistsProduct() {
        Product 살치살 = Product.of(1L,"살치살",10000);
        Product 부채살 = Product.of(2L,"부채살",10000);
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        MenuRequest menuRequest = MenuRequest.of("소고기세트", 50000, savedMenuGroup,
                Arrays.asList(
                        MenuProductRequest.of(살치살, 2),
                        MenuProductRequest.of(부채살, 1)
                )
        );

        assertThatThrownBy( () -> menuService.create(menuRequest))
                .isInstanceOf(NotCreatedProductException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품들 가격의 합보다 크면 예외 발생한다.")
    @Test
    void priceCheaperThanSumMenuProducts() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Product 소고기 = productRepository.save(Product.of("소고기", 30000));
        Product 쌈채소 = productRepository.save(Product.of("쌈채소", 10000));
        MenuRequest menuRequest = MenuRequest.of("소고기세트", 100000, savedMenuGroup,
                Arrays.asList(
                        MenuProductRequest.of(소고기, 2),
                        MenuProductRequest.of(쌈채소, 1)
                )
        );
        assertThatThrownBy( () -> menuService.create(menuRequest))
                .isInstanceOf(DifferentOrderAndMenuPriceException.class);
    }

    @DisplayName("메뉴 생성 성공")
    @Test
    void success() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Product 소고기 = productRepository.save(Product.of("소고기", 30000));
        Product 쌈채소 = productRepository.save(Product.of("쌈채소", 10000));
        MenuRequest menuRequest = MenuRequest.of("소고기세트", 70000, savedMenuGroup,
                Arrays.asList(
                        MenuProductRequest.of(소고기, 2),
                        MenuProductRequest.of(쌈채소, 1)
                )
        );

        MenuResponse menuResponse = menuService.create(menuRequest);
        assertAll(
                () -> equalsMenu(menuResponse, savedMenuGroup.getId(), menuRequest),
                () -> equalsMenuProduct(menuResponse, menuRequest)
        );
    }

    @DisplayName("모든 메뉴 조회")
    @Test
    void list() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.of("추천메뉴"));
        Product 소고기 = productRepository.save(Product.of("소고기", 30000));
        Product 쌈채소 = productRepository.save(Product.of("쌈채소", 10000));
        Product 소주 = productRepository.save(Product.of("소주", 5000));
        MenuRequest menu1 = MenuRequest.of("소고기세트", 70000, savedMenuGroup,
                Arrays.asList(
                        MenuProductRequest.of(소고기, 2),
                        MenuProductRequest.of(쌈채소, 1)
                )
        );
        MenuRequest menu2 = MenuRequest.of("소고기세트", 105000, savedMenuGroup,
                Arrays.asList(
                        MenuProductRequest.of(소고기, 3),
                        MenuProductRequest.of(쌈채소, 1),
                        MenuProductRequest.of(소주, 1)
                )
        );

        MenuResponse menuResponse1 = menuService.create(menu1);
        MenuResponse menuResponse2 = menuService.create(menu2);

        List<MenuResponse> list = menuService.list();
        long count = getMenuProductCount(list);
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(count).isEqualTo(menuResponse1.getMenuProductResponses().size() + menuResponse2.getMenuProductResponses().size())
        );
    }

    private long getMenuProductCount(List<MenuResponse> list) {
        return list.stream()
                .map(menu -> menu.getMenuProductResponses())
                .flatMap(menuProducts -> menuProducts.stream())
                .count();
    }

    private void equalsMenuProduct(MenuResponse resultMenu, MenuRequest menu) {
        List<MenuProductResponse> savedMenuProducts = resultMenu.getMenuProductResponses();
        List<MenuProductRequest> menuProducts = menu.getMenuProductRequests();
        for (int i = 0; i < savedMenuProducts.size(); i++) {
            MenuProductResponse resultMenuProduct = savedMenuProducts.get(i);
            MenuProductRequest menuProduct = menuProducts.get(i);
            assertThat(resultMenuProduct.getQuantity()).isEqualTo(menuProduct.getQuantity());
            assertThat(resultMenuProduct.getMenuId()).isEqualTo(resultMenu.getId());
        }
    }

    private void equalsMenu(MenuResponse menuResponse, Long menuGroupId, MenuRequest menu) {
        assertThat(menuResponse.getMenuGroup().getId()).isEqualTo(menuGroupId);
        assertThat(menuResponse.getPrice()).isEqualTo(menu.getPrice());
        assertThat(menuResponse.getName()).isEqualTo(menu.getName());
    }

}
