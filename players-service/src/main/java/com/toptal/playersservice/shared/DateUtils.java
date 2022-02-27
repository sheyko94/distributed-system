package com.toptal.playersservice.shared;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtils {

  public static Timestamp getLocalNow() {
    Date date = new Date();
    return new Timestamp(date.getTime());
  }

}
