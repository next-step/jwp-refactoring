package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
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

    private MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
    private Product product = new Product(1L, "상품1", BigDecimal.valueOf(5000));
    private final List<MenuProductRequest> menu1MenuProductRequests = Arrays.asList(menuProductRequest);
    private final MenuProduct menuProduct1 = new MenuProduct(1L, new Menu(), product, 1);
    private final List<MenuProduct> menu1MenuProduct = Arrays.asList(menuProduct1);

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProductRequests);
        Menu menu1 = menuRequest1.toEntityWith(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1"), menu1MenuProduct);

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1")));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(menuRepository.save(any())).thenReturn(menu1);

        MenuResponse menuResponse = menuService.create(menuRequest1);

        assertThat(menuResponse.getName()).isEqualTo(menuRequest1.getName());
        assertThat(menuResponse.getPrice()).isEqualTo(menuRequest1.getPrice());
        assertThat(menuResponse.getMenuGroup().getId()).isEqualTo(menuRequest1.getMenuGroupId());
//        assertThat(menuResponse.getMenuProductRequests()).isEqualTo(menuRequest1.getMenuProductRequests());
    }

    @DisplayName("메뉴 가격은 0 이상이다.")
    @Test
    void 메뉴_가격이_올바르지_않으면_등록할_수_없다_1() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, invalidPrice, menu1MenuGroupId, menu1MenuProductRequests);

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1")));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 없으면 메뉴를 등록할 수 없다.")
    @Test
    void 메뉴_가격이_올바르지_않으면_등록할_수_없다_2() {
        BigDecimal invalidPrice = null;
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, invalidPrice, menu1MenuGroupId, menu1MenuProductRequests);

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1")));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 내의 메뉴상품들 총합보다 크면 등록할 수 없다.")
    @Test
    void 메뉴_가격이_올바르지_않으면_등록할_수_없다_3() {
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProductRequests);

        Product diffPriceProduct = new Product(2L, "상품2", BigDecimal.valueOf(500));

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));
        when(productRepository.findById(any())).thenReturn(Optional.of(diffPriceProduct));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴그릅의 메뉴는 등록할 수 없다.")
    @Test
    void 메뉴의_메뉴그룹이_올바르지_않으면_등록할_수_없다() {
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProductRequests);

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1")));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 내의 메뉴상품들 중 등록이 안된 메뉴상품이 있으면 등록할 수 없다.")
    @Test
    void 메뉴의_메뉴상품들이_올바르지_않으면_등록할_수_없다() {
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProductRequests);

        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1")));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체를 조회할 수 있다.")
    @Test
    void listTest() {
        MenuRequest menuRequest1 = new MenuRequest(menu1Name, menu1Price, menu1MenuGroupId, menu1MenuProductRequests);
        Menu menu1 = menuRequest1.toEntityWith(MenuGroup.of(menu1MenuGroupId, "메뉴그룹1"), menu1MenuProduct);

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1));

        List<MenuResponse> menuResponses = menuService.list();

        assertThat(menuResponses.get(0).getName()).contains(menu1.getName());
    }

}
