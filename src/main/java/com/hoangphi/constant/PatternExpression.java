package com.hoangphi.constant;

import java.util.regex.Pattern;

public class PatternExpression {

    public static Pattern NOT_SPECIAL = Pattern.compile("[^a-zA-Z0-9]");

    public static Pattern NOT_SPECIAL_SPACE = Pattern.compile("[^a-zA-Z0-9 ]");

    public static Pattern IS_PHONE_VALID = Pattern.compile("^0[0-9]{9}");

}
