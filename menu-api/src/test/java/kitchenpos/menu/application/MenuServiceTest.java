package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private Long productId1;
    private Long productId2;
    private MenuProductRequest menuProduct1;
    private MenuProductRequest menuProduct2;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    void setUp() {
        productId1 = 1L;
        productId2 = 2L;
        menuProduct1 = new MenuProductRequest(productId1,  1);
        menuProduct2 = new MenuProductRequest(productId2, 1);
        menuProducts = Arrays.asList(menuProduct1, menuProduct2);
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void 메뉴_등록() {
        // given
        BigDecimal price = BigDecimal.valueOf(30000);
        MenuRequest request = new MenuRequest("1+1치킨", price, 1L, menuProducts);

        // when
        MenuResponse actual = menuService.create(request);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getMenuGroup()).isNotNull();
        assertThat(actual.getMenuProducts().size()).isEqualTo(menuProducts.size());
    }

    @DisplayName("메뉴의 가격이 없으면 등록이 실패한다.")
    @Test
    void 메뉴_생성_예외_메뉴_가격이_없음() {
        // given
        MenuRequest menu = new MenuRequest("반반", null, 1L, menuProducts);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("메뉴의 가격이 없으면 0보다 작으면 등록이 실패한다.")
    @Test
    void 메뉴_생성_예외_메뉴_가격이_음수() {
        // given
        final BigDecimal price = BigDecimal.valueOf(-1000L);
        MenuRequest menu = new MenuRequest("반반", price, 1L, menuProducts);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }


    @DisplayName("단일 상품 가격의 합보다 메뉴의 가격이 같거나 크면 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {18000 + 17000 + 100, 18000 + 17000})
    void 메뉴_생성_예외_메뉴_가격이_같거나_큼(Long price) {
        // given
        MenuRequest menu = new MenuRequest("반반", BigDecimal.valueOf(price), 1L, menuProducts);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> menuService.create(menu)
        );
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void 메뉴_목록_조회() {
        // given
        int savedMenuSize = 6;

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(savedMenuSize);
    }

}
