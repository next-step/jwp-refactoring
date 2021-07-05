package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    ProductRepository productRepository;

    MenuGroup savedMenuGroup;

    Product savedProduct;

    MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        BigDecimal price = BigDecimal.valueOf(10000);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("패스트푸드");
        savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setPrice(new Price(price));
        product.setName("빅맥");
        savedProduct = productRepository.save(product);

        menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);

    }

    @DisplayName("메뉴를 만들어보자")
    @Test
    public void createMenu() throws Exception {
        //given
        BigDecimal price = BigDecimal.valueOf(10000);
        String menuName = "맥도날드햄버거";

        MenuRequest menu = new MenuRequest(menuName, price, savedMenuGroup.getId(), Arrays.asList(menuProduct));

        //when
        MenuResponse savedMenu = menuService.create(menu);

        //then
        assertNotNull(savedMenu.getId());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(savedMenu.getMenuProducts()).hasSize(1);
        assertThat(savedMenu.getPrice()).isEqualByComparingTo(price);
        assertThat(savedMenu.getName()).isEqualTo(menuName);
    }

    @DisplayName("메뉴리스트를 출력해보자")
    @Test
    public void listMenu() throws Exception {
        //given
        BigDecimal price = BigDecimal.valueOf(10000);
        String menuName = "맥도날드햄버거";

        Menu menu = new Menu();
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setName(menuName);
        menu.setPrice(new Price(price));

        Menu savedMenu = menuRepository.save(menu);

        //when
        List<MenuResponse> menus = menuService.list();
        List<Long> findMenuIds = menus.stream()
                .map(findMenu -> findMenu.getId())
                .collect(Collectors.toList());

        //then
        assertNotNull(menus);
        assertTrue(findMenuIds.contains(savedMenu.getId()));
    }

    @DisplayName("0보다 작은 가격의 메뉴는 생성할수 없다")
    @Test
    public void invalidCreateMenu() throws Exception {
        //given
        MenuRequest menu = new MenuRequest("맥도날드햄버거", BigDecimal.valueOf(-1), savedMenuGroup.getId(), Arrays.asList(menuProduct));

        //when
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹이 존재하지 않으면 메뉴는 생성할수 없다")
    @Test
    public void failCreateMenuNotExistGroupMenu() throws Exception {
        //given
        MenuRequest menu = new MenuRequest("맥도날드햄버거", BigDecimal.valueOf(10000), 0L, Arrays.asList(menuProduct));

        //when
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 개별보다 비쌀 경우 메뉴는 생성할수 없다")
    @Test
    public void failCreateMenuInvalidPrice() throws Exception {
        //given
        MenuRequest menu = new MenuRequest("맥도날드햄버거", BigDecimal.valueOf(20000), savedMenuGroup.getId(), Arrays.asList(menuProduct));

        //when
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
