package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 비즈니스 기능 테스트")
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

    private Product 삼겹살;
    private Product 김치;
    private MenuGroup 한식;
    private Menu 삼겹살세트메뉴;
    private MenuProduct 삼겹살메뉴상품;
    private MenuProduct 김치메뉴상품;


    @BeforeEach
    void setUp() {
        삼겹살 = new Product(1L, "삼겹살", BigDecimal.valueOf(5_000));
        김치 = new Product(2L, "김치", BigDecimal.valueOf(3_000));
        한식 = new MenuGroup(1L, "한식");
        삼겹살세트메뉴 = new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(8_000), 한식);
        삼겹살메뉴상품 = new MenuProduct(1L, 삼겹살세트메뉴, 삼겹살, 1L);
        김치메뉴상품 = new MenuProduct(2L, 삼겹살세트메뉴, 김치, 1L);
        삼겹살세트메뉴.getMenuProducts().setMenuProducts(Arrays.asList(삼겹살메뉴상품, 김치메뉴상품));
    }

    @DisplayName("메뉴생성 테스트")
    @Test
    void createMenuTest() {
        // given
        settingMockInfoForCreateMenu();

        // when
        Menu result = menuService.create(삼겹살세트메뉴);

        // then
        checkForCreteMenu(result);
    }

    private void settingMockInfoForCreateMenu() {
        when(menuGroupRepository.existsById(삼겹살세트메뉴.getMenuGroup().getId())).thenReturn(true);
        when(productRepository.findById(삼겹살메뉴상품.getProduct().getId())).thenReturn(Optional.of(삼겹살));
        when(productRepository.findById(김치메뉴상품.getProduct().getId())).thenReturn(Optional.of(김치));
        when(menuRepository.save(삼겹살세트메뉴)).thenReturn(삼겹살세트메뉴);
        when(menuProductRepository.save(삼겹살메뉴상품)).thenReturn(삼겹살메뉴상품);
        when(menuProductRepository.save(김치메뉴상품)).thenReturn(김치메뉴상품);
    }

    private void checkForCreteMenu(Menu result) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(삼겹살세트메뉴.getId()),
                () -> assertThat(result.getName()).isEqualTo(삼겹살세트메뉴.getName())
        );
    }

    @DisplayName("메뉴생성 테스트 - 올바르지 않는 가격인경우")
    @ParameterizedTest
    @ValueSource(ints = { -1, -1000 })
    void createMenuTest2(int price) {
        // when & then
        assertThatThrownBy(() -> new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(price), 한식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴생성 테스트 - 가격이 null인 경우")
    @Test
    void createMenuTest2() {
        // when & then
        assertThatThrownBy(() -> new Menu(1L, "삼겹살세트메뉴", null, 한식))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴생성 테스트 - 메뉴 그룹이 없는 경우")
    @Test
    void createMenuTest3() {
        // given
        삼겹살세트메뉴 = new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(8_000), 한식);

        // when & then
        assertThatThrownBy(() -> menuService.create(삼겹살세트메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴생성 테스트 - 상품이 존재하지 않은 경우")
    @Test
    void createMenuTest4() {
        // given
        삼겹살세트메뉴 = new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(8_000), 한식);
        when(menuGroupRepository.existsById(삼겹살세트메뉴.getMenuGroup().getId())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> menuService.create(삼겹살세트메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴생성 테스트 - 메뉴의 가격은 모든 상품 가격의 합보다 작은 경우")
    @Test
    void createMenuTest5() {
        // given
        삼겹살세트메뉴.setPrice(BigDecimal.valueOf(10_000));
        when(menuGroupRepository.existsById(삼겹살세트메뉴.getMenuGroup().getId())).thenReturn(true);
        when(productRepository.findById(김치메뉴상품.getProduct().getId())).thenReturn(Optional.of(김치));
        when(productRepository.findById(삼겹살메뉴상품.getProduct().getId())).thenReturn(Optional.of(삼겹살));

        // when & then
        assertThatThrownBy(() -> menuService.create(삼겹살세트메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴조회 테스트")
    @Test
    void findAllMenuTest() {
        // given
        List<Menu> menus = Arrays.asList(삼겹살세트메뉴);
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(1)
                .containsExactly(삼겹살세트메뉴);
    }

}