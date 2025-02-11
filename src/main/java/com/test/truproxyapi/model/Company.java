package com.test.truproxyapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    private String company_number;
    private String company_type;
    private String title;
    private String company_status;
    private String date_of_creation;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Officer> officers;
}
