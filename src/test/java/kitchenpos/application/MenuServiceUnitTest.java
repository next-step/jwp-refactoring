package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.common.DefaultData.메뉴그룹_두마리메뉴_ID;
import static kitchenpos.common.DefaultData.상품_후라이드_ID;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceUnitTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 가격 없이 생성한다")
    @Test
    void testCreateMenuWithoutPrice() {
        // given
        Menu menu = new Menu();

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 가격을 0원 미만으로 생성한다")
    @Test
    void testCreateMenuWithNegativePrice() {
        // given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(-1000));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }
    
    @DisplayName("존재하지 않는 메뉴그룹으로 메뉴를 생성한다")
    @Test
    void testCreateMenu_withNonExistentMenuGroup() {
        // given
        Long 미존재_메뉴그룹_ID = 0L;

        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(19_000));
        menu.setMenuGroupId(미존재_메뉴그룹_ID);

        given(menuGroupDao.existsById(미존재_메뉴그룹_ID)).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 상품 개수를 0개로 등록한다")
    @Test
    void testCreateMenuWithZeroProduct() {
        // given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(19_000));
        menu.setMenuGroupId(메뉴그룹_두마리메뉴_ID);
        menu.setMenuProducts(Collections.emptyList());

        given(menuGroupDao.existsById(메뉴그룹_두마리메뉴_ID)).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 상품으로 메뉴를 등록한다")
    @Test
    void testCreateMenu_withNonExistentProduct() {
        // given
        Long 미존재_상품_ID = 0L;
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(미존재_상품_ID);

        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(19_000));
        menu.setMenuGroupId(메뉴그룹_두마리메뉴_ID);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(메뉴그룹_두마리메뉴_ID)).willReturn(true);
        given(productDao.findById(미존재_상품_ID)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("상품 가격 합계보다 비싼 메뉴를 등록한다")
    @Test
    void testCreateMenuWithZeroPriceProduct() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(상품_후라이드_ID);
        menuProduct.setQuantity(1);

        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(100_000));
        menu.setMenuGroupId(메뉴그룹_두마리메뉴_ID);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        Product product = new Product();
        product.setName("후라이드");
        product.setPrice(new BigDecimal(16_000));

        given(menuGroupDao.existsById(메뉴그룹_두마리메뉴_ID)).willReturn(true);
        given(productDao.findById(상품_후라이드_ID)).willReturn(Optional.of(product));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }
}
