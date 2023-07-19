package com.questionpro.hackerNewsService.Configuration;

import com.questionpro.hackerNewsService.Entity.Item;
import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        Converter<Integer, Integer> idToStoryIdConverter = context -> context.getSource();

        TypeMap<ItemDto, Item> typeMap = modelMapper.createTypeMap(ItemDto.class, Item.class);
        typeMap.addMappings(mapper -> mapper.using(idToStoryIdConverter)
                .map(ItemDto::getId, Item::setStoryId));

        return modelMapper;
    }
}
