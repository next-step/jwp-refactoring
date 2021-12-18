package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
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

import static kitchenpos.domain.MenuProductTest.콜라;
import static kitchenpos.domain.MenuProductTest.통새우와퍼;
import static kitchenpos.domain.MenuTest.통새우와퍼_세트;
import static kitchenpos.domain.ProductTest.콜라_상품;
import static kitchenpos.domain.ProductTest.통새우와퍼_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관리 테스트")
public class MenuServiceTest {
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

    @Mock
    private Menu menu;

    @Test
    @DisplayName("메뉴 등록")
    void createTest() {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        Product product = mock(Product.class);
        given(product.getPrice()).willReturn(BigDecimal.valueOf(5000));
        MenuProduct menuProduct = mock(MenuProduct.class);
        given(menuProduct.getQuantity()).willReturn(1L);
        given(menu.getMenuProducts()).willReturn(Collections.singletonList(menuProduct));
        given(menu.getPrice()).willReturn(BigDecimal.valueOf(4000));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(menuRepository.save(any())).willReturn(menu);
        // when
        Menu actual = menuService.create(menu);
        // then
        verify(menuProductRepository, times(1)).save(any());
        assertThat(actual).isEqualTo(menu);
    }

    @Test
    @DisplayName("메뉴 가격은 0원 이상 이어야 한다.")
    void menuPriceOverZero() {
        // given
        given(menu.getPrice()).willReturn(BigDecimal.valueOf(-1));
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 등록 할 수 없다.")
    void notFoundMenuGroup() {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(통새우와퍼_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 상품들이 존재하지 않으면 등록 할 수 없다.")
    void notFoundMenuProduct() {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> menuService.create(통새우와퍼_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 [`상품 가격 * 메뉴에 속하는 상품 수량`]의 합보다 클 수 없다.")
    void menuPriceNotOverTotalProductPrice() {
        // given
        given(menu.getMenuProducts()).willReturn(Arrays.asList(통새우와퍼, 콜라));
        given(menu.getPrice()).willReturn(BigDecimal.valueOf(7001));
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(통새우와퍼_상품), Optional.of(콜라_상품));
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 리스트 조회")
    void listTest() {
        // given
        given(menuRepository.findAll()).willReturn(Collections.singletonList(통새우와퍼_세트));
        // when
        List<Menu> actual = menuService.list();
        // then
        verify(menuProductRepository, times(1)).findAllByMenuId(any());
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactly(통새우와퍼_세트)
        );
    }
}
