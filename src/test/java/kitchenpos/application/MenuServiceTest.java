package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.infra.ProductRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.MenuGroupServiceTest.getMenuGroup;
import static kitchenpos.application.ProductServiceTest.getProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    private Product 양지쌀국수;
    private Product 분짜;

    @BeforeEach
    void setUp() {
        양지쌀국수 = getProduct(1L, "양지쌀국수", 7_500);
        분짜 = getProduct(2L, "분짜", 9_500);
    }

    @DisplayName("메뉴의 이름, 가격과 메뉴 그룹의 아이디, 메뉴상품그룹을 통해 메뉴를 생성할 수 있다.")
    @Test
    void create() {
        // given
        Menu createRequest = getCreateRequest(
                "대표메뉴",
                17_000,
                getMenuGroup(1L, "쌀국수류"),
                Arrays.asList(
                        getMenuProduct(1L, 양지쌀국수, 10),
                        getMenuProduct(2L, 분짜, 6)
                )
        );
        Menu expected = getMenu(1L, createRequest);

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productRepository.findById(분짜.getId())).willReturn(Optional.of(분짜));
        given(productRepository.findById(양지쌀국수.getId())).willReturn(Optional.of(양지쌀국수));
        given(menuDao.save(any(Menu.class))).willReturn(expected);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(new MenuProduct());

        // when
        Menu actual = menuService.create(createRequest);
        // then
        assertThat(actual).isEqualTo(expected);
    }


    @DisplayName("메뉴를 생성할 수 없는 경우")
    @Nested
    class CreateFailTest {

        @DisplayName("메뉴 가격은 0보다 작을 경우")
        @Test
        void createByZeroMoreLessPrice() {
            // given
            Menu createRequest = getCreateRequest(
                    "대표메뉴",
                    -10,
                    getMenuGroup(1L, "쌀국수류"),
                    Arrays.asList(
                            getMenuProduct(1L, 양지쌀국수, 10),
                            getMenuProduct(2L, 분짜, 6)
                    )
            );
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹의 아이디에 따른 메뉴 그룹이 존재하지 않을 경우")
        @Test
        void createByNotExistMenuGroup() {
            // given
            Menu createRequest = getCreateRequest(
                    "대표메뉴",
                    19_000,
                    getMenuGroup(1L, "쌀국수류"),
                    Arrays.asList(
                            getMenuProduct(1L, 양지쌀국수, 10),
                            getMenuProduct(2L, 분짜, 6)
                    )
            );
            given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(false);
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴상품그룹의 메뉴 아이디에 따른 메뉴 상품의 상품이 존재하지 않을 경우")
        @Test
        void createByNotExistProduct() {
            // given
            Menu createRequest = getCreateRequest(
                    "대표메뉴",
                    19_000,
                    getMenuGroup(1L, "쌀국수류"),
                    Arrays.asList(
                            getMenuProduct(1L, 양지쌀국수, 10),
                            getMenuProduct(2L, 분짜, 6)
                    )
            );
            given(menuGroupDao.existsById(any(Long.TYPE))).willReturn(false);
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격은 포함된 상품들의 금액의 합 보다 클 경우")
        @Test
        void createByIllegalPrice() {
            // given
            final MenuProduct 메뉴로_등록된_양지쌀국수 = getMenuProduct(1L, 양지쌀국수, 10);
            final MenuProduct 메뉴로_등록된_분짜 = getMenuProduct(2L, 분짜, 6);
            final BigDecimal 포함된_상품들의_총_가격보다_1큰_가격 = 양지쌀국수.getPrice().multiply(BigDecimal.valueOf(메뉴로_등록된_양지쌀국수.getQuantity()))
                    .add(분짜.getPrice().multiply(BigDecimal.valueOf(메뉴로_등록된_분짜.getQuantity())))
                    .add(BigDecimal.ONE);

            Menu createRequest = getCreateRequest(
                    "대표메뉴",
                    포함된_상품들의_총_가격보다_1큰_가격,
                    getMenuGroup(1L, "쌀국수류"),
                    Arrays.asList(
                            메뉴로_등록된_양지쌀국수,
                            메뉴로_등록된_분짜
                    )
            );

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productRepository.findById(분짜.getId())).willReturn(Optional.of(분짜));
            given(productRepository.findById(양지쌀국수.getId())).willReturn(Optional.of(양지쌀국수));
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> menuService.create(createRequest);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴의 목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        Menu 대표메뉴 = getMenu(1L, "대표메뉴",
                17_000,
                getMenuGroup(1L, "쌀국수류"),
                Arrays.asList(
                        getMenuProduct(1L, 양지쌀국수, 10),
                        getMenuProduct(2L, 분짜, 6)
                ));

        Menu 추천메뉴 = getMenu(1L, "추천메뉴",
                17_000,
                getMenuGroup(1L, "쌀국수류"),
                Arrays.asList(
                        getMenuProduct(1L, 양지쌀국수, 10),
                        getMenuProduct(2L, 분짜, 6)
                ));

        final List<Menu> expected = Arrays.asList(대표메뉴, 추천메뉴);
        given(menuDao.findAll()).willReturn(expected);
        // when
        List<Menu> list = menuService.list();
        // then
        assertThat(list).containsExactlyElementsOf(expected);

    }


    public static MenuProduct getMenuProduct(long id, Product product, int quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setSeq(id);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Menu getMenu(long id, Menu createRequest) {
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(createRequest.getName());
        menu.setMenuProducts(createRequest.getMenuProducts());
        menu.setMenuGroupId(createRequest.getMenuGroupId());
        menu.setPrice(createRequest.getPrice());
        return menu;
    }

    public static Menu getMenu(long id, String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setMenuProducts(menuProducts);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(BigDecimal.valueOf(price));
        return menu;
    }

    private Menu getCreateRequest(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setMenuProducts(menuProducts);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(price);
        return menu;
    }

    private Menu getCreateRequest(String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return getCreateRequest(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }
}
