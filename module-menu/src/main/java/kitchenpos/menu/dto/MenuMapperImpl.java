package kitchenpos.menu.dto;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.MenuGroupMapper;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-31T10:11:50+0900",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class MenuMapperImpl implements MenuMapper {

    private final MenuProductMapper menuProductMapper = Mappers.getMapper( MenuProductMapper.class );
    private final MenuGroupMapper menuGroupMapper = Mappers.getMapper( MenuGroupMapper.class );

    @Override
    public MenuResponse toResponse(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuResponse menuResponse = new MenuResponse();

        menuResponse.setId( menu.getId() );
        menuResponse.setName( menu.getName() );
        menuResponse.setPrice( menu.getPrice() );
        menuResponse.setMenuGroup( menuGroupMapper.toResponse( menu.getMenuGroup() ) );
        menuResponse.setMenuProducts( menuProductListToMenuProductResponseList( menu.getMenuProducts() ) );

        return menuResponse;
    }

    protected List<MenuProductResponse> menuProductListToMenuProductResponseList(List<MenuProduct> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuProductResponse> list1 = new ArrayList<MenuProductResponse>( list.size() );
        for ( MenuProduct menuProduct : list ) {
            list1.add( menuProductMapper.toDto( menuProduct ) );
        }

        return list1;
    }
}
