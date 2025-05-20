package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Item;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("AL")
public class Album extends Item {
    private String artist;
    private String etc;

}
