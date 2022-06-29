package kitchenpos.event.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import kitchenpos.domain.Product;
import kitchenpos.dto.event.MenuCreateEventForProductDTO;
import kitchenpos.event.customEvent.MenuCreateEvent;
import kitchenpos.repository.ProductRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MenuCreateEventListenerInProduct implements ApplicationListener<MenuCreateEvent> {

    private final ProductRepository productRepository;

    public MenuCreateEventListenerInProduct(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(MenuCreateEvent event) {
        MenuCreateEventForProductDTO requestData = (MenuCreateEventForProductDTO) event.getSource();

        validatePriceSmallThenSum(requestData.getQuantityPerProduct(), requestData.getMenuPrice());
    }

    private void validatePriceSmallThenSum(Map<Long, Long> quantityPerProduct,
        BigDecimal menuPrice) {
        BigDecimal sum = BigDecimal.ZERO;

        productRepository.findAllByIdIn(new ArrayList<>(quantityPerProduct.keySet()));

        for (Entry<Long, Long> menuInfo : quantityPerProduct.entrySet()) {
            final Product product = productRepository.findById(menuInfo.getKey())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuInfo.getValue())));
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격 총합보다 작아야합니다");
        }
    }
}
