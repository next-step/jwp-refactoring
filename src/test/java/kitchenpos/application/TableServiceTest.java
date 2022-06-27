package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable 주문_테이블1;


    @BeforeEach
    void before() {
        주문_테이블1 = orderTableRepository.save(new OrderTable(3, false));
    }

    @Test
    @DisplayName("주문 테이블을 생성 할 수 있다.")
    void createTest() {
        //when
        OrderTableResponse orderTable = tableService.create(
                OrderTableRequest.of(주문_테이블1.getNumberOfGuests(), 주문_테이블1.isEmpty()));

        //then
        assertAll(
                () -> assertThat(orderTable).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(주문_테이블1.getNumberOfGuests()),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(주문_테이블1.isEmpty())
        );
        assertThat(orderTable).isNotNull();
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(주문_테이블1.getNumberOfGuests());
        assertThat(orderTable.isEmpty()).isEqualTo(주문_테이블1.isEmpty());
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회 할 수 있다.")
    void listTest() {

        List<OrderTableResponse> orderTables = tableService.list();
        //then
        assertAll(
                () -> assertThat(orderTables).isNotNull(),
                () -> assertThat(orderTables).hasSize(1)
        );
    }

    @Test
    @DisplayName("주문 테이블이 시스템에 등록 되어 있지 않으면 빈테이블로 변경 할 수 없다.")
    void changeEmptyFailWithTableNotExitTest() {

        OrderTable orderTable = new OrderTable(7L, 3, false);
        //when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), OrderTableRequest.of(orderTable.isEmpty()))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("주문 테이블이 단체 지정 되어 있으면 빈테이블로 지정 할 수 없다.")
    void changeEmptyFailWithTableGroupTest() {
        //given
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(Arrays.asList(
                new OrderTable(3, true),
                new OrderTable(4, true))));
        OrderTable savedOrdertable = orderTableRepository.save(new OrderTable(tableGroup, 10, false));

        //when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedOrdertable.getId(), OrderTableRequest.of(true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 중(COOKING), 식사 중(MEAL) 상태에 있으면 빈테이블로 지정 할 수 없다.")
    void changeEmptyFailWithStatusTest(@Autowired MenuRepository menuRepository,
                                       @Autowired MenuGroupRepository menuGroupRepository) {
        //given : 주문 생성
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("중식"));
        Menu menu = menuRepository.save(new Menu("볶음밥", BigDecimal.valueOf(1000L), menuGroup));

        Order order = new Order(주문_테이블1.getId(), new OrderLineItem(menu.getId(), 1));
        orderRepository.save(order);

        //when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_테이블1.getId(), OrderTableRequest.of(주문_테이블1.isEmpty()))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블을 빈테이블로 변경 할 수 있다.")
    void changeEmptyTest() {

        //when
        OrderTableResponse orderTable = tableService.changeEmpty(주문_테이블1.getId(),
                OrderTableRequest.of(true));

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 시스템에 등록 되어 있지 않으면 손님수를 변경 할 수 없다.")
    void changeNumberOfGuestsFailWithOrderTableNotExistTest() {
        //given
        OrderTable orderTable = new OrderTable(8L, 5, false);

        //when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(),
                        OrderTableRequest.of(10))
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("빈테이블이 아니면 손님수를 변경 할 수 없다.")
    void changeNumberOfGuestsFailWithEmptyTableTest() {
        //given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1L, 5, false));

        //when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(),
                        OrderTableRequest.of(10))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블에 방문한 손님수를 변경 할 수 있다.")
    void changeNumberOfGuestsTest() {

        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(0, true));
        //when
        OrderTableResponse orderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                OrderTableRequest.of(5));

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }
}
