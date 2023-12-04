package com.github.maximtereshchenko.gateway;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper
interface Translator {

    List<ClerkReadGatewayTemplate> mapTemplates(List<ClerkReadTemplate> templates);

    ClerkReadGatewayPlaceholders map(ClerkReadPlaceholders placeholders);

    List<ClerkReadGatewayDocument> mapDocuments(List<ClerkReadDocument> documents);
}
