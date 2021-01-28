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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;

    @BeforeEach
    public void setup() {
        후라이드치킨 = new Product();
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드");
        후라이드치킨.setPrice(BigDecimal.valueOf(10000));

        양념치킨 = new Product();
        양념치킨.setId(2L);
        양념치킨.setName("양념");
        양념치킨.setPrice(BigDecimal.valueOf(110000));

        치킨세트 = new MenuGroup();
        치킨세트.setId(1L);
        치킨세트.setName("후라이드앙념치킨");

        후라이드한마리양념치킨한마리 = new Menu();
        후라이드한마리양념치킨한마리.setId(1L);
        후라이드한마리양념치킨한마리.setName("후라이드+양념");
        후라이드한마리양념치킨한마리.setMenuGroupId(치킨세트.getId());
        후라이드한마리양념치킨한마리.setPrice(BigDecimal.valueOf(21000));

        후라이드치킨한마리 = new MenuProduct();
        후라이드치킨한마리.setProductId(후라이드치킨.getId());
        후라이드치킨한마리.setQuantity(1);

        양념치킨한마리 = new MenuProduct();
        양념치킨한마리.setProductId(양념치킨.getId());
        양념치킨한마리.setQuantity(1);

        후라이드한마리양념치킨한마리.setMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));
    }

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
        given(menuGroupDao.existsById(치킨세트.getId())).willReturn(true);
        given(productDao.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        given(menuDao.save(후라이드한마리양념치킨한마리)).willReturn(후라이드한마리양념치킨한마리);
        given(menuProductDao.save(후라이드치킨한마리)).willReturn(후라이드치킨한마리);
        given(menuProductDao.save(양념치킨한마리)).willReturn(양념치킨한마리);

        Menu savedMenu = menuService.create(후라이드한마리양념치킨한마리);

        assertThat(savedMenu).isEqualTo(후라이드한마리양념치킨한마리);
    }


}
