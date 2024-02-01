package com.alexv.database.mapper.impl;

import com.alexv.database.domain.entity.AuthorEntity;
import com.alexv.database.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperImpl implements Mapper<AuthorEntity, com.alexv.database.domain.dto.AuthorDTO> {

    private ModelMapper modelMapper;

    @Autowired
    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public com.alexv.database.domain.dto.AuthorDTO mapTo(AuthorEntity authorEntity) {
        return modelMapper.map(authorEntity, com.alexv.database.domain.dto.AuthorDTO.class);
    }

    @Override
    public AuthorEntity mapFrom(com.alexv.database.domain.dto.AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, AuthorEntity.class);
    }
}
