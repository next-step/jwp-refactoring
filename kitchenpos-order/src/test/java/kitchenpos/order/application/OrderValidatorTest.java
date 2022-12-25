package kitchenpos.order.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("주문과 주문 테이블 간의 validation 클래스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    private static final Long MENU_ID1 = 1L;
    private static final Long MENU_ID2 = 2L;
    private static final Long EMPTY_ORDER_TABLE_ID = 2L;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    private MenuGroup 양식;
    private Menu 양식_세트1;
    private Menu 양식_세트2;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트1 = new Menu("양식 세트1", 50000, 양식);
        양식_세트2 = new Menu("양식 세트2", 43000, 양식);
        주문테이블 = new OrderTable(2, false);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);
    }

    @DisplayName("주문 생성시 등록되지 않은 주문 테이블이면 주문을 생성할 수 없다.")
    @Test
    void 주문_생성시_등록되지_않은_주문_테이블이면_주문을_생성할_수_없다() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreateOrder(주문테이블.getId(), Arrays.asList(MENU_ID1, MENU_ID2)))
                .withMessage(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 생성시 등록되지 않은 메뉴가 포함되어 있으면 주문을 생성할 수 없다.")
    @Test
    void 주문_생성시_등록되지_않은_메뉴가_포함되어_있으면_주문을_생성할_수_없다() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(menuRepository.findAllById(Arrays.asList(MENU_ID1, MENU_ID2))).willReturn(Arrays.asList(양식_세트1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreateOrder(주문테이블.getId(), Arrays.asList(MENU_ID1, MENU_ID2)))
                .withMessage(ErrorMessage.MENU_NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 생성시 주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void 주문_생성시_주문_테이블이_비어있으면_주문을_생성할_수_없다() {
        OrderTable 빈_주문테이블 = new OrderTable(0, true);
        given(orderTableRepository.findById(EMPTY_ORDER_TABLE_ID)).willReturn(Optional.of(빈_주문테이블));
        given(menuRepository.findAllById(Arrays.asList(MENU_ID1, MENU_ID2))).willReturn(Arrays.asList(양식_세트1, 양식_세트2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreateOrder(EMPTY_ORDER_TABLE_ID, Arrays.asList(MENU_ID1, MENU_ID2)))
                .withMessage(ErrorMessage.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
    }

    @DisplayName("주문 생성시 1개의 메뉴도 포함되지 않았으면 주문을 생성할 수 없다.")
    @Test
    void 주문_생성시_1개의_메뉴도_포함되지_않았으면_주문을_생성할_수_없다() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreateOrder(주문테이블.getId(), Collections.emptyList()))
                .withMessage(ErrorMessage.ORDER_MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }
}
