package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

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

    Product 스낵랩;
    Product 맥모닝;
    MenuProduct 스낵랩_메뉴_상품;
    MenuProduct 맥모닝_메뉴_상품;
    MenuGroup 패스트푸드류;

    @BeforeEach
    void setUp() {
        스낵랩 = new Product();
        스낵랩.setId(1L);
        스낵랩.setName("스낵랩");
        스낵랩.setPrice(BigDecimal.valueOf(3000));

        맥모닝 = new Product();
        맥모닝.setId(2L);
        맥모닝.setName("맥모닝");
        맥모닝.setPrice(BigDecimal.valueOf(4000));

        스낵랩_메뉴_상품 = new MenuProduct();
        스낵랩_메뉴_상품.setQuantity(1);
        스낵랩_메뉴_상품.setProductId(스낵랩.getId());

        맥모닝_메뉴_상품 = new MenuProduct();
        맥모닝_메뉴_상품.setQuantity(1);
        맥모닝_메뉴_상품.setProductId(맥모닝.getId());

        패스트푸드류 = new MenuGroup();
        패스트푸드류.setId(1L);
        패스트푸드류.setName("패스트푸드");
    }

    @Test
    @DisplayName("메뉴 생성 시도 시 메뉴 가격이 음수 일 경우 에러 반환")
    public void createPriceException() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미존재하는 메뉴 그룹으로 메뉴 등록 시 에러 반환")
    public void createNonExistsMenuGroup() {
        Menu menu = new Menu();
        menu.setMenuGroupId(0L);
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setName("탕수육 세트");

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미존재하는 상품으로 메뉴 구성시 에러 반황")
    public void createNonExistsProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        menu.setPrice(BigDecimal.valueOf(18000));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 각 상품의 합보다 메뉴의 가격이 작지 않으면 등록 시 에러 반환")
    public void createNotCheaperPrice() {
        Menu menu = new Menu(1L, "모닝세트", BigDecimal.valueOf(8000), 패스트푸드류.getId(), Arrays.asList(스낵랩_메뉴_상품, 맥모닝_메뉴_상품));

        given(productDao.findById(스낵랩.getId())).willReturn(Optional.of(스낵랩));
        given(productDao.findById(맥모닝.getId())).willReturn(Optional.of(맥모닝));
        given(menuGroupDao.existsById(패스트푸드류.getId())).willReturn(true);

        assertThatThrownBy(()-> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createSuccess() {
        Menu menu = new Menu(1L, "스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류.getId(), Arrays.asList(스낵랩_메뉴_상품));

        given(productDao.findById(스낵랩.getId())).willReturn(Optional.of(스낵랩));
        given(menuProductDao.save(스낵랩_메뉴_상품)).willReturn(스낵랩_메뉴_상품);
        given(menuGroupDao.existsById(패스트푸드류.getId())).willReturn(true);
        given(menuDao.save(menu)).willReturn(menu);

        assertThat(menuService.create(menu).getId()).isEqualTo(menu.getId());
    }

    @Test
    public void list(){
        Menu 스낵랩_세트 = new Menu(1L, "스낵랩 상품", BigDecimal.valueOf(3000), 패스트푸드류.getId(), Arrays.asList(스낵랩_메뉴_상품));
        Menu 모닝_세트 = new Menu(1L, "모닝세트", BigDecimal.valueOf(7000), 패스트푸드류.getId(), Arrays.asList(스낵랩_메뉴_상품, 맥모닝_메뉴_상품));

        given(menuDao.findAll()).willReturn(Arrays.asList(스낵랩_세트, 모닝_세트));
        given(menuProductDao.findAllByMenuId(스낵랩.getId())).willReturn(Arrays.asList(스낵랩_메뉴_상품, 맥모닝_메뉴_상품));

        assertThat(menuService.list()).contains(스낵랩_세트, 모닝_세트);
    }
}