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

import static kitchenpos.application.MenuGroupServiceTest.메뉴_그룹_등록;
import static kitchenpos.application.ProductServiceTest.상품_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관련 기능")
public class MenuServiceTest {
    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    ProductDao productDao;

    @Mock
    MenuProductDao menuProductDao;

    @InjectMocks
    MenuService menuService;

    private Product 짜장면;
    private Product 탕수육;
    private MenuGroup 중국음식;
    private Menu 짜장면메뉴;

    @BeforeEach
    void setUp() {
        짜장면 = 상품_등록(1L, "짜장면", 5000);
        탕수육 = 상품_등록(2L, "탕수육", 15000);
        중국음식 = 메뉴_그룹_등록(1L, "중국음식");
        짜장면메뉴 = 메뉴_등록(1L, "짜장면탕수육세트", 짜장면.getPrice().add(탕수육.getPrice()), 중국음식.getId(), Arrays.asList(메뉴_상품_등록(1L, 짜장면.getId(), 1L), 메뉴_상품_등록(2L, 탕수육.getId(), 1L)));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(짜장면.getId())).willReturn(Optional.of(짜장면));
        given(productDao.findById(탕수육.getId())).willReturn(Optional.of(탕수육));
        given(menuDao.save(any())).willReturn(짜장면메뉴);

        // when
        Menu createMenu = menuService.create(짜장면메뉴);

        // then
        assertThat(createMenu).isNotNull();
    }

    static Menu 메뉴_등록(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    static MenuProduct 메뉴_상품_등록(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
