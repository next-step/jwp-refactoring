package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.product.application.ProductServiceTest;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
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

    private MenuGroup 파스타메뉴;
    private Product 봉골레파스타;
    private Product 감자튀김;
    private Menu 봉골레파스타세트;

    @BeforeEach
    void setUp() {
        파스타메뉴 = MenuGroupServiceTest.메뉴_그룹_생성(1L, "파스타메뉴");
        봉골레파스타 = ProductServiceTest.상품_생성(1L, "봉골레파스타", 13000);
        감자튀김 = ProductServiceTest.상품_생성(2L, "감자튀김", 5000);
        봉골레파스타세트 = 메뉴_생성(1L, "봉골레파스타세트", BigDecimal.valueOf(15000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(봉골레파스타.getId())).willReturn(Optional.of(봉골레파스타));
        given(productDao.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(menuDao.save(any())).willReturn(봉골레파스타세트);

        //when
        Menu createdMenu = menuService.create(봉골레파스타세트);

        //then
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getId()).isEqualTo(봉골레파스타세트.getId());
    }

    @DisplayName("메뉴 가격이 0보다 작은 경우, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidPrice() {
        //given
        봉골레파스타세트.setPrice(BigDecimal.valueOf(-1));

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 상품이 포함되면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidNotExistsMenuProduct() {
        //given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(봉골레파스타.getId())).willReturn(Optional.ofNullable(null));

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 내 제품가격의 합보다 메뉴 가격이 크면, 메뉴 등록에 실패한다.")
    @Test
    void create_invalidPriceSum() {
        //given
        봉골레파스타세트.setPrice(BigDecimal.valueOf(20000));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(봉골레파스타.getId())).willReturn(Optional.of(봉골레파스타));
        given(productDao.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));

        //when & then
        assertThatThrownBy(() -> menuService.create(봉골레파스타세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(봉골레파스타세트));
        given(menuProductDao.findAllByMenuId(any())).willReturn(파스타메뉴_상품_생성());

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).hasSize(1);
        assertThat(menus).containsExactly(봉골레파스타세트);
    }

    public static Menu 메뉴_생성(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct 메뉴_상품_생성(Long id, Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(id);
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    private List<MenuProduct> 파스타메뉴_상품_생성() {
        return Arrays.asList(
                메뉴_상품_생성(1L, 0L, 봉골레파스타.getId(), 1L),
                메뉴_상품_생성(2L, 1L, 감자튀김.getId(), 1L)
        );
    }
}
