/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.rss.client.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;

public class FeedEntryBeanTest {
    @Mock
    private SyndEntry syndEntry;
    @Mock
    private SyndContent syndDescription;
    @Mock
    private SyndContent syndContent;
    @Mock
    private ArrayList<SyndContent> contentList;
    @Mock
    private Date publishedDate;
    @Mock
    private FeedEntryBean feedEntryBeanSample1;
    @Mock
    private FeedEntryBean feedEntryBeanSample2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void shouldConstructFeedEntryBean() {
        // Given
        String descriptionValue = "description value";
        constructFeedBean(descriptionValue, true);

        // When
        String feedTitle = "feed title";
        FeedEntryBean feedEntryBean = new FeedEntryBean(syndEntry, feedTitle);

        // Then
        assertEquals("description value", feedEntryBean.getExcerpt());
        assertEquals(feedTitle, feedEntryBean.getFeedTitle());
        assertEquals("title", feedEntryBean.getTitle());
        assertEquals("content value", feedEntryBean.getContentsString());
        assertEquals("link", feedEntryBean.getLink());
        assertSame(publishedDate, feedEntryBean.getPublishedDate());
    }

    @Test
    public final void shouldConstructAbbreviatedDescription() {
        // Given
        // 20 x 0123456789 + abc
        String veryLongDescriptionValue = "";
        for (int i = 0; i < 21; i++) {
            veryLongDescriptionValue += "0123456789";
        }
        veryLongDescriptionValue += "abc";
        constructFeedBean(veryLongDescriptionValue, true);

        // When
        String feedTitle = "feed title";
        FeedEntryBean feedEntryBean = new FeedEntryBean(syndEntry, feedTitle);

        assertEquals(StringUtils.abbreviate(veryLongDescriptionValue, FeedEntryBean.SHORT_EXCERPT_LENGTH),
                feedEntryBean.getShortExcerpt());
    }

    @Test
    public final void shouldPutDescriptionInContentIfThereIsNoDescription() {
        // Given
        constructFeedBean("description value", false);

        // When
        String feedTitle = "feed title";
        FeedEntryBean feedEntryBean = new FeedEntryBean(syndEntry, feedTitle);

        // Then
        assertEquals("description value", feedEntryBean.getContentsString());
    }

    @Test
    public void shouldSortByPublishDate() throws Exception {
        given(feedEntryBeanSample1.getPublishedDate()).willReturn(new Date());
        Calendar longTimeAgoCalendar = Calendar.getInstance();
        longTimeAgoCalendar.set(1970, 1, 1);
        given(feedEntryBeanSample2.getPublishedDate()).willReturn(longTimeAgoCalendar.getTime());
        int compareResult = FeedEntryBean.SORT_BY_DATE.compare(feedEntryBeanSample2, feedEntryBeanSample1);
        assertEquals(1, compareResult);
    }

    @Test
    public void shouldSortByFeedTitle() throws Exception {
        given(feedEntryBeanSample1.getFeedTitle()).willReturn("Title 1");
        given(feedEntryBeanSample2.getFeedTitle()).willReturn("Title 2");
        int compareResult = FeedEntryBean.GROUP_BY_SOURCE.compare(feedEntryBeanSample1, feedEntryBeanSample2);
        assertEquals(-1, compareResult);
    }
    
    private void constructFeedBean(String descriptionValue, boolean addContent) {
        // Given
        given(syndEntry.getTitle()).willReturn("title");
        given(syndEntry.getDescription()).willReturn(syndDescription);
        given(syndDescription.getValue()).willReturn(descriptionValue);
        given(syndEntry.getLink()).willReturn("link");
        given(syndEntry.getPublishedDate()).willReturn(publishedDate);

        if (addContent) {
            contentList.add(syndContent);
            given(syndEntry.getContents()).willReturn(contentList);
            given(syndContent.getValue()).willReturn("content value");
            given(contentList.size()).willReturn(1);
            given(contentList.get(0)).willReturn(syndContent);
        }
    }
}