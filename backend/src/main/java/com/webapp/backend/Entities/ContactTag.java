package com.webapp.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact_tag",
        uniqueConstraints = @UniqueConstraint(columnNames = {"contact_id", "tag_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
