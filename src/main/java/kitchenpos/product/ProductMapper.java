package kitchenpos.product;

import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    ProductResponse toResponse(Product product);

}
