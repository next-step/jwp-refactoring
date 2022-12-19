package kitchenpos.table.application;

import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.product.domain.MenuProduct;
import kitchenpos.product.domain.MenuProducts;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private OrderTable 주문테이블1;
    private Product 참치김밥;
    private Product 라볶이;
    private Product 돈까스;

    private MenuGroup 분식;

    private MenuProduct 라볶이세트참치김밥;
    private MenuProduct 라볶이세트라볶이;
    private MenuProduct 라볶이세트돈까스;

    private MenuProducts 라볶이세트구성;
    private Menu 라볶이세트;

    private OrderLineItem 주문항목1;
    private OrderLineItem 주문항목2;
    private OrderTable 주문테이블2;
    private Order 주문;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    private OrderValidator orderValidator;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(3), true);

        참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
        라볶이 = new Product(2L, "라볶이", new Price(new BigDecimal(4500)));
        돈까스 = new Product(3L, "돈까스", new Price(new BigDecimal(7000)));

        분식 = new MenuGroup(1L, "분식");

        라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
        라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
        라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

        라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
        라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);

        주문항목1 = new OrderLineItem(1L, null, 라볶이세트.getId(), new Quantity(1));
        주문항목2 = new OrderLineItem(2L, null, 라볶이세트.getId(), new Quantity(1));
        주문테이블2 = new OrderTable(1L, null, new NumberOfGuests(4), false);

        주문 = new Order(1L, 주문테이블2.getId(), OrderStatus.COOKING, LocalDateTime.now());

        orderValidator = new OrderValidator(orderRepository);
        tableService = new TableService(orderTableRepository, orderValidator);
    }

    @DisplayName("테이블생성테스트")
    @Test
    void createTableTest() {
        //given
        when(orderTableRepository.save(any(OrderTable.class)))
                .thenReturn(주문테이블1);

        //when
        OrderTableResponse result = tableService.create(OrderTableToOrderTableRequest(주문테이블1));

        //then
        assertAll(
                () -> assertThat(result.getId())
                        .isEqualTo(주문테이블1.getId()),
                () -> assertThat(result.getNumberOfGuests())
                        .isEqualTo(주문테이블1.getNumberOfGuests().getValue())
        );
    }

    private OrderTableRequest OrderTableToOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty());
    }

    @DisplayName("빈 상태로 변경테스트")
    @Test
    void changeEmptyTableTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(2), false);
        Order 주문 = new Order(1L, 주문테이블2.getId(), OrderStatus.COMPLETION, LocalDateTime.now());

        when(orderTableRepository.findById(orderTable.getId()))
                .thenReturn(Optional.ofNullable(orderTable));
        when(orderRepository.findOrderByOrderTableId(any(Long.class)))
                .thenReturn(Optional.ofNullable(주문));

        //when
        OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), true);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 id로 빈 테이블 변경 오류 테스트")
    @Test
    void notExistIdChangeEmptyTableExceptionTest() {
        //given
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블1.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 빈 테이블로 변경 오류 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void cookingOrMealChangeEmptyTableExceptionTest(OrderStatus orderStatus) {
        //given
        Order 주문 = new Order(1L, 주문테이블2.getId(), orderStatus, LocalDateTime.now());

        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문테이블1));
        when(orderRepository.findOrderByOrderTableId(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(주문));

        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블1.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("게스트숫자 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        int changeGuestNumber = 2;
        final OrderTable 주문테이블 = new OrderTable(1L, null, new NumberOfGuests(3), false);

        when(orderTableRepository.findById(주문테이블.getId()))
                .thenReturn(Optional.ofNullable(주문테이블));

        //when
        final OrderTableResponse result = tableService.changeNumberOfGuests(주문테이블.getId(), changeGuestNumber);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(changeGuestNumber);
    }

    @DisplayName("게스트숫자 0보다 작은 값으로 변경 오류 테스트")
    @Test
    void changeNumberOfGuestsUnderZeroExceptionTest() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 id로 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestNotExistTableIdExceptionTest() {
        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(), 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블에서 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestEmptyTableIdExceptionTest() {
        //given
        final OrderTable 빈테이블 = new OrderTable(1L, null, new NumberOfGuests(3), true);
        when(orderTableRepository.findById(빈테이블.getId()))
                .thenReturn(Optional.ofNullable(빈테이블));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈테이블.getId(), 10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
