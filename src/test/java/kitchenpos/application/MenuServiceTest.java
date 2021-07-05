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

import static org.assertj.core.api.Assertions.*;
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

    private MenuGroup 추천메뉴;
    private MenuProduct 강정치킨양두배;
    private Menu 강정치킨plus강정치킨;
    private Product 강정치킨;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        강정치킨양두배 = new MenuProduct(1L, 1L, 1L, 2);
        강정치킨plus강정치킨 = new Menu(1L, "강정치킨+강정치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(강정치킨양두배));
        강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(menuGroupDao.existsById(강정치킨plus강정치킨.getMenuGroupId())).willReturn(true);
        given(productDao.findById(강정치킨양두배.getProductId())).willReturn(Optional.of(강정치킨));
        given(menuDao.save(강정치킨plus강정치킨)).willReturn(강정치킨plus강정치킨);
        given(menuProductDao.save(강정치킨양두배)).willReturn(강정치킨양두배);

        // when
        Menu createdMenu = menuService.create(강정치킨plus강정치킨);

        // then
        assertThat(createdMenu.getId()).isEqualTo(강정치킨plus강정치킨.getId());
        assertThat(createdMenu.getName()).isEqualTo(강정치킨plus강정치킨.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(강정치킨plus강정치킨.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(강정치킨plus강정치킨.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts()).isEqualTo(강정치킨plus강정치킨.getMenuProducts());
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다 : 메뉴의 가격은 0 원 이상이어야 한다.")
    @Test
    void createTest_wrongPrice() {
        // given
        강정치킨plus강정치킨.setPrice(BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> menuService.create(강정치킨plus강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다 : 메뉴의 가격은 메뉴 상품 목록 가격의 합보다 작거나 같아야 한다.")
    @Test
    void createTest_wrongPrice2() {
        // given
        강정치킨plus강정치킨.setPrice(BigDecimal.valueOf(34001));
        given(menuGroupDao.existsById(강정치킨plus강정치킨.getMenuGroupId())).willReturn(true);
        given(productDao.findById(강정치킨양두배.getProductId())).willReturn(Optional.of(강정치킨));

        // when & then
        assertThatThrownBy(() -> menuService.create(강정치킨plus강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴그룹이 올바르지 않으면 등록할 수 없다 : 메뉴의 메뉴 그룹은 등록된 메뉴 그룹이어야 한다.")
    @Test
    void createTest_unregisteredMenuGroup() {
        // given
        강정치킨plus강정치킨.setMenuGroupId(100L);

        // when & then
        assertThatThrownBy(() -> menuService.create(강정치킨plus강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴 상품 목록이 올바르지 않으면 등록할 수 없다 : 메뉴의 메뉴 상품 목록의 상품은 등록된 상품이어야 한다.")
    @Test
    void createTest_unregisteredMenuProduct() {
        // given
        강정치킨양두배.setProductId(100L);

        // when & then
        assertThatThrownBy(() -> menuService.create(강정치킨plus강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(강정치킨plus강정치킨));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(강정치킨plus강정치킨);
    }
}
