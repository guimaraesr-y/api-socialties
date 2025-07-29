package br.com.socialties.domain.storage;

import br.com.socialties.domain.storage.strategies.LocalStorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@RequiredArgsConstructor
public class StorageFactory {

    private final StorageProperties properties;

    public static StorageService getStorageService(StorageType storageType, StorageProperties properties) {
        return switch (storageType) {
            case LOCAL -> new LocalStorageStrategy(properties);
            default -> throw new UnsupportedOperationException();
        };
    }

    @Bean
    public StorageService storageService() {
        StorageType storageType = properties.getType();
        return getStorageService(storageType, properties);
    }

}
