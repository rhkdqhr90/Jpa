package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Item;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("MV")
public class Movie extends Item {
    private String director;
    private String actor;
}
