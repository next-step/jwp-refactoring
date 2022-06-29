package kitchenpos.common.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequestDto;
import kitchenpos.table.dto.OrderTableResponseDto;

public class OrderTableFixture {

    public static OrderTableRequestDto 주문테이블_요청_데이터_생성(int numberOfGuests) {
        return new OrderTableRequestDto(numberOfGuests, false);
    }

    public static OrderTable 주문테이블_데이터_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroup, numberOfGuests, isEmpty);
    }

    public static OrderTableResponseDto 주문테이블_응답_데이터_생성(Long id, int numberOfGuests, boolean isEmpty, TableGroup tableGroup) {
        return new OrderTableResponseDto(id, numberOfGuests, isEmpty ,tableGroup);
    }

}
