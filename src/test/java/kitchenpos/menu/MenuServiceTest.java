package kitchenpos.menu;

import kitchenpos.menu.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import kitchenpos.menu.product.domain.Product;
import kitchenpos.menu.product.domain.ProductPrice;
import kitchenpos.menu.product.domain.ProductRepository;
import kitchenpos.menu.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {

        //given
        MenuRequest menu = new MenuRequest();
        ReflectionTestUtils.setField(menu, "menuGroupId", 1L);
        ReflectionTestUtils.setField(menu, "name", "후라이드세트");
        ReflectionTestUtils.setField(menu, "price", new BigDecimal("24000"));

        MenuProductRequest 후라이드요청 = new MenuProductRequest();
        ReflectionTestUtils.setField(후라이드요청, "productId", 1L);
        ReflectionTestUtils.setField(후라이드요청, "quantity", 1);

        MenuProductRequest 콜라요청 = new MenuProductRequest();
        ReflectionTestUtils.setField(콜라요청, "productId", 2L);
        ReflectionTestUtils.setField(콜라요청, "quantity", 1);

        ReflectionTestUtils.setField(menu, "products", Arrays.asList(후라이드요청, 콜라요청));

        MenuProduct 후라이드메뉴 = new MenuProduct();
        ReflectionTestUtils.setField(후라이드메뉴, "quantity", 1L);

        Product 후라이드 = new Product();
        ReflectionTestUtils.setField(후라이드, "id", 1L);
        ReflectionTestUtils.setField(후라이드, "name", "후라이드");
        ReflectionTestUtils.setField(후라이드, "productPrice", new ProductPrice("22000"));

        MenuProduct 콜라메뉴 = new MenuProduct();
        ReflectionTestUtils.setField(콜라메뉴, "quantity", 1L);

        Product 콜라 = new Product();
        ReflectionTestUtils.setField(콜라, "id", 2L);
        ReflectionTestUtils.setField(콜라, "name", "콜라");
        ReflectionTestUtils.setField(콜라, "productPrice", new ProductPrice("2000"));

        MenuGroup menuGroup = MenuGroup.create("치킨");
        List<Product> products = Arrays.asList(후라이드, 콜라);

        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(menuGroup));
        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(menuRepository.save(any())).thenReturn(Menu.create("후라이드세트", new BigDecimal("24000")));

        //when
        MenuResponse savedMenu = menuService.create(menu);

        //then
        assertThat(savedMenu).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void getMenus() {

        //given
        Menu menuA = Menu.create("후라이드세트", new BigDecimal("24000"));
        menuA.addProduct(Product.create("후라이드", new BigDecimal("23000")), 1);
        menuA.addProduct(Product.create("콜라", new BigDecimal("2000")), 1);

        Menu menuB = Menu.create("햄버거세트", new BigDecimal("10000"));
        menuB.addProduct(Product.create("햄버거", new BigDecimal("8000")), 1);
        menuB.addProduct(Product.create("콜라", new BigDecimal("2000")), 1);
        List<Menu> menus = Arrays.asList(menuA, menuB);

        when(menuRepository.findAll()).thenReturn(menus);

        //when
        List<MenuResponse> findMenus = menuService.list();


        //then
        assertThat(findMenus).isNotEmpty();
        List<MenuProductResponse> result = MenuProductResponse.ofList(
                menus.stream()
                .flatMap(m -> m.getMenuProducts().stream())
                .collect(toList())
        );

        assertThat(findMenus.stream()
                .flatMap(m -> m.getMenuProducts()
                .stream())
                .distinct()
                .collect(toList()))
                .containsAll(result);
    }
}

