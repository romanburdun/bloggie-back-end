package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Setter
@Getter
public class CustomField extends BaseEntity{
    private String fieldName;
    private String value;
    @Enumerated(value = EnumType.STRING)
    private CustomFieldType type;


    @Override
    public boolean equals(Object obj) {
        CustomField anotherField = (CustomField) obj;

        if(!this.getFieldName().equals(anotherField.getFieldName())) {
            return false;
        }

        if(!this.getValue().equals(anotherField.getValue())) {
            return false;
        }

        if(!this.getType().equals(anotherField.getType())) {
            return false;
        }

        return true;
    }
}
