package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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

import static kitchenpos.application.ProductServiceTest.상품_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스 테스트")
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

    @DisplayName("메뉴 등록")
    @Test
    public void 메뉴_등록_확인() throws Exception {
        //given
        Product 후라이드반 = 상품_등록됨(1L, "후라이드반", BigDecimal.valueOf(8_000));
        Product 양념반 = 상품_등록됨(2L, "양념반", BigDecimal.valueOf(8_000));
        MenuProduct menuProduct1 = 메뉴상품_등록됨(1L, 1L, 1L, 1L);
        MenuProduct menuProduct2 = 메뉴상품_등록됨(1L, 2L, 1L, 2L);
        Menu menu = 메뉴_등록됨(1L, "반반치킨", BigDecimal.valueOf(15_000), 1L);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(후라이드반));
        given(productDao.findById(2L)).willReturn(Optional.of(양념반));
        given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);
        Menu createMenu = 메뉴_생성("반반치킨", BigDecimal.valueOf(15_000), 1L);
        createMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        given(menuDao.save(createMenu)).willReturn(menu);

        //when
        Menu saveMenu = menuService.create(createMenu);

        //then
        assertThat(saveMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    public void 메뉴_목록조회_확인() throws Exception {
        //given
        Menu menu1 = 메뉴_등록됨(1L, "메뉴1", BigDecimal.valueOf(11_000), 1L);
        Menu menu2 = 메뉴_등록됨(2L, "메뉴2", BigDecimal.valueOf(12_000), 2L);
        Menu menu3 = 메뉴_등록됨(3L, "메뉴3", BigDecimal.valueOf(13_000), 3L);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu1, menu2, menu3));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus.size()).isEqualTo(3);
    }

    public Menu 메뉴_생성(String name, BigDecimal price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public Menu 메뉴_등록됨(Long id, String name, BigDecimal price, Long menuGroupId) {
        Menu menu = 메뉴_생성(name, price, menuGroupId);
        menu.setId(id);
        return menu;
    }

    public MenuProduct 메뉴상품_등록됨(Long menuId, Long productId, Long quantity, Long seq) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menuProduct.setSeq(seq);
        return menuProduct;
    }
}
