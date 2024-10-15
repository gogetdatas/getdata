package com.gogetdata.datamanagemant.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyData {

    @Id
    private String id;

    private Long companyId;
    @NotNull
    @Indexed
    private String type;

    @NotNull
    @Indexed
    private String subtype;

    @NotNull
    private JsonNode data;

    private String keyHash;
}
