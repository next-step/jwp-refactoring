package kitchenpos.application;

import kitchenpos.domain.ProductRepository;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 서비스 관련 테스트")
public class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;

    private Long menu1Id = 1L;
    private String menu1Name = "메뉴이름1";
    private BigDecimal menu1Price = BigDecimal.valueOf(1000);
    private Long menu1MenuGroupId = 1L;

    private MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 1);
    private Product product = new Product(1L, "상품1", BigDecimal.valueOf(1000));
    private List<MenuProduct> menu1MenuProducts = Arrays.asList(menuProduct);

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        Menu menu1 = new Menu(menu1Id, menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProducts);

        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuRepository.save(any())).thenReturn(menu1);
        when(menuProductRepository.save(any())).thenReturn(menuProduct);

        Menu menuResponse = menuService.create(menu1);

        assertThat(menuResponse.getId()).isEqualTo(menu1.getId());
        assertThat(menuResponse.getName()).isEqualTo(menu1.getName());
        assertThat(menuResponse.getPrice()).isEqualTo(menu1.getPrice());
        assertThat(menuResponse.getMenuGroupId()).isEqualTo(menu1.getMenuGroupId());
        assertThat(menuResponse.getMenuProducts()).isEqualTo(menu1.getMenuProducts());
    }

    @DisplayName("메뉴 가격은 0 이상이다.")
    @Test
    void 메뉴_가격이_올바르지_않으면_등록할_수_없다_1() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        Menu menu1 = new Menu(menu1Id, menu1Name, invalidPrice, menu1MenuGroupId, menu1MenuProducts);

        assertThatThrownBy(() -> {
            menuService.create(menu1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 없으면 메뉴를 등록할 수 없다.")
    @Test
    void 메뉴_가격이_올바르지_않으면_등록할_수_없다_2() {
        BigDecimal invalidPrice = null;
        Menu menu1 = new Menu(menu1Id, menu1Name, invalidPrice, menu1MenuGroupId, menu1MenuProducts);

        assertThatThrownBy(() -> {
            menuService.create(menu1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 내의 메뉴상품들 총합보다 크면 등록할 수 없다.")
    @Test
    void 메뉴_가격이_올바르지_않으면_등록할_수_없다_3() {
        Menu menu1 = new Menu(menu1Id, menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProducts);

        Product diffPriceProduct = new Product(2L, "상품2", BigDecimal.valueOf(500));

        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(diffPriceProduct));

        assertThatThrownBy(() -> {
            menuService.create(menu1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴그릅의 메뉴는 등록할 수 없다.")
    @Test
    void 메뉴의_메뉴그룹이_올바르지_않으면_등록할_수_없다() {
        Menu menu1 = new Menu(menu1Id, menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProducts);

        when(menuGroupRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> {
            menuService.create(menu1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 내의 메뉴상품들 중 등록이 안된 메뉴상품이 있으면 등록할 수 없다.")
    @Test
    void 메뉴의_메뉴상품들이_올바르지_않으면_등록할_수_없다() {
        Menu menu1 = new Menu(menu1Id, menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProducts);

        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            menuService.create(menu1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체를 조회할 수 있다.")
    @Test
    void listTest() {
        Menu menu1 = new Menu(menu1Id, menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProducts);

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1));
        when(menuProductRepository.findAllByMenuId(any())).thenReturn(menu1MenuProducts);

        assertThat(menuService.list()).contains(menu1);
        assertThat(menu1.getMenuProducts()).contains(menuProduct);
    }

}
