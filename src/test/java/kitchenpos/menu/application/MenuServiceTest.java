package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.domain.dto.MenuProductRequest;
import kitchenpos.menu.domain.dto.MenuRequest;
import kitchenpos.menu.domain.dto.MenuResponse;
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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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
    private MenuProductRequest 매콤치킨구성;
    private MenuProductRequest 치즈볼구성;
    private MenuGroup 인기메뉴그룹;
    private MenuRequest 매콤치킨단품;
    private MenuRequest 매콤치즈볼세트;

    @BeforeEach
    void setUp() {
        매콤치킨 = new Product(1L, "매콤치킨", BigDecimal.valueOf(13000));
        치즈볼 = new Product(2L, "치즈볼", BigDecimal.valueOf(2000));

        매콤치킨구성 = new MenuProductRequest(매콤치킨.getId(), 1L);
        치즈볼구성 = new MenuProductRequest(치즈볼.getId(), 2L);

        인기메뉴그룹 = new MenuGroup(1L, "인기메뉴");
        매콤치킨단품 = MenuRequest.of("매콤치킨단품", BigDecimal.valueOf(13000), 인기메뉴그룹.getId(), Collections.singletonList(매콤치킨구성));
        매콤치즈볼세트 = MenuRequest.of("매콤치즈볼세트", BigDecimal.valueOf(15000), 인기메뉴그룹.getId(), Arrays.asList(매콤치킨구성, 치즈볼구성));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        MenuProduct 매콤치킨구성 = new MenuProduct(매콤치킨, 1L);
        Menu 메뉴 = Menu.of("매콤치킨단품", BigDecimal.valueOf(13000), 인기메뉴그룹, Collections.singletonList(매콤치킨구성));

        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(인기메뉴그룹));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨));
        when(menuRepository.save(any())).thenReturn(메뉴);

        MenuResponse response = menuService.create(매콤치킨단품);

        verify(menuGroupRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
        verify(menuRepository, times(1)).save(any(Menu.class));
        assertThat(response)
                .extracting("name", "price")
                .containsExactly(매콤치킨단품.getName(), 매콤치킨단품.getPrice());
    }

    @Test
    @DisplayName("메뉴의 가격이 상품 가격의 합보다 큰 경우 예외가 발생한다.")
    void validateProductsSum() {
        BigDecimal overPriceSum = BigDecimal.valueOf(18000);
        List<MenuProductRequest> 메뉴상품구성 = Arrays.asList(매콤치킨구성, 치즈볼구성);// 13000 + 2000
        MenuRequest 비싼매콤치즈볼세트 = MenuRequest.of("비싼매콤치즈볼세트", overPriceSum, 인기메뉴그룹.getId(), 메뉴상품구성);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(비싼매콤치즈볼세트));
    }

    @Test
    @DisplayName("메뉴의 가격이 null인 경우 예외가 발생한다.")
    void validatePriceNull() {
        MenuRequest menu = MenuRequest.of("통통치킨한마리", null, 인기메뉴그룹.getId(), Collections.singletonList(매콤치킨구성));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 미만인 경우 예외가 발생한다.")
    void validateMinPrice() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        MenuRequest menu = MenuRequest.of("통통치킨한마리", invalidPrice, 인기메뉴그룹.getId(), Collections.singletonList(매콤치킨구성));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴 그룹에 속하지 않는 메뉴인 경우 예외가 발생한다.")
    void validateMenuGroup() {
        MenuRequest menu = MenuRequest.of("통통치킨한마리", BigDecimal.valueOf(10000), null, Collections.singletonList(매콤치킨구성));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴에 속한 상품이 존재하지 않는 경우 예외가 발생한다.")
    void validateProduct() {
        MenuRequest menu = MenuRequest.of("통통치킨한마리", BigDecimal.valueOf(10000), 인기메뉴그룹.getId(), Collections.singletonList(매콤치킨구성));

        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(인기메뉴그룹));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
        verify(menuGroupRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        MenuProduct 매콤치킨구성 = new MenuProduct(매콤치킨, 1L);
        MenuProduct 치즈볼구성 = new MenuProduct(치즈볼, 2L);
        Menu 매콤치킨단품 = Menu.of("매콤치킨단품", BigDecimal.valueOf(13000), 인기메뉴그룹, Collections.singletonList(매콤치킨구성));
        Menu 매콤치즈볼세트 = Menu.of("매콤치즈볼세트", BigDecimal.valueOf(15000), 인기메뉴그룹, Arrays.asList(매콤치킨구성, 치즈볼구성));

        when(menuRepository.findAll()).thenReturn(Arrays.asList(매콤치킨단품, 매콤치즈볼세트));

        List<MenuResponse> responses = menuService.list();

        verify(menuRepository, times(1)).findAll();
        assertThat(responses).hasSize(2);
    }
}