package org.jboss.examples.bankplus.core.rest.exception;

import org.jboss.examples.bankplus.core.exception.BusinessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageHolder(e)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
