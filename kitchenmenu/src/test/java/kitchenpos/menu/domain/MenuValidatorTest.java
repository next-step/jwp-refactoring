package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 validation service 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

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

    @DisplayName("존재하지 않는 메뉴 그룹에 속하는 메뉴 생성 요청 시 예외처리")
    @Test
    void 존재하지_않는_메뉴_그룹의_메뉴_생성() {
        when(menuGroupRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> menuValidator.validateBeforeCreateMenu(떡튀순))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품으로 구성된 메뉴 생성 요청 시 예외처리")
    @Test
    void 존재하지_않는_상품_메뉴_생성() {
        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(떡볶이.getId())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuValidator.validateBeforeCreateMenu(떡튀순))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("각 상품 가격의 합보다 큰 가격의 메뉴 생성 요청 시 예외처리")
    @Test
    void 초과_가격_예외처리() {
        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(떡볶이.getId())).thenReturn(Optional.of(떡볶이));
        when(productRepository.findById(튀김.getId())).thenReturn(Optional.of(튀김));
        when(productRepository.findById(순대.getId())).thenReturn(Optional.of(순대));
        Menu 초과_가격_메뉴 = new Menu(3L, "떡튀순", new MenuPrice(12000), 세트.getId(), new MenuProducts(떡튀순_상품_목록));

        assertThatThrownBy(() -> menuValidator.validateBeforeCreateMenu(초과_가격_메뉴)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("비어있는 이름으로 메뉴 생성 요청 시 예외처리")
    @Test
    void 비어있는_이름_예외처리() {
        Menu 비어있는_이름_메뉴 = new Menu(3L, "", new MenuPrice(10000), 세트.getId(), new MenuProducts(떡튀순_상품_목록));

        assertThatThrownBy(() -> menuValidator.validateBeforeCreateMenu(비어있는_이름_메뉴)).isInstanceOf(
                IllegalArgumentException.class);
    }

}
