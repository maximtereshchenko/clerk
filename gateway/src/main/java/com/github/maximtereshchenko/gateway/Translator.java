package com.github.maximtereshchenko.gateway;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper
interface Translator {

    List<ClerkReadGatewayTemplate> map(List<ClerkReadTemplate> templates);
}
