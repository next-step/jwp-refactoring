package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ServiceTestBase {
    @MockBean
    protected ProductDao productDao;
    protected Product product;
    @MockBean
    protected MenuGroupDao menuGroupDao;
    protected MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        setProduct();
        setMenuGroup();
    }

    private void setProduct() {
        product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(17_000));
        product.setName("양념치킨");

        when(productDao.save(any(Product.class))).thenReturn(product);
        when(productDao.findAll()).thenReturn(Collections.singletonList(product));
    }

    private void setMenuGroup() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("추천메뉴");
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(menuGroup);
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));
    }
}
