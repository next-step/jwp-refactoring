package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    private Menu menu;
    private Product newProduct;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        newProduct = new Product(1L,"강정치킨", new BigDecimal(17000));
        menuProduct = new MenuProduct(1L, 1L, 1L, 2);

        menu = new Menu(1L, "후라이드+후라이드", new BigDecimal(34000), 1L);
        menu.changeMenuProducts(Arrays.asList(menuProduct));
    }

    @Test
    @DisplayName("메뉴등록")
    void create() {
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(newProduct));
        when(menuRepository.save(any())).thenReturn(menu);
        when(menuProductRepository.save(any())).thenReturn(menuProduct);

        assertThat(menuService.create(menu)).isNotNull();
    }

    @Test
    @DisplayName("메뉴등록시 메뉴그룹이 존재하지 않으면 등록 할 수 없음")
    void callExceptionNotMenuGroup() {
        when(menuGroupRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴조회")
    void list() {
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));
        when(menuProductRepository.findAllByMenuId(any())).thenReturn(Arrays.asList(menuProduct));

        assertThat(menuService.list()).isNotNull();
    }
}
