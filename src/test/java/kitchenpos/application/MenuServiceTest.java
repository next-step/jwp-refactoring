package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("후라이드");
        product1.setPrice(BigDecimal.valueOf(10000));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("양념");
        product2.setPrice(BigDecimal.valueOf(110000));

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("후라이드앙념치킨");

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+양념");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(BigDecimal.valueOf(21000));

        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(1);

        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(1);

        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        given(menuGroupDao.existsById(menuGroup.getId())).willReturn(true);
        given(productDao.findById(product1.getId())).willReturn(Optional.of(product1));
        given(productDao.findById(product2.getId())).willReturn(Optional.of(product2));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu).isEqualTo(menu);

    }

}
