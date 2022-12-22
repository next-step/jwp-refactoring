package kitchenpos.table.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
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

import static kitchenpos.table.application.TableGroupServiceTest.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문테이블")
public
class OrderTableServiceTest {

    @InjectMocks
    private OrderTableService orderTableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable 메뉴테이블1;
    private OrderTable 메뉴테이블2;
    private OrderTable 메뉴테이블3;

    private TableGroup 단체지정;

    @BeforeEach
    void setUp() {
        단체지정 = generateTableGroup();

        메뉴테이블1 = generateOrderTable(단체지정, 5, false);
        메뉴테이블2 = generateOrderTable(단체지정, 3, false);
        메뉴테이블3 = generateOrderTable(단체지정, 4, false);
    }

    @Test
    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    void tableTest1() {
        List<OrderTable> 메뉴테이블들 = 메뉴테이블들_생성();
        given(orderTableRepository.findAllJoinFetch()).willReturn(메뉴테이블들);

        List<OrderTableResponse> 조회된_메뉴테이블들 = orderTableService.list();

        assertThat(조회된_메뉴테이블들.size()).isEqualTo(메뉴테이블들.size());
    }

    @Test
    @DisplayName("새로운 주문 테이블을 추가할 수 있다.")
    void tableTest2() {
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(메뉴테이블1);

        OrderTableRequest 추가할_메뉴테이블 = generateOrderTableRequest(메뉴테이블1.getNumberOfGuests(), 메뉴테이블1.isEmpty());
        OrderTableResponse 추가된_메뉴테이블 = orderTableService.create(추가할_메뉴테이블);

        assertThat(추가된_메뉴테이블.getId()).isEqualTo(메뉴테이블1.getId());
    }

    @Test
    @DisplayName("주문 테이블의 공석여부를 변경할 수 있다.")
    void tableTest3() {
        OrderTableRequest 변경할_메뉴테이블 = generateOrderTableRequest(0, true);

        given(orderTableRepository.findById(메뉴테이블1.getId())).willReturn(Optional.of(메뉴테이블1));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(메뉴테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        메뉴테이블1.updateEmpty(변경할_메뉴테이블.isEmpty());

        given(orderTableRepository.save(메뉴테이블1)).willReturn(메뉴테이블1);

        OrderTableResponse 변경된_메뉴테이블 = orderTableService.changeEmpty(메뉴테이블1.getId(), 변경할_메뉴테이블);
        assertThat(변경된_메뉴테이블.isEmpty()).isEqualTo(변경할_메뉴테이블.isEmpty());
    }

    @Test
    @DisplayName("공석여부 변경 - 주문 테이블은 단체 지정이 되어있으면 안된다.")
    void tableTest4() {
        OrderTableRequest 변경할_메뉴테이블 = generateOrderTableRequest(1, true);

        given(orderTableRepository.findById(메뉴테이블1.getId())).willReturn(Optional.of(메뉴테이블1));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(메뉴테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> orderTableService.changeEmpty(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("공석여부를 변경 - 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.")
    void tableTest5() {
        given(orderTableRepository.findById(메뉴테이블1.getId())).willReturn(Optional.of(메뉴테이블1));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(메뉴테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> orderTableService.changeEmpty(메뉴테이블1.getId(), any(OrderTableRequest.class)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 게스트 수를 변경할 수 있다.")
    void tableTest6() {
        OrderTableRequest 변경할_메뉴테이블 = generateOrderTableRequest(5, false);

        given(orderTableRepository.findById(메뉴테이블1.getId())).willReturn(Optional.of(메뉴테이블1));

        메뉴테이블1.updateNumberOfGuests(변경할_메뉴테이블.getNumberOfGuests());

        given(orderTableRepository.save(메뉴테이블1)).willReturn(메뉴테이블1);

        OrderTableResponse 변경된_메뉴테이블 = orderTableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블);

        assertThat(변경된_메뉴테이블.getNumberOfGuests()).isEqualTo(변경할_메뉴테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("게스트 수 변경 - 게스트 수는 음수여선 안된다.")
    void tableTest7() {
        OrderTableRequest 변경할_메뉴테이블 = generateOrderTableRequest(-1, false);

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 수 변경 - 존재하지 않는 주문 테이블로 요청할 수 없다.")
    void tableTest8() {
        given(orderTableRepository.findById(메뉴테이블1.getId())).willReturn(Optional.of(generateOrderTable(null, 5, true)));

        OrderTableRequest 변경할_메뉴테이블 = generateOrderTableRequest(5, false);

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게스트 수 변경 - 빈 주문 테이블이어선 안된다.")
    void tableTest9() {
        given(orderTableRepository.findById(메뉴테이블1.getId())).willReturn(Optional.empty());

        OrderTableRequest 변경할_메뉴테이블 = generateOrderTableRequest(5, false);

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(메뉴테이블1.getId(), 변경할_메뉴테이블))
                .isInstanceOf(EntityNotFoundException.class);
    }

    public static OrderTable generateOrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    private OrderTableRequest generateOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTableIdRequest generateOrderTableIdRequest(Long id) {
        return new OrderTableIdRequest(id);
    }

    private List<OrderTable> 메뉴테이블들_생성() {
        return Arrays.asList(메뉴테이블1, 메뉴테이블2, 메뉴테이블3);
    }

}
