package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.OrderTableMapper;
import kitchenpos.tablegroup.domain.TableGroup;
import org.mapstruct.Mapper;

@Mapper(uses = {
        OrderTableMapper.class
})
public interface TableGroupMapper {

    TableGroupResponse toResponse(TableGroup tableGroup);

}
