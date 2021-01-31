package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.MenuGroupMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        MenuProductMapper.class,
        MenuGroupMapper.class
})
public interface MenuMapper {

    MenuResponse toResponse(Menu menu);

}
