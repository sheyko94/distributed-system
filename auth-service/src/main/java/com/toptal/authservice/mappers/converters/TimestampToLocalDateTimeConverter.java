package com.toptal.authservice.mappers.converters;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TimestampToLocalDateTimeConverter extends AbstractConverter<Timestamp, LocalDateTime> {

  @Override
  protected LocalDateTime convert(Timestamp source) {
    return source != null ? source.toLocalDateTime() : null;
  }

}
