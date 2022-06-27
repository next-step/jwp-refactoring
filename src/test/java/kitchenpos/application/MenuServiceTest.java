package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.factory.fixture.MenuFixtureFactory.createMenu;
import static kitchenpos.factory.fixture.MenuGroupFixtureFactory.createMenuGroup;
import static kitchenpos.factory.fixture.MenuProductFixtureFactory.createMenuProduct;
import static kitchenpos.factory.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    Long 양념_치킨_ID;
    Long 치킨_그룹_ID;

    private Product 콜라;
    private Product 닭;
    private MenuProduct 양념_치킨_콜라;
    private MenuProduct 양념_치킨_닭;
    private MenuGroup 치킨_그룹;
    private Menu 양념_치킨;

    @BeforeEach
    void setUp() {
        양념_치킨_ID = 1L;
        치킨_그룹_ID = 1L;

        콜라 = createProduct(1L, "밀가루", BigDecimal.valueOf(5000L));
        닭 = createProduct(2L, "닭", BigDecimal.valueOf(10000L));
        양념_치킨_콜라 = createMenuProduct(1L, 양념_치킨_ID, 콜라.getId(), 2);
        양념_치킨_닭 = createMenuProduct(2L, 양념_치킨_ID, 닭.getId(), 1);
        치킨_그룹 = createMenuGroup(치킨_그룹_ID, "치킨");
        양념_치킨 = createMenu(양념_치킨_ID, "양념치킨", BigDecimal.valueOf(20000L), 치킨_그룹.getId(), Arrays.asList(양념_치킨_콜라, 양념_치킨_닭));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        given(menuGroupDao.existsById(치킨_그룹.getId())).willReturn(Boolean.TRUE);
        given(productDao.findById(양념_치킨_콜라.getProductId())).willReturn(Optional.of(콜라));
        given(productDao.findById(양념_치킨_닭.getProductId())).willReturn(Optional.of(닭));
        given(menuDao.save(any(Menu.class))).willReturn(양념_치킨);

        Menu menu = menuService.create(양념_치킨);

        assertThat(menu.getName()).isEqualTo(양념_치킨.getName());
    }

    @DisplayName("메뉴의 가격이 0원이면 예외를 던진다.")
    @Test
    void createWithInvalidPrice() {
        MenuGroup menuGroup = createMenuGroup(1000L, "테스트");
        Menu menu = createMenu(1000L, null, BigDecimal.ZERO, menuGroup.getId(), null);

        given(menuGroupDao.existsById(menuGroup.getId())).willReturn(Boolean.FALSE);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹에 등록되지 않았다면 예외를 던진다.")
    @Test
    void createWihInvalidMeuGroupId() {
        MenuGroup menuGroup = createMenuGroup(1000L, "테스트");
        Menu menu = createMenu(1000L, null, BigDecimal.valueOf(1000L), menuGroup.getId(), null);

        given(menuGroupDao.existsById(menuGroup.getId())).willReturn(Boolean.FALSE);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품이 존재하지 않는다면 예외를 던진다.")
    @Test
    void createWihInvalidProductId() {
        given(menuGroupDao.existsById(치킨_그룹.getId())).willReturn(Boolean.TRUE);
        given(productDao.findById(양념_치킨_콜라.getProductId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(양념_치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 금액의 합보다 메뉴 금액이 더 크면 예외를 던진다.")
    @Test
    void createWithInvalidSum() {
        닭.setPrice(BigDecimal.valueOf(10000L));
        콜라.setPrice(BigDecimal.valueOf(4000L));

        given(menuGroupDao.existsById(양념_치킨.getMenuGroupId())).willReturn(true);
        given(productDao.findById(양념_치킨_닭.getProductId())).willReturn(Optional.of(닭));
        given(productDao.findById(양념_치킨_콜라.getProductId())).willReturn(Optional.of(콜라));

        assertThatThrownBy(() -> menuService.create(양념_치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        Menu 양념_치킨 = createMenu(양념_치킨_ID, "양념치킨", BigDecimal.valueOf(20000L), 치킨_그룹.getId(), Arrays.asList(양념_치킨_콜라, 양념_치킨_닭));
        given(menuDao.findAll()).willReturn(Arrays.asList(양념_치킨));
        given(menuProductDao.findAllByMenuId(양념_치킨.getId()))
                .willReturn(Arrays.asList(양념_치킨_콜라, 양념_치킨_닭));

        List<Menu> list = menuService.list();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getName()).isEqualTo("양념치킨");
    }
}
