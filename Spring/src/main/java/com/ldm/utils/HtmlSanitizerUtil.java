package com.ldm.utils;

import org.owasp.html.CssSchema;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HtmlSanitizerUtil {

    private static final PolicyFactory BASE_POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.LINKS)
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.IMAGES);

    private static final PolicyFactory EXTENDED_POLICY;

    static {
        HtmlPolicyBuilder builder = new HtmlPolicyBuilder()
                .allowElements("span")
                .allowAttributes("style").onElements("span")
                .allowStyling(CssSchema.DEFAULT);

        EXTENDED_POLICY = BASE_POLICY.and(builder.toFactory());
    }

    public static String sanitize(String html) {
        return EXTENDED_POLICY.sanitize(html);
    }
}