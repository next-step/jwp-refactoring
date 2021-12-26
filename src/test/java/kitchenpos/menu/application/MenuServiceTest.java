package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.MenuGroupNotFoundException;
import kitchenpos.menu.application.exception.ProductNotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    private Product 매콤치킨;
    private Product 치즈볼;
    private MenuProduct 매콤치킨구성;
    private MenuProduct 치즈볼구성;

    private MenuGroup 인기메뉴그룹;
    private Menu 매콤치킨단품;
    private Menu 매콤치즈볼세트;

    @BeforeEach
    void setUp() {
        매콤치킨 = new Product("매콤치킨", BigDecimal.valueOf(13000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(2000));

        매콤치킨구성 = new MenuProduct(매콤치킨, 1L);
        치즈볼구성 = new MenuProduct(치즈볼, 2L);

        인기메뉴그룹 = new MenuGroup("인기메뉴");

        매콤치킨단품 = Menu.of("매콤치킨단품", BigDecimal.valueOf(13000), 인기메뉴그룹, Collections.singletonList(매콤치킨구성));
        매콤치즈볼세트 = Menu.of("매콤치즈볼세트", BigDecimal.valueOf(15000), 인기메뉴그룹, Arrays.asList(매콤치킨구성, 치즈볼구성));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        MenuProductRequest 메뉴상품 = new MenuProductRequest(1L, 1L);
        MenuRequest 메뉴요청 = MenuRequest.of("매콤치킨단품", BigDecimal.valueOf(13000), 1L, Collections.singletonList(메뉴상품));

        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(인기메뉴그룹));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨));
        when(menuRepository.save(any())).thenReturn(매콤치킨단품);

        MenuResponse response = menuService.create(메뉴요청);

        verify(menuGroupRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(menuRepository, times(1)).save(any(Menu.class));
        assertThat(response)
                .extracting("name", "price")
                .containsExactly(매콤치킨단품.getName(), 매콤치킨단품.getPrice());
    }

    @Test
    @DisplayName("메뉴 그룹에 속하지 않는 메뉴인 경우 예외가 발생한다.")
    void validateMenuGroup() {
        MenuProductRequest 메뉴상품 = new MenuProductRequest(1L, 1L);
        MenuRequest 그룹없는메뉴 = MenuRequest.of("통통치킨한마리", BigDecimal.valueOf(10000), null, Collections.singletonList(메뉴상품));

        assertThatThrownBy(() -> menuService.create(그룹없는메뉴))
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴에 속한 상품이 존재하지 않는 경우 예외가 발생한다.")
    void validateProduct() {
        MenuProductRequest 메뉴상품 = new MenuProductRequest(1L, 1L);
        MenuRequest 상품없는메뉴 = MenuRequest.of("통통치킨한마리", BigDecimal.valueOf(10000), 1L, Collections.singletonList(메뉴상품));

        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(인기메뉴그룹));

        assertThatThrownBy(() -> menuService.create(상품없는메뉴))
                .isInstanceOf(ProductNotFoundException.class);
        verify(menuGroupRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(매콤치킨단품, 매콤치즈볼세트));

        List<MenuResponse> responses = menuService.list();

        verify(menuRepository, times(1)).findAll();
        assertThat(responses).hasSize(2);
    }
}