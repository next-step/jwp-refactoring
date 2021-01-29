package kitchenpos.menugroup;

import org.mapstruct.Mapper;

@Mapper
public interface MenuGroupMapper {

    MenuGroupResponse toResponse(MenuGroup menuGroup);

}
