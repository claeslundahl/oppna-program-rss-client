package se.vgregion.portal.rss.client.chain;

import java.util.*;

public class MockProcessor extends StringTemplatePlaceholderProcessor {
    @Override
    protected Set<String> getKeys(String userId) {
        if (userId.equals("OneKey")) {
            return new HashSet<String>(Arrays.asList("key1"));
        } else if (userId.equals("TwoKey")) {
            return new HashSet<String>(Arrays.asList("key1", "key2"));
        } else if (userId.equals("ThreeKey")) {
            return new HashSet<String>(Arrays.asList("key1", "key2", "key3"));
        } else if (userId.equals("NoKey")) {
                return new HashSet<String>(Arrays.asList("nokey"));
        } else {
            return new HashSet<String>();
        }

    }

    @Override
    protected Map<String, String> getReplaceValues() {
        Map<String, String> replaceValues = new HashMap<String, String>();
        replaceValues.put("key1", "value1");
        replaceValues.put("key2", "value2");
        replaceValues.put("key3", "value3");
        return replaceValues;
    }
}