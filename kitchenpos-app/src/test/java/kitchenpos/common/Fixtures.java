package kitchenpos.common;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.ChangeEmptyTableRequest;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.NumberOfGuestsRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableId;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.TableGroupRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Fixtures {

    public static ProductRequest.ProductRequestBuilder productRequest() {
        return ProductRequest.builder()
                .name("뿌링클")
                .price(BigDecimal.valueOf(17_000));
    }

    public static MenuRequest.MenuRequestBuilder menuRequest() {
        return MenuRequest.builder()
                .name("후라이드+후라이드")
                .price(new BigDecimal(19000))
                .menuGroupId(1L)
                .menuProducts(Collections.singletonList(menuProductRequest().build()));
    }

    public static MenuProductRequest.MenuProductRequestBuilder menuProductRequest() {
        return MenuProductRequest.builder()
                .productId(1L)
                .quantity(2);
    }

    public static MenuGroupRequest menuGroupRequest() {
        return new MenuGroupRequest("두마리메뉴");
    }

    public static OrderTableRequest.OrderTableRequestBuilder emptyOrderTableRequest() {
        return OrderTableRequest.builder()
                .numberOfGuests(0)
                .empty(true);
    }

    public static ChangeEmptyTableRequest toNotEmptyTableRequest() {
        return new ChangeEmptyTableRequest(false);
    }

    public static NumberOfGuestsRequest numberOfGuestsRequest() {
        return new NumberOfGuestsRequest(4);
    }

    public static OrderRequest.OrderRequestBuilder orderRequest() {
        return OrderRequest.builder()
                .orderTableId(1L)
                .orderLineItems(Collections.singletonList(orderLineItemRequest().build()));
    }

    public static OrderLineItemRequest.OrderLineItemRequestBuilder orderLineItemRequest() {
        return OrderLineItemRequest.builder()
                .menuId(1L)
                .quantity(1L);
    }

    public static TableGroupRequest tableGroupRequest(List<Long> tableIds) {
        List<OrderTableId> orderTableIds = tableIds.stream()
                .map(OrderTableId::new)
                .collect(Collectors.toList());

        return new TableGroupRequest(orderTableIds);
    }

    public static OrderTable.OrderTableBuilder anEmptyOrderTable() {
        return OrderTable.builder()
                .numberOfGuests(0)
                .empty(true);
    }

    public static TableGroup aTableGroup() {
        OrderTable orderTable = anEmptyOrderTable().build();
        return new TableGroup(Arrays.asList(orderTable, orderTable));
    }
}
