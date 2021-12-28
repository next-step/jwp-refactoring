package kitchenpos.menu;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.group.application.MenuGroupService;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

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
    private MenuGroupService menuGroupService;

    @Mock
    private ProductService productService;

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
        ReflectionTestUtils.setField(후라이드요청, "quantity", 1L);

        MenuProductRequest 콜라요청 = new MenuProductRequest();
        ReflectionTestUtils.setField(콜라요청, "productId", 2L);
        ReflectionTestUtils.setField(콜라요청, "quantity", 1L);

        ReflectionTestUtils.setField(menu, "products", Arrays.asList(후라이드요청, 콜라요청));

        Product 후라이드 = Product.create("후라이드", new BigDecimal(22000));
        ReflectionTestUtils.setField(후라이드, "id", 1L);

        Product 콜라 = Product.create("콜라", new BigDecimal(2000));
        ReflectionTestUtils.setField(콜라, "id", 2L);

        MenuGroup menuGroup = MenuGroup.create("치킨");
        List<Product> products = Arrays.asList(후라이드, 콜라);

        when(menuGroupService.findById(anyLong())).thenReturn(menuGroup);
        when(productService.findAllByIds(anyList())).thenReturn(products);
        when(menuRepository.save(any())).thenReturn(Menu.prepared("후라이드세트", new BigDecimal("24000")));

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
        final Menu menuA = Menu.prepared("후라이드세트", new BigDecimal("24000"));
        final Map<Product, Long> menuAProducts = new HashMap<>();
        menuAProducts.put(Product.create("후라이드", new BigDecimal("23000")), 1L);
        menuAProducts.put(Product.create("콜라", new BigDecimal("2000")), 1L);
        menuA.addProducts(menuAProducts);

        final Map<Product, Long> menuBProducts = new HashMap<>();
        final Menu menuB = Menu.prepared("햄버거세트", new BigDecimal("10000"));
        menuBProducts.put(Product.create("햄버거", new BigDecimal("8000")), 1L);
        menuBProducts.put(Product.create("콜라", new BigDecimal("2000")), 1L);
        menuB.addProducts(menuBProducts);

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

