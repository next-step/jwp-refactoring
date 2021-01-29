package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        ProductMapper.class
})
public interface MenuProductMapper {

    MenuProductResponse toDto(MenuProduct menuProduct);

}
