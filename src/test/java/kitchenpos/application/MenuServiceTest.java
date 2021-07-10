//package kitchenpos.application;
//
//import kitchenpos.application.menu.MenuService;
//import kitchenpos.domain.menu.Menu;
//import kitchenpos.domain.menu.MenuGroupRepository;
//import kitchenpos.domain.menu.MenuRepository;
//import kitchenpos.domain.product.Product;
//import kitchenpos.domain.product.ProductRepository;
//import kitchenpos.ui.dto.menu.MenuProductRequest;
//import kitchenpos.ui.dto.menu.MenuRequest;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@DisplayName("메뉴 관리")
//@ExtendWith(MockitoExtension.class)
//class MenuServiceTest {
//    private MenuService menuService;
//
//    @Mock
//    private MenuRepository menuRepository;
//    @Mock
//    private MenuGroupRepository menuGroupRepository;
//    @Mock
//    private ProductRepository productRepository;
//
//    private Product 후라이드치킨;
//    private Product 양념치킨;
//    private Product 족발;
//    private MenuProductRequest 후라이드치킨_세트메뉴;
//    private MenuProductRequest 양념치킨_세트메뉴;
//    private MenuProductRequest 족발_세트메뉴;
//
//    @BeforeEach
//    void setUp() {
//        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);
//        후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(10000));
//        후라이드치킨_세트메뉴 = MenuProductRequest.of(1L, 3);
//        양념치킨 = Product.of(2L, "양념치킨", BigDecimal.valueOf(11000));
//        양념치킨_세트메뉴 = MenuProductRequest.of(2L, 4);
//        족발 = Product.of(3L, "족발", BigDecimal.valueOf(15000));
//        족발_세트메뉴 = MenuProductRequest.of(3L, 5);
//    }
//
//    @DisplayName("메뉴를 추가한다.")
//    @Test
//    void create() {
//        //given
//        MenuRequest menuRequest = MenuRequest.of(
//                "10만원의 행복 파티 세트 (12인)",
//                BigDecimal.valueOf(100000),
//                1L,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
//
//        //and
//        given(productRepository.findById(any())).willReturn(Optional.of(후라이드치킨));
//        given(menuRepository.save(menu)).willReturn(menu);
//
//        //when
//        MenuResponse actual = menuService.create(menuRequest);
//
//        //then
//        assertThat(actual).isEqualTo(menu);
//    }
//
//    @DisplayName("메뉴의 이름을 지정해야한다.")
//    @Test
//    void createMenuExceptionIfNameIsNull() {
//        //TODO: 추가 기능 개발
//    }
//
//    @DisplayName("메뉴의 총 가격은 0원 이상이어야한다.")
//    @Test
//    void createMenuExceptionIfPriceIsNull() {
//        //given
//        Menu menu = Menu.of(
//                1L,
//                "10만원의 행복 파티 세트 (12인)",
//                BigDecimal.valueOf(-1000),
//                1L,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
//
//        //when
//        assertThatThrownBy(() -> menuService.create(menu))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("메뉴는 속할 메뉴 그룹을 지정해야한다.")
//    @Test
//    void createMenuExceptionIfMenuGroupIsNull() {
//        //given
//        Menu menu = Menu.of(
//                1L,
//                "10만원의 행복 파티 세트 (12인)",
//                BigDecimal.valueOf(100000),
//                null,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
//
//        //and
//        given(menuGroupDao.existsById(any())).willReturn(false);
//
//        //when
//        assertThatThrownBy(() -> menuService.create(menu))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("메뉴는 존재하는 상품으로 구성해야한다.")
//    @Test
//    void createMenuExceptionIfMenuProductIsNotExist() {
//        //given
//        Menu menu = Menu.of(
//                1L,
//                "10만원의 행복 파티 세트 (12인)",
//                BigDecimal.valueOf(100000),
//                1L,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
//
//        //and
//        given(menuGroupDao.existsById(any())).willReturn(true);
//        given(productDao.findById(any())).willReturn(Optional.empty());
//
//        //when
//        assertThatThrownBy(() -> menuService.create(menu))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("메뉴의 메뉴상품은 갯수를 지정해야한다.")
//    @Test
//    void createMenuExceptionIfMenuProductQuantityIsNull() {
//        //TODO: 추가 기능 개발
//    }
//
//    @DisplayName("메뉴의 총 가격이 기존 상품들의 총합 가격보다 비쌀 수 없다.")
//    @Test
//    void createMenuExceptionIfMenuPriceHigherThanProductTotalPrice() {
//        //given
//        Menu menu = Menu.of(
//                1L,
//                "10만원의 행복 파티 세트 (12인)",
//                BigDecimal.valueOf(200000),
//                1L,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
//
//        //and
//        given(menuGroupDao.existsById(any())).willReturn(true);
//        given(productDao.findById(any())).willReturn(Optional.of(후라이드치킨));
//
//        //when
//        assertThatThrownBy(() -> menuService.create(menu))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("메뉴 그룹을 모두 조회한다.")
//    @Test
//    void list() {
//        //given
//        Menu menu1 = Menu.of(
//                1L,
//                "10만원의 행복 파티 세트 (7인)",
//                BigDecimal.valueOf(60000),
//                1L,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴));
//        Menu menu2 = Menu.of(
//                2L,
//                "10만원의 행복 파티 세트2 (12인)",
//                BigDecimal.valueOf(100000),
//                1L,
//                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
//        List<Menu> menus = Lists.list(menu1, menu2);
//
//        //and
//        given(menuDao.findAll()).willReturn(menus);
//
//        //when
//        List<Menu> actual = menuService.list();
//
//        //then
//        assertThat(actual).isEqualTo(menus);
//    }
//}
