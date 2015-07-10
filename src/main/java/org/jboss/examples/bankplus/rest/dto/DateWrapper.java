package org.jboss.examples.bankplus.rest.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateWrapper implements Serializable {

    private SimpleDateFormat ISO8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public DateWrapper (String dateString) {
        try {
            date = new Date(dateString);
        } catch(Exception e) {
            try {
                date = ISO8601_FORMAT.parse(dateString);
            } catch (ParseException parseEx) {
                throw new IllegalArgumentException("Unable to parse date");
            }
        }
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
