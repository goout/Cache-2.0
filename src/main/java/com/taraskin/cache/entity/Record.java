package com.taraskin.cache.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    @Id
    @Column(name = "id")
    private Long key;

    @NonNull
    @Column(name = "text")
    private String value;

}
