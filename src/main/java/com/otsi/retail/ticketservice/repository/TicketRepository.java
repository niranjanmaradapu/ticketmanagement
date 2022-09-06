package com.otsi.retail.ticketservice.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.ticketservice.common.TicketStatus;
import com.otsi.retail.ticketservice.entities.TicketEntity;

public interface TicketRepository extends JpaRepository<TicketEntity, Serializable> {

	List<TicketEntity> findByStatus(TicketStatus open);

	TicketEntity findByTicketId(String ticketId);

	List<TicketEntity> findAllByClientId(Long clientId);

	List<TicketEntity> findAllByStatusAndClientId(TicketStatus status, Long clientId);

	List<TicketEntity> findByClientIdIn(List<Long> clientIds);

	List<TicketEntity> findByStatusAndClientIdIn(TicketStatus status, List<Long> clientIds);

	List<TicketEntity> findByStatusAndTicketIdAndClientIdIn(TicketStatus status, String ticketId, List<Long> clientIds);

}
