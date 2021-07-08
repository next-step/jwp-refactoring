package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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

    @DisplayName("메뉴 생성 테스트")
    @Test
    void createTest() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        MenuProduct 메뉴_불고기 = new MenuProduct(1L, 불고기.getId(), 3);
        MenuRequest menuRequest = MenuRequest.Builder.of("메뉴1", new BigDecimal(2000))
                                                     .menuGroupId(1L)
                                                     .menuProducts(Arrays.asList(new MenuProductRequest(1L, 1000)))
                                                     .build();
        Mockito.when(menuGroupRepository.existsById(any())).thenReturn(true);
        Mockito.when(productRepository.findById(any())).thenReturn(Optional.of(불고기));
        Mockito.when(menuRepository.save(any())).thenReturn(menuRequest.toMenu());
        Mockito.when(menuProductRepository.save(any())).thenReturn(메뉴_불고기);

        // when
        MenuResponse result = menuService.create(menuRequest);

        // then
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("메뉴1");
            assertThat(result.getMenuGroupId()).isEqualTo(1L);
        });
        Mockito.verify(menuGroupRepository).existsById(any());
        Mockito.verify(productRepository).findById(any());
        Mockito.verify(menuRepository).save(any());
        Mockito.verify(menuProductRepository).save(any());
    }

    @DisplayName("메뉴 그룹이 없는 경우")
    @Test
    void notExistedMenuGroup() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        MenuRequest menuRequest = MenuRequest.Builder.of("메뉴1", new BigDecimal(2000))
                                                     .menuGroupId(1L)
                                                     .menuProducts(Arrays.asList(new MenuProductRequest()))
                                                     .build();
        Mockito.when(menuGroupRepository.existsById(any())).thenReturn(false);

        // when
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 금액보다 메뉴 가격이 더 비싼경우")
    @Test
    void overPriceMenu() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        MenuRequest menuRequest = MenuRequest.Builder.of("메뉴1", new BigDecimal(5000))
                                                     .menuGroupId(1L)
                                                     .menuProducts(Arrays.asList(new MenuProductRequest()))
                                                     .build();
        menuRequest.setMenuProducts(Arrays.asList(new MenuProductRequest(1L, 3)));
        Mockito.when(menuGroupRepository.existsById(any())).thenReturn(true);
        Mockito.when(productRepository.findById(any())).thenReturn(Optional.of(불고기));

        // when
        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
