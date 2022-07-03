package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.domain.MenuTest.메뉴_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        Product product = 상품_생성(1L, "상품", 1_000);
        MenuRequest.ProductInfo productInfo = new MenuRequest.ProductInfo(product.getId(), 1);
        List<MenuRequest.ProductInfo> productInfos = Collections.singletonList(productInfo);
        MenuProduct menuProduct = new MenuProduct(product, productInfo.getQuantity());
        MenuRequest request = new MenuRequest("매뉴", BigDecimal.valueOf(1_000), 1L, productInfos);

        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(product));
        when(menuRepository.save(any())).thenReturn(메뉴_생성(1L, "매뉴", 1_000, 1L, Collections.singletonList(menuProduct)));

        // when
        MenuResponse response = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getMenuId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(request.getPrice()),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(request.getMenuGroupId())
        );
    }

    @DisplayName("메뉴의 가격은 0이상이여야 한다")
    @Test
    void createMenu1() {
        // given
        MenuRequest request = new MenuRequest("매뉴", BigDecimal.valueOf(1_000), null, null);

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴그룹에 포함되어야 한다")
    @Test
    void createMenu2() {
        // given
        MenuRequest request = new MenuRequest("매뉴", BigDecimal.valueOf(1_000), null, null);

        when(menuGroupRepository.existsById(any())).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 정보가 존재하지 않으면 안된다.")
    @Test
    void createMenu3() {
        // given
        Product product = 상품_생성(1L, "상품", 1_000);
        MenuRequest.ProductInfo productInfo = new MenuRequest.ProductInfo(product.getId(), 1);
        List<MenuRequest.ProductInfo> productInfos = Collections.singletonList(productInfo);
        MenuRequest request = new MenuRequest("매뉴", BigDecimal.valueOf(1_000), 1L, productInfos);

        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findAllByIdIn(any())).thenReturn(Collections.emptyList());
        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품의 가격의 합보다 크면 안된다.")
    @Test
    void createMenu4() {
        // given
        Product product = 상품_생성(1L, "상품", 1_000);
        MenuRequest.ProductInfo productInfo = new MenuRequest.ProductInfo(product.getId(), 1);
        List<MenuRequest.ProductInfo> productInfos = Collections.singletonList(productInfo);
        MenuRequest request = new MenuRequest("매뉴", BigDecimal.valueOf(2_000), 1L, productInfos);

        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void listMenu() {
        // given
        Menu menu = 메뉴_생성(1L, "매뉴", 1000, null, null);

        when(menuRepository.findAll()).thenReturn(Collections.singletonList(menu));

        // when
        List<MenuResponse> list = menuService.list();

        // then
        assertThat(list).hasSize(1);
    }
}
