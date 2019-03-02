package com.vmzone.demo.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vmzone.demo.dto.ListSubCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "category_id")
	private Long categoryId;

	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	public Category(String name, Category parent) {
		super();
		this.name = name;
		this.parent = parent;
	}
//
//	@OneToMany(mappedBy = "parent")
//	@JsonIgnoreProperties("parent")
//	private List<Category> subCategories;

}
