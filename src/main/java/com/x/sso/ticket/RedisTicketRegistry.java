package com.x.sso.ticket;

import java.util.Collection;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.x.sso.exception.SystemException;

import javax.validation.constraints.Min;

@Component("redisTicketRegistry")
public class RedisTicketRegistry extends AbstractDistributedTicketRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(RedisTicketRegistry.class);

	private static final  String TICKECKKEY = "SSO-";

	@Min(0)
	@Value("${tgt.maxTimeToLiveInSeconds:28800}")
	private int tgtTimeout;

	@Min(0)
	@Value("${st.timeToKillInSeconds:10}")
	private int stTimeout;

	@Override
	public void addTicket(final Ticket paramTicket) {
		LOG.debug("-------------------------------------------RedisTicketRegistry-----addTicket-----------------------------------------------begin");
		if(paramTicket == null){
			LOG.info("ticket is null");
			return;
		}
		LOG.debug("------------------------------------------ticket-----------------------------------------------"+paramTicket);
		String ticketKey = TICKECKKEY+paramTicket.getId();
		LOG.debug("------------------------------------------tecketId-----------------------------------------------"+ticketKey);
		int seconds = 0;
		if(paramTicket instanceof TicketGrantingTicket){
			seconds = tgtTimeout;
		}else{
			seconds = stTimeout;
		}
		
		try {
			new TicketService().saveTicket(ticketKey,paramTicket,seconds);
			LOG.debug("------------------------------------------write-------------------------------ok----------------");
		} catch (Exception e) {
			LOG.error("--------------------------------------adding ticket to redis error.",e);
		}
		LOG.debug("-------------------------------------------RedisTicketRegistry-----addTicket-----------------------------------------------end");
	}


	@Override
	public boolean deleteSingleTicket(String ticketId) {
		if(ticketId == null){
			return false;
		}
		try {
			new TicketService().deleteTicket(TICKECKKEY+ticketId);
			return true;
		} catch (Exception e) {
			LOG.error("-----del ticket["+TICKECKKEY+ticketId+"] error!-----",e);
		}
		return false;
	}

	@Override
	public Ticket getTicket(final String ticketId) {
		return getProxiedTicketInstance(getRawTicket(ticketId));
	}

	private Ticket getRawTicket(String ticketId) {
		if(ticketId == null){
			return null;
		}
		Ticket ticket = null;
		try {
			ticket = new TicketService().getTicket(TICKECKKEY+ticketId);
		} catch (Exception e) {
			LOG.error("------------------------getting ticket from redis error.",e);
		}
		return ticket;
	}

	@Override
	public Collection<Ticket> getTickets() {
		try {
			return new TicketService().getTickets();
		} catch (Exception e) {
			throw new SystemException("GetTickets not supported.",e);
		}
	}

	@Override
	protected boolean needsCallback() {
		return false;
	}

	@Override
	protected void updateTicket(final Ticket ticket) {
		addTicket(ticket);
	}

	public int getTimeout(final Ticket t) {
		if (t instanceof TicketGrantingTicket) {
			return tgtTimeout;
		} else if (t instanceof ServiceTicket) {
			return stTimeout;
		}
		throw new IllegalArgumentException("Invalid ticket type");
	}

	public void setTgtTimeout(final int tgtTimeout) {
		this.tgtTimeout = tgtTimeout;
	}

	public int getTimeout() {
		return stTimeout;
	}

	public void setStTimeout(final int stTimeout) {
		this.stTimeout = stTimeout;
	}
}
