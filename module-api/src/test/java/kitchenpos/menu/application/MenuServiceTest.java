package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductService productService;
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuRequest menuRequest;
    MenuProductRequest 후라이드;
    MenuProductRequest 양념치킨;

    @BeforeEach
    public void setUp() {
        후라이드 =  new MenuProductRequest( 1L, 1);
        양념치킨 =  new MenuProductRequest( 1L, 1);
        List<MenuProductRequest> 두마리치킨 = Arrays.asList(후라이드, 양념치킨);
        menuRequest = new MenuRequest("두마리특급세일", BigDecimal.valueOf(10000), 1L, 두마리치킨);
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void createTest() {
        //given
        Product 치킨1 = new Product(1L, "치킨1", BigDecimal.valueOf(10000));
        Product 치킨2 = new Product(2L, "치킨2", BigDecimal.valueOf(10000));
        MenuProduct 반1 =  new MenuProduct( 치킨1, 1);
        MenuProduct 반2 =  new MenuProduct( 치킨2, 2);
        when(menuGroupService.findById(menuRequest.getMenuGroupId())).thenReturn(new MenuGroup("메뉴그룹"));
        when(productService.findById(any())).thenReturn(new Product("치킨",BigDecimal.valueOf(10000)));
        when(menuRepository.save(any())).thenReturn(new Menu("두마리특급세일",BigDecimal.valueOf(10000),new MenuGroup(1L, "메뉴그룹"), new MenuProducts(Arrays.asList(반1, 반2))));


        //when
        MenuResponse menuResponse = menuService.create(menuRequest);

        //then
        assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName());
        assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId());
        assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice());
    }


}