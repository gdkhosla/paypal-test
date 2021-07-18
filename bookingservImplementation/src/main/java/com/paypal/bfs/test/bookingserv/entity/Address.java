package com.paypal.bfs.test.bookingserv.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing address
 *
 */
@Entity
@Table(name = "address")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Address {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "line_1", length = 255, nullable = false)
	private String line1;
	
	@Column(name = "line_2", length = 255, nullable = true)
	private String line2;
	
	@Column(name = "city", length = 255, nullable = false)
	private String city;
	
	@Column(name = "state", length = 255, nullable = false)
	private String state;
	
	@Column(name = "country", length = 255, nullable = false)
	private String country;
	
	@Column(name = "zip_code", length = 255, nullable = false)
	private String zipCode;
	
	@OneToOne(mappedBy = "address", cascade = CascadeType.REFRESH,
            fetch = FetchType.LAZY, optional = false)
	private Booking booking;

}
