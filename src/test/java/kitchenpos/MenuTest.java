package kitchenpos;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuTest {

    @Mock
    private MenuDao menuDao;
    //@Mock
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    //@Mock
    @Mock
    private ProductDao productDao;


    @Test
    public void 메뉴생성_성공() {
        //given
        //Menu 데이터 정의
        Product product = new Product();
        product.setId(1L);
        product.setName("강정치킨");
        product.setPrice(new BigDecimal(17000));

        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuGroupId(1L);
        List<MenuProduct> menuProductList = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);
        menuProductList.add(menuProduct);
        menu.setMenuProducts(menuProductList);

        //given-willReturn 정의
        given(menuGroupDao.existsById(anyLong())).willReturn(Boolean.TRUE);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

        //when-then
        // create 메소드 호출
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        assertThat(menuService.create(menu).getName()).isEqualTo("후라이드+후라이드");
    }
}
