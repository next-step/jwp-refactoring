package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    public static final Price 치즈버거_가격 = Price.wonOf(4500);
    public static final Price 감자튀김_가격 = Price.wonOf(2000);
    public static final Price 콜라_가격 = Price.wonOf(1000);
    public static final Price 치즈버거세트_가격 = new Price(6000);
    public static final Product 치즈버거 = new Product(1L, "치즈버거", 치즈버거_가격);
    public static final Product 감자튀김 = new Product(2L, "감자튀김", 감자튀김_가격);
    public static final Product 콜라 = new Product(3L, "콜라", 콜라_가격);
    public static final Long 치즈버거세트_메뉴ID = 1L;
    public static final MenuProduct 치즈버거세트_치즈버거 = new MenuProduct(치즈버거세트_메뉴ID, 치즈버거, 1L);
    public static final MenuProduct 치즈버거세트_감자튀김 = new MenuProduct(치즈버거세트_메뉴ID, 감자튀김, 1L);
    public static final MenuProduct 치즈버거세트_콜라 = new MenuProduct(치즈버거세트_메뉴ID, 콜라, 1L);
    public static final MenuGroup 패스트푸드 = new MenuGroup(1L, "패스트푸드");

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createSuccess() {
        // Given
        Menu 치즈버거세트 = new Menu("치즈버거세트", 치즈버거세트_가격, 패스트푸드.getId(), Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 메뉴 = MenuRequest.of(치즈버거세트);
        given(productRepository.findAllById(any())).willReturn(new ArrayList<>(Arrays.asList(치즈버거, 감자튀김, 콜라)));
        given(menuRepository.save(any())).willReturn(치즈버거세트);

        // When
        menuService.create(메뉴);

        // Then
        verify(productRepository, times(1)).findAllById(any());
        verify(menuRepository, times(1)).save(any());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // Given
        Menu 치즈버거세트 = new Menu(1L, "치즈버거세트", 치즈버거세트_가격, 패스트푸드, Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        List<Menu> menus = new ArrayList<>(Collections.singletonList(치즈버거세트));
        given(menuRepository.findAll()).willReturn(menus);

        // When & Then
        assertThat(menuService.list()).hasSize(1);
        verify(menuRepository, times(1)).findAll();
    }

}
