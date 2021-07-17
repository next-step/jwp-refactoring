package kitchenpos.product.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.MenuProductResponse;
import kitchenpos.product.dto.MenuResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductEventConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventConfiguration.class);

    private final ProductRepository productRepository;
    private final RabbitTemplate template;

    public ProductEventConfiguration(ProductRepository productRepository, RabbitTemplate template) {
        this.productRepository = productRepository;
        this.template = template;
    }

    @Bean
    Consumer<List<MenuResponse>> menuCreate() {
        return list -> list.forEach(this::validationMenuProductPrices);
    }

    public void validationMenuProductPrices(MenuResponse menuResponse) {
        logger.info("[ 메뉴 생성 이벤트 발생 - 메뉴가격 유효성 검증 ] 메뉴ID : {} ", menuResponse.getId());
        if (isExceedMenuProductPrice(menuResponse)) {
            publishCancelMenuCreateEvent(menuResponse);
        }
    }

    private void publishCancelMenuCreateEvent(MenuResponse menuResponse) {
        try {
            String message = new ObjectMapper().writeValueAsString(menuResponse);
            template.convertAndSend("cancelMenuCreate-in-0.menuGroup", message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("객체를 JSON 문자열로 변환하는 도중 예외가 발생했습니다.");
        }
    }

    private boolean isExceedMenuProductPrice(MenuResponse menuResponse) {
        Price menuPrice = Price.wonOf(menuResponse.getPrice());
        List<MenuProductResponse> menuProducts = menuResponse.getMenuProducts();
        List<Product> products = productRepository.findAllById(findProductIds(menuProducts));
        Price sumPrice = sumMenuProductPrices(menuProducts, products);
        if (menuPrice.isExceed(sumPrice)) {
            logger.info("[ 메뉴 가격 오류 ] 메뉴ID : {}, 메뉴가격 : {}, 메뉴 가격의 합 : {}", menuResponse.getId(), menuResponse.getPrice(), sumPrice);
            return true;
        }
        logger.info("[ 메뉴 가격 정상 ] 메뉴ID : {}, 메뉴가격 : {}, 메뉴 가격의 합 : {}", menuResponse.getId(), menuResponse.getPrice(), sumPrice);
        return false;
    }

    private Price sumMenuProductPrices(List<MenuProductResponse> menuProductRequests, List<Product> products) {
        Price sumPrice = Price.wonOf(0);
        for (MenuProductResponse menuProductRequest : menuProductRequests) {
            Product product = findProductById(products, menuProductRequest.getProductId());
            Quantity quantity = new Quantity(menuProductRequest.getQuantity());
            sumPrice = sumPrice.plus(product.multiplyPrice(quantity));
        }
        return sumPrice;
    }

    private List<Long> findProductIds(List<MenuProductResponse> menuProductRequests) {
        return menuProductRequests.stream()
            .map(MenuProductResponse::getProductId)
            .collect(Collectors.toList());
    }

    private Product findProductById(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElseThrow(EntityNotFoundException::new);
    }

}
