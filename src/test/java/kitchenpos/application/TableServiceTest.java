package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문테이블")
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable 메뉴테이블1;
    private OrderTable 메뉴테이블2;
    private OrderTable 메뉴테이블3;

    @BeforeEach
    void setUp() {
        메뉴테이블1 = generateOrderTable(1L, 5, false);
        메뉴테이블2 = generateOrderTable(2L, 3, false);
        메뉴테이블3 = generateOrderTable(3L, 4, false);
    }

    @Test
    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    void tableTest1() {
        List<OrderTable> 메뉴테이블들 = 메뉴테이블들_생성();
        given(orderTableDao.findAll()).willReturn(메뉴테이블들);

        List<OrderTable> 조회된_메뉴테이블들 = tableService.list();
        assertThat(조회된_메뉴테이블들.size()).isEqualTo(메뉴테이블들.size());
    }

    @Test
    @DisplayName("새로운 주문 테이블을 추가할 수 있다.")
    void tableTest2() {
        given(orderTableDao.save(any(OrderTable.class))).willReturn(메뉴테이블1);

        OrderTable 추가된_메뉴테이블 = tableService.create(메뉴테이블1);
        assertThat(추가된_메뉴테이블.getId()).isEqualTo(메뉴테이블1.getId());
    }

    @Test
    @DisplayName("주문 테이블의 공석여부를 변경할 수 있다.")
    void tableTest3() {
        OrderTable 변경할_메뉴테이블 = generateOrderTable(null, 0, true);

        given(orderTableDao.findById(메뉴테이블1.getId())).willReturn(Optional.of(메뉴테이블1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(메뉴테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        메뉴테이블1.setEmpty(변경할_메뉴테이블.isEmpty());

        given(orderTableDao.save(메뉴테이블1)).willReturn(메뉴테이블1);

        OrderTable 변경된_메뉴테이블 = tableService.changeEmpty(메뉴테이블1.getId(), 변경할_메뉴테이블);
        assertThat(변경된_메뉴테이블.isEmpty()).isEqualTo(변경할_메뉴테이블.isEmpty());
    }

    @Test
    @DisplayName("공석여부 변경 - 주문 테이블은 단체 지정이 되어있으면 안된다.")
    void tableTest4() {
        OrderTable 변경할_메뉴테이블 = generateOrderTable(1L, 1L);

        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(변경할_메뉴테이블));

        assertThatThrownBy(() -> tableService.changeEmpty(변경할_메뉴테이블.getId(), any(OrderTable.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("공석여부를 변경 - 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.")
    void tableTest5() {
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(메뉴테이블1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(메뉴테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(메뉴테이블1.getId(), any(OrderTable.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 게스트 수를 변경할 수 있다.")
    void tableTest6() {
        OrderTable 변경할_메뉴테이블 = generateOrderTable(null, 5, false);

        given(orderTableDao.findById(메뉴테이블1.getId())).willReturn(Optional.of(메뉴테이블1));

        메뉴테이블1.setNumberOfGuests(변경할_메뉴테이블.getNumberOfGuests());

        given(orderTableDao.save(메뉴테이블1)).willReturn(메뉴테이블1);

        OrderTable 변경된_메뉴테이블 = tableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블);
        assertThat(변경된_메뉴테이블.getNumberOfGuests()).isEqualTo(변경할_메뉴테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("게스트 수 변경 - 게스트 수는 음수여선 안된다.")
    void tableTest7() {
        OrderTable 변경할_메뉴테이블 = generateOrderTable(null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 수 변경 - 존재하지 않는 주문 테이블로 요청할 수 없다.")
    void tableTest8() {
        given(orderTableDao.findById(메뉴테이블1.getId())).willReturn(Optional.of(generateOrderTable(1L, 5, true)));

        OrderTable 변경할_메뉴테이블 = generateOrderTable(null, 5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 수 변경 - 빈 주문 테이블이어선 안된다.")
    void tableTest9() {
        OrderTable 변경할_메뉴테이블 = generateOrderTable(null, 5, false);

        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable generateOrderTable(Long id, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, null, numberOfGuests, empty);
    }

    public static OrderTable generateOrderTable(Long id, Long tableGroupId) {
        return OrderTable.of(id, tableGroupId, 0, true);
    }

    public static OrderTable generateOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }

    private List<OrderTable> 메뉴테이블들_생성() {
        return Arrays.asList(메뉴테이블1, 메뉴테이블2, 메뉴테이블3);
    }

}
