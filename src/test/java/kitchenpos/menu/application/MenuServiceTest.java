package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.application.MenuService;
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
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성 테스트")
    @Test
    void createTest() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        MenuProduct 메뉴_불고기 = new MenuProduct(1L, 불고기.getId(), 3);
        Menu menu = Menu.Builder.of("메뉴1", new BigDecimal(2000))
                                .menuGroupId(1L)
                                .menuProducts(Arrays.asList(메뉴_불고기))
                                .build();
        Mockito.when(menuGroupDao.existsById(any())).thenReturn(true);
        Mockito.when(productDao.findById(any())).thenReturn(Optional.of(불고기));
        Mockito.when(menuDao.save(any())).thenReturn(menu);
        Mockito.when(menuProductDao.save(any())).thenReturn(메뉴_불고기);

        // when
        Menu result = menuService.create(menu);

        // then
        assertAll(() -> {
           assertThat(result).isNotNull();
           assertThat(result.getName()).isEqualTo("메뉴1");
           assertThat(result.getMenuGroupId()).isEqualTo(1L);
           assertThat(result.getMenuProducts()).isNotEmpty().containsExactly(메뉴_불고기);
        });
        Mockito.verify(menuGroupDao).existsById(any());
        Mockito.verify(productDao).findById(any());
        Mockito.verify(menuDao).save(any());
        Mockito.verify(menuProductDao).save(any());
    }

    @DisplayName("메뉴 그룹이 없는 경우")
    @Test
    void notExistedMenuGroup() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        MenuProduct 메뉴_불고기 = new MenuProduct(1L, 불고기.getId(), 3);
        Menu menu = Menu.Builder.of("메뉴1", new BigDecimal(2000))
                                .menuGroupId(1L)
                                .menuProducts(Arrays.asList(메뉴_불고기))
                                .build();
        Mockito.when(menuGroupDao.existsById(any())).thenReturn(false);

        // when
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 금액보다 메뉴 가격이 더 비싼경우")
    @Test
    void overPriceMenu() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        MenuProduct 메뉴_불고기 = new MenuProduct(1L, 불고기.getId(), 3);
        Menu menu = Menu.Builder.of("메뉴1", new BigDecimal(5000))
                                .menuGroupId(1L)
                                .menuProducts(Arrays.asList(메뉴_불고기))
                                .build();
        Mockito.when(menuGroupDao.existsById(any())).thenReturn(true);
        Mockito.when(productDao.findById(any())).thenReturn(Optional.of(불고기));

        // when
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
