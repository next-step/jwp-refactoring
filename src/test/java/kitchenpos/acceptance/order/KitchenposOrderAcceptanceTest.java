package kitchenpos.acceptance.order;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.menu.MenuDto;
import kitchenpos.dto.order.OrderDto;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderLineItemDtos;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.TableGroupDto;
import kitchenpos.presentation.menu.MenuRestControllerTest;
import kitchenpos.presentation.order.OrderRestControllerTest;
import kitchenpos.presentation.table.TableGroupRestControllerTest;
import kitchenpos.presentation.table.TableRestControllerTest;
import kitchenpos.testassistance.config.TestConfig;

public class KitchenposOrderAcceptanceTest extends TestConfig {
    @DisplayName("한테이블대한 손님들이 음식을 주문한다.")
    @Test
    void order_oneTable() {
        // given
        OrderTableDto 손님있는_테이블 = 테이블에_손님이앉음(2);

        // when
        MenuDto[] 메뉴들 = MenuRestControllerTest.메뉴_조회요청().as(MenuDto[].class);
        OrderLineItemDtos 주문명세서 = 주문명세서_생성(List.of(메뉴들[0], 메뉴들[1]));
        OrderDto 주문 = 테이블_주문요청(손님있는_테이블, 주문명세서);

        // then
        테이블_주문됨(손님있는_테이블, 주문명세서, 주문);
    }

    @DisplayName("여러 테이블의 손님이 음식을 주문한다.")
    @Test
    void order_multiTable() {
        // when
        OrderTableDto 손님있는_테이블1 = 테이블에_손님이앉음(2);
        OrderTableDto 손님있는_테이블2 = 테이블에_손님이앉음(4);

        TableGroupDto 단체결제지정 = TableGroupDto.of(List.of(손님있는_테이블1, 손님있는_테이블2));

        TableGroupRestControllerTest.단체지정_저장요청(단체결제지정);

        MenuDto[] 메뉴들 = MenuRestControllerTest.메뉴_조회요청().as(MenuDto[].class);

        OrderLineItemDtos 주문명세서 = 주문명세서_생성(List.of(메뉴들[0], 메뉴들[1]));

        // then
        OrderDto 주문1 = 테이블_주문요청(손님있는_테이블1, 주문명세서);
        OrderDto 주문2 = 테이블_주문요청(손님있는_테이블2, 주문명세서);

        // then
        테이블_주문됨(손님있는_테이블1, 주문명세서, 주문1);
        테이블_주문됨(손님있는_테이블2, 주문명세서, 주문2);
    }

    @DisplayName("주문된 음식을 계산한다.")
    @Test
    void order_priceCalculator() {
        // given
        OrderTableDto 손님있는_테이블 = 테이블에_손님이앉음(2);

        // when
        MenuDto[] 메뉴들 = MenuRestControllerTest.메뉴_조회요청().as(MenuDto[].class);
        OrderLineItemDtos 주문명세서 = 주문명세서_생성(List.of(메뉴들[0], 메뉴들[1]));
        OrderDto 주문 = 테이블_주문요청(손님있는_테이블, 주문명세서);

        // then
        테이블_주문됨(손님있는_테이블, 주문명세서, 주문);

        // when
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        OrderDto 결재된_주문 = OrderRestControllerTest.주문_상태변경요청(주문).as(OrderDto.class);

        // then
        Assertions.assertThat(결재된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    private void 테이블_주문됨(OrderTableDto 손님있는_테이블, OrderLineItemDtos 주문명세서, OrderDto 주문) throws MultipleFailuresError {
        Assertions.assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        Assertions.assertThat(주문.getOrderTableId()).isEqualTo(손님있는_테이블.getId());

        
        assertAll(
            () -> Assertions.assertThat(주문.getOrderLineItems().get(0).getOrderId()).isEqualTo(주문.getId()),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(0).getMenuId()).isEqualTo(주문명세서.getMenuId(0)),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(0).getQuantity()).isEqualTo(주문명세서.getQuantity(0))
        );

        assertAll(
            () -> Assertions.assertThat(주문.getOrderLineItems().get(1).getOrderId()).isEqualTo(주문.getId()),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(1).getMenuId()).isEqualTo(주문명세서.getMenuId(1)),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(1).getQuantity()).isEqualTo(주문명세서.getQuantity(1))
        );
    }

    private OrderLineItemDtos 주문명세서_생성(List<MenuDto> menus) {
        return menus.stream()
                    .map(menu -> 주문항목_생성(menu))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), OrderLineItemDtos::of));
    }

    private OrderLineItemDto 주문항목_생성(final MenuDto menu) {
        return OrderLineItemDto.of(menu.getId(), 1L);
    }

    private OrderDto 테이블_주문요청(OrderTableDto orderTable, OrderLineItemDtos OrderSpecification) {
        // when
        OrderDto 주문 = 주문_생성(orderTable, OrderSpecification);

        return OrderRestControllerTest.주문_저장요청(주문).as(OrderDto.class);
    }

    private OrderDto 주문_생성(OrderTableDto orderTable, OrderLineItemDtos OrderSpecification) {
        return OrderDto.of(orderTable.getId(), OrderSpecification.getOrderLineItemDtos());
    }

    private OrderTableDto 테이블에_손님이앉음(int numberOfGuests) {
        List<OrderTableDto> 손님없는_테이블들 = 반테이블들_조회됨();

        OrderTableDto 손님있는_테이블 = 손님있는_테이블_생성(numberOfGuests, 손님없는_테이블들);

        손님있는_테이블 = TableRestControllerTest.주문테이블_빈테이블_변경요청(손님있는_테이블).as(OrderTableDto.class);
        손님있는_테이블 = TableRestControllerTest.주문테이블_고객수_변경요청(손님있는_테이블).as(OrderTableDto.class);

        return 손님있는_테이블;
    }

    private OrderTableDto 손님있는_테이블_생성(int numberOfGuests, List<OrderTableDto> 손님없는_테이블들) {
        OrderTableDto 손님있는_테이블 = 손님없는_테이블들.get(0);
        손님있는_테이블.changeNumberOfGuests(numberOfGuests);
        손님있는_테이블.changeEmpty(false);
        return 손님있는_테이블;
    }

    private List<OrderTableDto> 반테이블들_조회됨() {
        return List.of(TableRestControllerTest.주문테이블_조회요청().as(OrderTableDto[].class)).stream()
                    .filter(OrderTableDto::isEmpty)
                    .collect(Collectors.toList());
    }
}
