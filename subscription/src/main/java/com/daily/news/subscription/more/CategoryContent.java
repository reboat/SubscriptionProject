package com.daily.news.subscription.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryContent {

    public static final List<CategoryItem> ITEMS = new ArrayList<CategoryItem>();
    public static final Map<String, CategoryItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createCategoryItem(i));
        }
    }

    private static void addItem(CategoryItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CategoryItem createCategoryItem(int position) {
        return new CategoryItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class CategoryItem {
        public final String id;
        public final String content;
        public final String details;

        public CategoryItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
