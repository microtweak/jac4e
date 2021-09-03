package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.testing.converter.CountryAttributeEnumeratedAttributeConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    private Gender gender;

    @Setter
    @Convert(converter = CountryAttributeEnumeratedAttributeConverter.class)
    private Country country;

}
