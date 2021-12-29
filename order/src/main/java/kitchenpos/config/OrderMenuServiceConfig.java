package kitchenpos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kitchenpos.order.application.OrderMenuService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class OrderMenuServiceConfig {

    @Bean("orderMenuServiceOkHttpClient")
    public OkHttpClient orderMenuServiceOkHttpClient() {

        return new OkHttpClient.Builder()
                .build();
    }

    @Bean("orderMenuServiceObjectMapper")
    public ObjectMapper orderMenuServiceObjectMapper() {

        return Jackson2ObjectMapperBuilder.json()

                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .modules(new JavaTimeModule())
                .build();
    }

    @Bean("orderMenuServiceRetrofit")
    public Retrofit orderMenuServiceRetrofit(

            @Qualifier("orderMenuServiceObjectMapper") ObjectMapper orderMenuServiceObjectMapper,
            @Qualifier("orderMenuServiceOkHttpClient") OkHttpClient orderMenuServiceOkHttpClient
    ) {

        return new Retrofit.Builder()

                .baseUrl("http://localhost:8080")
                .addConverterFactory(JacksonConverterFactory.create(orderMenuServiceObjectMapper))
                .client(orderMenuServiceOkHttpClient)
                .build();
    }

    @Bean("orderMenuService")
    public OrderMenuService orderMenuService(

            @Qualifier("orderMenuServiceRetrofit") Retrofit orderMenuServiceRetrofit
    ) {
        return orderMenuServiceRetrofit.create(OrderMenuService.class);
    }
}
