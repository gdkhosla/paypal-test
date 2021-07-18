package com.paypal.bfs.test.bookingserv.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing booking records
 *
 */
@Entity
@Table(name = "booking")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Booking {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "first_name", length = 255, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 255, nullable = false)
	private String lastName;

	@Column(name = "date_of_birth", nullable = false)
	private Long dateOfBirth;

	@Column(name = "check_in", nullable = false)
	private Long checkIn;

	@Column(name = "check_out")
	private Long checkOut;

	@Column(name = "total_price", nullable = false)
	private Double totalPrice;

	@Column(name = "deposit", nullable = false)
	private Double deposit;

	@OneToOne( 
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address address;
}
