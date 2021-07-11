package kitchenpos.domain;

import kitchenpos.application.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 객체 테스트")
@ExtendWith(MockitoExtension.class)
class MenuTest {

    @InjectMocks
    private MenuService menuService;

    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setMenuGroupId(1L);
        메뉴.setName("짜장면 탕수육 세트");
        메뉴.setPrice(new BigDecimal(19000));
    }

    @Test
    void 메뉴_가격에_음수_입력_하여_등록_요청_시_에러_발생() {
        메뉴.setPrice(new BigDecimal(-1000));
        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격에_null_입력_하여_등록_요청_시_에러_발생() {
        메뉴.setPrice(null);
        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }
}
