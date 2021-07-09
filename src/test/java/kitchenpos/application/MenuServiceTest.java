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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private Product 후라이드;
    private Product 양념;
    private MenuGroup 두마리메뉴;
    private Menu 후라이드치킨;
    private Menu 양념치킨;
    private MenuProduct 메뉴상품_후라이드;
    private MenuProduct 메뉴상품_양념;
    private List<MenuProduct> 메뉴상품목록;

    @Mock
    private ProductDao productDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private ProductService productService;
    @InjectMocks
    private MenuGroupService menuGroupService;
    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setName("후라이드");
        후라이드.setPrice(BigDecimal.valueOf(16000));

        두마리메뉴 = new MenuGroup();
        두마리메뉴.setId(1L);
        두마리메뉴.setName("두마리메뉴");

        후라이드치킨 = new Menu();
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(BigDecimal.valueOf(16000));
        후라이드치킨.setMenuGroupId(두마리메뉴.getId());

        메뉴상품_후라이드 = new MenuProduct();
        메뉴상품_후라이드.setMenuId(후라이드치킨.getId());
        메뉴상품_후라이드.setProductId(후라이드.getId());
        메뉴상품_후라이드.setQuantity(1L);

        메뉴상품목록 = new ArrayList<>();
        메뉴상품목록.add(메뉴상품_후라이드);

        후라이드치킨.setMenuProducts(메뉴상품목록);

        양념 = new Product();
        양념.setId(2L);
        양념.setName("양념");
        양념.setPrice(BigDecimal.valueOf(16000));

        양념치킨 = new Menu();
        양념치킨.setId(2L);
        양념치킨.setName("양념치킨");
        양념치킨.setPrice(BigDecimal.valueOf(16000));
        양념치킨.setMenuGroupId(두마리메뉴.getId());

        메뉴상품_양념 = new MenuProduct();
        메뉴상품_양념.setMenuId(양념치킨.getId());
        메뉴상품_양념.setProductId(양념.getId());
        메뉴상품_양념.setQuantity(1L);

        메뉴상품목록 = new ArrayList<>();
        메뉴상품목록.add(메뉴상품_양념);

        양념치킨.setMenuProducts(메뉴상품목록);
    }

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        // given
        given(menuDao.save(후라이드치킨)).willReturn(후라이드치킨);
        given(menuGroupDao.existsById(두마리메뉴.getId())).willReturn(true);
        given(productDao.findById(메뉴상품_후라이드.getProductId())).willReturn(Optional.of(후라이드));
        given(menuProductDao.save(메뉴상품_후라이드)).willReturn(메뉴상품_후라이드);

        // when
        Menu expected = menuService.create(후라이드치킨);

        // then
        assertThat(expected).isEqualTo(후라이드치킨);
    }

    @DisplayName("메뉴 등록 - 가격은 0 이상의 숫자를 입력해야 한다")
    @Test
    void create_invalidPrice() {
        // given
        후라이드치킨.setPrice(BigDecimal.valueOf(-1));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드치킨));
    }

    @DisplayName("메뉴 등록 - 메뉴그룹은 필수 입력")
    @Test
    void create_menuGroupIsEssential() {
        // given
        후라이드치킨.setMenuGroupId(null);

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드치킨));
    }

    @DisplayName("메뉴 등록 - 메뉴상품은 필수 입력")
    @Test
    void create_menuProductIsEssential() {
        // given
        후라이드치킨.setMenuProducts(Collections.emptyList());

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드치킨));
    }

    @DisplayName("메뉴 등록 - 메뉴 가격은 메뉴상품 가격의 총합보다 클 수 없다")
    @Test
    void create_menuPriceIsGreaterThanMenuProducts() {
        // given
        후라이드치킨.setPrice(BigDecimal.valueOf(17000));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드치킨));
    }

    @DisplayName("등록한 메뉴목록을 조회한다.")
    @Test
    void findAll() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));

        // when
        List<Menu> expected = menuService.list();

        // then
        assertThat(expected).isEqualTo(Arrays.asList(후라이드치킨, 양념치킨));
    }

}