package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private Product 떡볶이;
    private Product 튀김;
    private Product 순대;

    private MenuProduct 떡튀순_상품_떡볶이;
    private MenuProduct 떡튀순_상품_튀김;
    private MenuProduct 떡튀순_상품_순대;
    private MenuProduct 떡튀순_곱배기_상품_떡볶이;

    private List<MenuProduct> 떡튀순_상품_목록;
    private List<MenuProduct> 떡튀순_곱배기_상품_목록;
    private MenuGroup 세트;
    private Menu 떡튀순;
    private Menu 떡튀순_곱배기;

    @BeforeEach
    void setUp() {
        떡볶이 = new Product(1L, "떡볶이", new ProductPrice(4500));
        튀김 = new Product(2L, "튀김", new ProductPrice(2500));
        순대 = new Product(3L, "순대", new ProductPrice(4000));

        떡튀순_상품_떡볶이 = new MenuProduct(1L, null, 떡볶이.getId(), 1);
        떡튀순_상품_튀김 = new MenuProduct(2L, null, 튀김.getId(), 1);
        떡튀순_상품_순대 = new MenuProduct(3L, null, 순대.getId(), 1);
        떡튀순_곱배기_상품_떡볶이 = new MenuProduct(4L, null, 떡볶이.getId(), 2);

        떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        세트 = new MenuGroup(1L, "세트");
        떡튀순 = new Menu(1L, "떡튀순", new MenuPrice(10000), 세트.getId(), new MenuProducts(떡튀순_상품_목록));
        떡튀순_곱배기 = new Menu(2L, "떡튀순_곱배기", new MenuPrice(10000), 세트.getId(), new MenuProducts(떡튀순_곱배기_상품_목록));

        떡튀순_상품_떡볶이.setMenu(떡튀순);
        떡튀순_상품_튀김.setMenu(떡튀순);
        떡튀순_상품_순대.setMenu(떡튀순);
        떡튀순_곱배기_상품_떡볶이.setMenu(떡튀순_곱배기);
    }

    @DisplayName("메뉴 생성")
    @Test
    void 메뉴_생성() {
        MenuRequest request = MenuRequest.from(떡튀순);
        when(menuRepository.save(request.toMenu())).thenReturn(request.toMenu());

        MenuResponse 생성된_메뉴 = menuService.create(request);

        verify(menuValidator).validateBeforeCreateMenu(any(Menu.class));
        assertAll(
                () -> assertThat(생성된_메뉴.getName()).isEqualTo("떡튀순"),
                () -> assertThat(생성된_메뉴.getPrice()).isEqualTo(BigDecimal.valueOf(10000))
        );
    }

    @DisplayName("메뉴 조회")
    @Test
    void 메뉴_조회() {
        List<Menu> 등록된_메뉴_목록 = Arrays.asList(떡튀순, 떡튀순_곱배기);
        when(menuRepository.findAll()).thenReturn(등록된_메뉴_목록);

        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        assertAll(
                () -> assertThat(조회된_메뉴_목록.size()).isEqualTo(등록된_메뉴_목록.size()),
                () -> assertThat(조회된_메뉴_목록.get(0).getName()).isEqualTo(등록된_메뉴_목록.get(0).getName()),
                () -> assertThat(조회된_메뉴_목록.get(1).getName()).isEqualTo(등록된_메뉴_목록.get(1).getName()),
                () -> assertThat(조회된_메뉴_목록.get(0).getPrice()).isEqualTo(등록된_메뉴_목록.get(0).getPriceValue()),
                () -> assertThat(조회된_메뉴_목록.get(1).getPrice()).isEqualTo(등록된_메뉴_목록.get(1).getPriceValue())
        );
    }

}