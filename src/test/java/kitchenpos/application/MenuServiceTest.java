package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @InjectMocks
    MenuService menuService;

    private Product 강정치킨;
    private MenuGroup 치킨메뉴;
    private Menu 추천메뉴;

    @BeforeEach
    void setUp() {
        강정치킨 = 상품_등록(1L, "강정치킨", 17000);
        치킨메뉴 = 메뉴_그룹_등록(1L, "치킨메뉴");
        추천메뉴 = 메뉴_등록(1L, "추천메뉴", 강정치킨.getPrice(), 치킨메뉴.getId(),
                Arrays.asList(메뉴_상품_등록(1L, 강정치킨.getId(), 1L)));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(강정치킨));
        given(menuDao.save(any())).willReturn(추천메뉴);

        // when
        Menu createMenu = menuService.create(추천메뉴);

        // then
        assertThat(createMenu).isNotNull();
    }

    static Menu 메뉴_등록(Long id, String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
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
