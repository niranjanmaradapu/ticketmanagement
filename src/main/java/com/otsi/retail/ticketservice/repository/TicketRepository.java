package com.otsi.retail.ticketservice.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.ticketservice.common.TicketStatus;
import com.otsi.retail.ticketservice.entities.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, Serializable> {

	List<TicketEntity> findAllByStatus(TicketStatus status);

	//List<TicketEntity> findByStatusIn(TicketStatus status);

}
