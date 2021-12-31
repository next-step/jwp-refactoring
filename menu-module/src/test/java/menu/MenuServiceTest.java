package menu;

import kitchenpos.AcceptanceTest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.exception.MenuPriceMoreThanMenuProductPriceSumException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 관련 기능")
class MenuServiceTest extends AcceptanceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistMenuGroup() {
        // given
        Product product = productRepository.save(new Product("후라이드", BigDecimal.valueOf(8000)));

        assertThatThrownBy(() -> {
            menuService.create(new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(18000), 1L, Arrays.asList(new MenuCreateRequest.MenuProductRequest(product.getId(), 1L))));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴에 등록하고자 하는 상품이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistProduct() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));

        // when
        assertThatThrownBy(() -> {
            menuService.create(new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuCreateRequest.MenuProductRequest(1L, 1L))));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴의 금액이 상품의 총 금액보다 크다면 예외가 발생한다.")
    void menuPriceMoreThanProductPriceSum() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        final Product product = productRepository.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2L);

        // when
        assertThatThrownBy(() -> {
            menuService.create(new MenuCreateRequest("후라이드 2마리", BigDecimal.valueOf(18000), menuGroup.getId(), Arrays.asList(new MenuCreateRequest.MenuProductRequest(menuProduct.getProduct(), 2L))));
        }).isInstanceOf(MenuPriceMoreThanMenuProductPriceSumException.class);
    }
}
