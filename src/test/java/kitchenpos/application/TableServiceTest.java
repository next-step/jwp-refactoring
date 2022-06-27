package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class TableServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableService tableService;

    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A;
    private OrderTable A_주문_테이블;
    private Order A_주문;
    private OrderLineItem A_주문항목;

    private TableGroup 단체_1;
    private OrderTable 주문_1_테이블;
    private OrderTable 주문_2_테이블;
    private OrderTable 주문_테이블_10명;

    @BeforeEach
    void setUp() {
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹");
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
        A = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A, 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A, 우아한_초밥_2.getId(), 2);

        A_우아한_초밥_1.mappedByMenu(A);
        A_우아한_초밥_2.mappedByMenu(A);

        초밥_메뉴그룹 = menuGroupRepository.save(초밥_메뉴그룹);
        우아한_초밥_1 = productRepository.save(우아한_초밥_1);
        우아한_초밥_2 = productRepository.save(우아한_초밥_2);
        A = menuRepository.save(A);

        A_주문_테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
        A_주문항목 = OrderLineItemFixtureFactory.create(A.getId(), 1);

        A_주문_테이블 = orderTableRepository.save(A_주문_테이블);

        A_주문 = OrderFixtureFactory.create(A_주문_테이블.getId(), OrderStatus.COOKING, Lists.newArrayList(A_주문항목));
        A_주문 = orderRepository.save(A_주문);

        주문_1_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_2_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(false, 10);
        단체_1 = TableGroupFixtureFactory.create(1L);

        주문_1_테이블 = orderTableRepository.save(주문_1_테이블);
        주문_2_테이블 = orderTableRepository.save(주문_2_테이블);
        주문_테이블_10명 = orderTableRepository.save(주문_테이블_10명);

        단체_1 = tableGroupRepository.save(단체_1);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create01() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        OrderTable findOrderTable = tableService.findOrderTable(response.getId());
        assertThat(response).isEqualTo(OrderTableResponse.from(findOrderTable));
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // when
        List<OrderTableResponse> responses = tableService.list();

        // then
        assertThat(responses).contains(OrderTableResponse.from(주문_1_테이블), OrderTableResponse.from(주문_2_테이블));
    }

    @DisplayName("테이블의 상태를 빈 테이블 상태로 변경할 수 있다.")
    @Test
    void change01() {
        // given
        주문_1_테이블.changeEmpty(true);

        OrderTableRequest request = OrderTableRequest.of(0, true);

        // when
        OrderTableResponse response = tableService.changeEmpty(주문_1_테이블.getId(), request);

        // then
        assertAll(
                () -> assertThat(response).isEqualTo(OrderTableResponse.from(주문_1_테이블)),
                () -> assertThat(response.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블이 존재하지 않으면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change02() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(0L, request));
    }

    @DisplayName("테이블 그룹이 존재하면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change03() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);
        주문_1_테이블.mappedByTableGroup(단체_1.getId());
        주문_2_테이블.mappedByTableGroup(단체_1.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_1_테이블.getId(), request));
    }

    @DisplayName("테이블의 주문 상태가 COOKING 혹은 MEAL 상태이면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change04() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);
        A_주문.changeOrderStatus(OrderStatus.MEAL);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(A_주문_테이블.getId(), request));
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change05() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, false);

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(A_주문_테이블.getId(), request);

        // then
        assertAll(
                () -> assertThat(response).isEqualTo(OrderTableResponse.from(A_주문_테이블)),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(주문_테이블_10명.findNumberOfGuests())
        );
    }

    @DisplayName("테이블의 변경하려는 손님 수는 1명 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void change06(int numberOfGuest) {
        // given
        OrderTableRequest request = OrderTableRequest.of(numberOfGuest, true);

        // when & then
        assertThrows(
                NegativeNumberOfGuestsException.class, () -> tableService.changeNumberOfGuests(A_주문_테이블.getId(), request));
    }

    @DisplayName("테이블이 없으면 손님 수를 변경할 수 없다.")
    @Test
    void change07() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(0L, request));
    }

    @DisplayName("테이블이 비어있으면 테이블 손님 수를 변경할 수 없다.")
    @Test
    void change08() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, true);
        주문_1_테이블.changeEmpty(true);
        tableService.changeEmpty(주문_1_테이블.getId(), request);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_1_테이블.getId(), request));
    }
}