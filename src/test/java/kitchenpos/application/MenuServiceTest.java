package kitchenpos.application;

import static kitchenpos.domain.MenuFixture.*;
import static kitchenpos.domain.MenuProductFixture.*;
import static kitchenpos.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 등록 API")
    @Test
    void create() {
        // given
        MenuProductRequest menuProductRequest = menuProductParam(1L, 2L);
        MenuRequest menuRequest = menuRequest("후라이드+후라이드", new BigDecimal(17000), 1L, Collections.singletonList(menuProductRequest));

        given(menuGroupRepository.existsById(menuRequest.getMenuGroupId())).willReturn(true);
        List<Product> savedProducts = Collections.singletonList(savedProduct(1L, new BigDecimal(10000)));

        given(productRepository.findAllById(anyList())).willReturn(savedProducts);

        MenuProduct savedMenuProduct = savedMenuProduct(1L, 1L, 2L);
        Menu savedMenu = savedMenu(1L, menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
            Collections.singletonList(savedMenuProduct));
        given(menuRepository.save(any())).willReturn(savedMenu);
        doNothing().when(menuValidator).validate(savedMenu, savedProducts, false);


        // when
        MenuResponse actual = menuService.create(menuRequest);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(savedMenu.getName()),
            () -> assertThat(actual.getPrice()).isEqualTo(savedMenu.getPrice()),
            () -> assertThat(actual.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId()),
            () -> assertThat(actual.getMenuProducts()).hasSize(1),
            () -> assertThat(actual.getMenuProducts().get(0).getSeq()).isNotNull(),
            () -> assertThat(actual.getMenuProducts().get(0).getProductId()).isEqualTo(1L),
            () -> assertThat(actual.getMenuProducts().get(0).getQuantity()).isEqualTo(2L)
        );
    }

    @Test
    void list() {
        // given
        Long menuId = 1L;
        MenuProduct menuProduct1 = savedMenuProduct(1L, 1L, 2L);
        MenuProduct menuProduct2 = savedMenuProduct(2L, 2L, 3L);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        Menu savedMenu = savedMenu(menuId, "메뉴", BigDecimal.valueOf(13000), 1L, menuProducts);
        given(menuRepository.findAll()).willReturn(Collections.singletonList(savedMenu));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertAll(
            () -> assertThat(menus).hasSize(1),
            () -> assertThat(menus.get(0).getName()).isEqualTo(savedMenu.getName()),
            () -> assertThat(menus.get(0).getMenuProducts()).hasSize(2)
        );
    }
}
