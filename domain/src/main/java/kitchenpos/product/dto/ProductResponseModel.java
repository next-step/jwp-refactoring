package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponseModel {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponseModel of(Product product) {
        return new ProductResponseModel(product.id(), product.name(), product.price());
    }

    public static List<ProductResponseModel> ofList(List<Product> products) {
        return products.stream().map(ProductResponseModel::of).collect(Collectors.toList());
    }

    public ProductResponseModel(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

}
