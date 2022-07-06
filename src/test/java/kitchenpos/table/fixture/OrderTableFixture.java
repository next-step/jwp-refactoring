package kitchenpos.table.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequestDto;
import kitchenpos.table.dto.OrderTableResponseDto;

public class OrderTableFixture {

    public static OrderTableRequestDto 주문테이블_요청_데이터_생성(int numberOfGuests) {
        return new OrderTableRequestDto(numberOfGuests, false);
    }

    public static OrderTable 주문테이블_데이터_생성(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, isEmpty);
    }

    public static OrderTableResponseDto 주문테이블_응답_데이터_생성(Long id, int numberOfGuests, boolean isEmpty, Long tableGroupId) {
        return new OrderTableResponseDto(id, numberOfGuests, isEmpty ,tableGroupId);
    }

}
