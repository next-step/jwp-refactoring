package common.config.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfiguration {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Bean
    public InitializingBean initEvents() {
        return () -> Events.setEventPublisher(eventPublisher);
    }
}
