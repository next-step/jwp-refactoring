package kitchenpos.menu.dto;

import javax.annotation.Generated;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.ProductMapper;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-31T10:11:50+0900",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class MenuProductMapperImpl implements MenuProductMapper {

    private final ProductMapper productMapper = Mappers.getMapper( ProductMapper.class );

    @Override
    public MenuProductResponse toDto(MenuProduct menuProduct) {
        if ( menuProduct == null ) {
            return null;
        }

        MenuProductResponse menuProductResponse = new MenuProductResponse();

        menuProductResponse.setProduct( productMapper.toResponse( menuProduct.getProduct() ) );
        menuProductResponse.setQuantity( menuProduct.getQuantity() );

        return menuProductResponse;
    }
}
