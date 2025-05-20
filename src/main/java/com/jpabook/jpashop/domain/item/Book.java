package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Item;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("BK")
public class Book extends Item {
    private String author;
    private String isbn;
}
