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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private Product 후라이드;
    private MenuGroup 두마리메뉴;
    private Menu 후라이드치킨;
    private MenuProduct 메뉴상품_후라이드;
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
    }

    @DisplayName("메뉴를 등록한다")
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

}