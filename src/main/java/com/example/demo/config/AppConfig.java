package com.example.demo.config;

import com.example.demo.dto.ComplaintRequestDto;
import com.example.demo.model.Complaint;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        configureTypeMaps(modelMapper);
        return modelMapper;
    }

    private void configureTypeMaps(ModelMapper modelMapper) {
        TypeMap<ComplaintRequestDto, Complaint> propertyMapper = modelMapper.createTypeMap(ComplaintRequestDto.class, Complaint.class);
        Converter<String, UUID> converter = c -> c.getSource()==null? null : UUID.fromString(c.getSource());
        propertyMapper.addMappings(
                mapper -> mapper.using(converter).map(ComplaintRequestDto::getUserId, Complaint::setUserId)
        );
        propertyMapper.addMappings(
                mapper -> mapper.using(converter).map(ComplaintRequestDto::getPurchaseId, Complaint::setPurchaseId)
        );
    }
}