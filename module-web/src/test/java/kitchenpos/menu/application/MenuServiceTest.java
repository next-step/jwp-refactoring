package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuRequest 강정치킨과양념치킨;
    private List<MenuProductRequest> 상품리스트요청 = new ArrayList<>();
    private Product 강정치킨;
    private Product 양념치킨;
    private Menu 강정치킨과양념치킨메뉴;
    private List<MenuProduct> 메뉴상품리스트 = new ArrayList<>();
    private List<Product> 상품리스트 = new ArrayList<>();
    private List<Menu> 메뉴리스트 = new ArrayList<>();
    private MenuGroup menuGroup = new MenuGroup(20L, "치킨");

    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        강정치킨 = new Product(1L,"강정치킨",new BigDecimal(20000));
        양념치킨 = new Product(2L,"양념치킨",new BigDecimal(20000));

        상품리스트요청.add(new MenuProductRequest(강정치킨.getId(), 1L));
        상품리스트요청.add(new MenuProductRequest(양념치킨.getId(), 1L));
        상품리스트.add(강정치킨);
        상품리스트.add(양념치킨);
        메뉴상품리스트.add(new MenuProduct(강정치킨, 1L));
        메뉴상품리스트.add(new MenuProduct(양념치킨, 1L));
        강정치킨과양념치킨 = new MenuRequest("강정치킨", new BigDecimal(17000), menuGroup.getId(), 상품리스트요청);
        강정치킨과양념치킨메뉴 = new Menu(강정치킨과양념치킨.getName(), 강정치킨과양념치킨.getPrice(), menuGroup, new MenuProducts(메뉴상품리스트));
        메뉴리스트.add(강정치킨과양념치킨메뉴);
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);
    }

    @DisplayName("메뉴 등록 테스트")
    @Test
    void createMenu() {
        when(menuGroupRepository.findById(menuGroup.getId())).thenReturn(Optional.ofNullable(menuGroup));
        when(menuRepository.save(any())).thenReturn(강정치킨과양념치킨메뉴);
        when(productRepository.findAllById(any())).thenReturn(상품리스트);

        MenuResponse resultMenu = menuService.create(강정치킨과양념치킨);

        assertAll(
                () -> assertThat(resultMenu.getName()).isEqualTo("강정치킨"),
                () -> assertThat(resultMenu.getPrice()).isEqualTo(new BigDecimal(17000)),
                () -> assertThat(resultMenu.getMenuProducts().size()).isEqualTo(2),
                () -> assertThat(resultMenu.getMenuProducts().get(0).getProductResponse().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 등록 예외 테스트: 메뉴값 null")
    @Test
    void nullMenu() {
        assertThatThrownBy(() -> menuService.create(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("메뉴 등록 예외 테스트: 가격정보 null or 음수")
    @Test
    void invaildPrice() {
        when(menuGroupRepository.findById(20L)).thenReturn(Optional.ofNullable(menuGroup));
        when(productRepository.findAllById(any())).thenReturn(상품리스트);

        강정치킨과양념치킨 = new MenuRequest("강정치킨", null, menuGroup.getId(), 상품리스트요청);
        assertThatThrownBy(() -> menuService.create(강정치킨과양념치킨))
                .isInstanceOf(IllegalArgumentException.class);

        강정치킨과양념치킨 = new MenuRequest("강정치킨", new BigDecimal(-17000), menuGroup.getId(), 상품리스트요청);
        assertThatThrownBy(() -> menuService.create(강정치킨과양념치킨))
                .isInstanceOf(IllegalArgumentException.class);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(강정치킨과양념치킨)
        );
        assertThat(exception.getMessage()).isEqualTo("입력된 가격이 올바르지 않습니다.");
    }

    @DisplayName("메뉴 등록 예외 테스트: 메뉴그룹값 올바르지 않을때")
    @Test
    void invalidMenuGroup() {
        assertThatThrownBy(() -> menuService.create(강정치킨과양념치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 예외 테스트: 메뉴 가격이 잘못된 경우")
    @Test
    void invalidTotalPrice() {
        when(menuGroupRepository.findById(20L)).thenReturn(Optional.ofNullable(menuGroup));
        when(productRepository.findAllById(any())).thenReturn(상품리스트);

        강정치킨과양념치킨 = new MenuRequest("강정치킨", new BigDecimal(5000000), menuGroup.getId(), 상품리스트요청);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(강정치킨과양념치킨)
        );
        assertThat(exception.getMessage()).isEqualTo("상품가격 총합과 메뉴의 가격이 올바르지 않습니다.");
    }

    @DisplayName("메뉴목록 조회 테스트")
    @Test
    void findMenuList() {
        when(menuRepository.findAll()).thenReturn(메뉴리스트);

        List<MenuResponse> resultMenus = menuService.list();

        List<String> menuNames =resultMenus.stream()
                .map(menu -> menu.getName())
                .collect(Collectors.toList());

        assertThat(resultMenus.size()).isEqualTo(1);
        assertThat(menuNames).contains("강정치킨");
   }

}
