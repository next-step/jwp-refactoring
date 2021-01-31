package kitchenpos.table.dto;

import javax.annotation.Generated;

import kitchenpos.table.OrderTable;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-31T10:11:50+0900",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class OrderTableMapperImpl implements OrderTableMapper {

    @Override
    public OrderTableResponse toResponse(OrderTable orderTable) {
        if ( orderTable == null ) {
            return null;
        }

        OrderTableResponse orderTableResponse = new OrderTableResponse();

        orderTableResponse.setId( orderTable.getId() );
        orderTableResponse.setNumberOfGuests( orderTable.getNumberOfGuests() );
        orderTableResponse.setEmpty( orderTable.isEmpty() );

        return orderTableResponse;
    }
}
