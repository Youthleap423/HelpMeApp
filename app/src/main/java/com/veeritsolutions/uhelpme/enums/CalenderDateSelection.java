package com.veeritsolutions.uhelpme.enums;

/**
 * This enum contains the calender Date
 * eg. CALENDER_WITH_PAST_DATE
 * which is help to constraint calender Date selection
 */

public enum CalenderDateSelection {

    CALENDER_WITH_ALL_DATE(), // no constraint in Date selection
    CALENDER_WITH_PAST_DATE(), // To set constraint on past Date selection
    CALENDER_WITH_FUTURE_DATE() // To set constraint on future Date selection

}
