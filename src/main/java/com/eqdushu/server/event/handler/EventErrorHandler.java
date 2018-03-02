package com.eqdushu.server.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class EventErrorHandler implements ErrorHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventErrorHandler.class);

	@Override
	public void handleError(Throwable throwable) {
		LOG.error("handle event error. ", throwable);
	}
}
