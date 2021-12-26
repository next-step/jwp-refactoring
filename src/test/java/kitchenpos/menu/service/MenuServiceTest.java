package kitchenpos.menu.service;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.MenuFactory;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given

        MenuGroup menuGroup = MenuFactory.ofMenuGroup(1L, "튀김류");
        Product product = MenuFactory.ofProduct(1L, "양념치킨", 5000);

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), menuGroup.getId(), Collections.singletonList(menuProductRequest));

        Menu menu = MenuFactory.ofMenu("치킨", Price.of(BigDecimal.valueOf(5000)), menuGroup);
        Menu savedMenu = MenuFactory.ofMenu(1L, "치킨", Price.of(BigDecimal.valueOf(5000)), menuGroup, Collections.singletonList(MenuProduct.of(product, Quantity.of(1L))));

        given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(menuRepository.save(menu)).willReturn(savedMenu);

        // when
        MenuResponse response = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(response.getMenuGroup().getId()).isEqualTo(menuRequest.getMenuGroupId()),
                () -> assertThat(response.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @DisplayName("메뉴가 메뉴 묶음에 존재하는 메뉴여야 한다.")
    @Test
    void 메뉴_생성_메뉴_그룹에_존재하지_않음() {
        // given
        MenuGroup menuGroup = MenuFactory.ofMenuGroup(1L, "튀김류");
        Product product = MenuFactory.ofProduct(1L, "양념치킨", 5000);

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(5000), menuGroup.getId(), Collections.singletonList(menuProductRequest));

        given(menuGroupRepository.findById(1L)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> menuService.create(menuRequest));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 메뉴 그룹을 찾을 수 없습니다.");
    }

    @DisplayName("메뉴 생성 가격이 null 이거나 0원보다 낮으면 안된다.")
    @Test
    void 메뉴_생성_가격_0미만_예외() {
        // given
        MenuGroup menuGroup = MenuFactory.ofMenuGroup(1L, "튀김류");
        Product product = MenuFactory.ofProduct(1L, "양념치킨", 5000);

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("치킨", BigDecimal.valueOf(-1), menuGroup.getId(), Collections.singletonList(menuProductRequest));

        given(menuGroupRepository.findById(1L)).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        Throwable thrown = catchThrowable(() -> menuService.create(menuRequest));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        MenuGroup menuGroup = MenuFactory.ofMenuGroup(1L, "튀김류");
        Product product = MenuFactory.ofProduct(1L, "양념치킨", 5000);
        Menu savedMenu = MenuFactory.ofMenu(1L, "치킨", Price.of(BigDecimal.valueOf(5000)), menuGroup, Collections.singletonList(MenuProduct.of(product, Quantity.of(1L))));

        given(menuRepository.findAll()).willReturn(Collections.singletonList(savedMenu));

        // when
        List<MenuResponse> response = menuService.list();

        // then
        assertThat(response.size()).isEqualTo(1);
    }
}
