package com.bloggie.server.fixtures;

import com.bloggie.server.domain.Meta;

public abstract class MetaFixtures {
    public static Meta getPageMeta() {
        Meta pageMeta = new Meta();
        pageMeta.setSeoTitle("Test page SEO title");
        pageMeta.setSeoDescription("Test page SEO description");

        return pageMeta;
    }

    public static Meta getPostMeta() {
        Meta postMeta = new Meta();
        postMeta.setSeoTitle("Test post SEO title");
        postMeta.setSeoDescription("Test post SEO description");

        return postMeta;
    }
}

